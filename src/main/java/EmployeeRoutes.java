import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

import java.time.Duration;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class EmployeeRoutes extends AllDirectives {
    final private ActorRef employeeActor;
    final private LoggingAdapter log;

    public EmployeeRoutes(ActorSystem system, ActorRef employeeActor){
        this.employeeActor = employeeActor;
        log = Logging.getLogger(system, this);
    }
    Duration timeout = Duration.ofSeconds(51);

    public Route routes() {
        return route(pathPrefix("employees", () ->
                route(
                        getOrPostEmployees(),
                        path(PathMatchers.segment(), name -> route(
                                getEmployee(name),
                                deleteEmployee(name)
                                )
                        )
                )
        ));
    }


// get employee by name
    private Route getEmployee(String name){
        return get(()->{
            CompletionStage<Optional<EmployeeActor.Employee>> maybeEmployee = Patterns
                    .ask(employeeActor, new EmployeeMessages.GetEmployee(name), timeout)
                    .thenApply(Optional.class::cast);

         return onSuccess(()-> maybeEmployee,
                 performed ->{
                       if (performed.isPresent())
                           return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                           else
                               return complete(StatusCodes.NOT_FOUND);
                 }) ;
                    });
        }

        //Delete employee by name
    private Route deleteEmployee(String name){
        return
                delete(()->{
                    CompletionStage<EmployeeMessages.ActionPerformed>employeeDeleted = Patterns
                          .ask(employeeActor, new EmployeeMessages.DeleteEmployee(name),timeout)
                          .thenApply(EmployeeMessages.ActionPerformed.class::cast);

                    return onSuccess(() -> employeeDeleted,
                        performed->{
                        log.info("Deleted employee[{}]:{}", name, performed.getDescription());
                        return complete(StatusCodes.OK, performed, Jackson.marshaller());
                        }    );
                          }) ;
                }




    private Route getOrPostEmployees() {
        return pathEnd(() ->
                route(
                        get(() -> {
                            CompletionStage<EmployeeActor> futureEmployees = Patterns
                                    .ask(employeeActor, new EmployeeMessages.GetEmployees(), timeout)
                                    .thenApply(EmployeeActor.class::cast);
                            return onSuccess(() -> futureEmployees,
                                    employees -> complete(StatusCodes.OK, employees, Jackson.marshaller()));
                        }),
                        post(() ->
                                entity(
                                        Jackson.unmarshaller(EmployeeActor.Employee.class),
                                        employee -> {
                                            CompletionStage<EmployeeMessages.ActionPerformed> employeeCreated = Patterns
                                                    .ask(employeeActor, new EmployeeMessages.CreateEmployee(employee), timeout)
                                                    .thenApply(EmployeeMessages.ActionPerformed.class::cast);
                                            return onSuccess(() -> employeeCreated,
                                                    performed -> {
                                                        log.info("Created employee[{}]: {}", employee.getName(), performed.getDescription());
                                                        return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
                                                    });
                                        }))));
    }




     }













