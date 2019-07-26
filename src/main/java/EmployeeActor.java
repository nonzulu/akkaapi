import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static class Employee {
        private final String name;
        private final int age;


    public Employee(){
        this.name="";
        this.age= 34;
    }


        public Employee(String name, int age) {
            this.name = name;
            this.age = age;

        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public static class Employees {
            private final List<Employee> employees;

            public Employees() {
                this.employees = new ArrayList<>();
            }

            public Employees(List<Employee> employees) {
                this.employees = employees;
            }

            public List<Employee> getEmployees() {
                return employees;
            }
        }

    }
    static Props props() {

        return Props.create(EmployeeActor.class);
    }
    private final List<Employee> employees = new ArrayList<>();
  @Override
  public Receive createReceive(){
     return receiveBuilder()
              .match(EmployeeMessages.GetEmployees.class, getEmployees ->getSender().tell(new Employee.Employees(employees),getSelf()))
              .match(EmployeeMessages.CreateEmployee.class, createEmployee -> {
                  employees.add(createEmployee.getEmployee());
                  getSender().tell(new EmployeeMessages.ActionPerformed(
                          String.format("Employee %s created.", createEmployee.getEmployee().getName())),getSelf());

                  })
              .match(EmployeeMessages.GetEmployee.class, getEmployee -> {
                 getSender().tell(employees.stream()
                         .filter(employee->employee.getName().equals(getEmployee.getName()))
                         .findFirst(), getSelf());

              })
               .match(EmployeeMessages.DeleteEmployee.class, deleteEmployee -> {
                   employees.removeIf(employee->employee.getName().equals(deleteEmployee.getName()));
                   getSender().tell(new EmployeeMessages.ActionPerformed(String.format("User %s deleted.", deleteEmployee.getName())),
                            getSelf());

                   }).matchAny(o->log.info("received unknown message"))
                     .build();
               }

              }



