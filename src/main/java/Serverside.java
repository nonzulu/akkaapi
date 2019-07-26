import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;

import akka.stream.javadsl.Flow;

import java.util.concurrent.CompletionStage;

public class Serverside extends AllDirectives {

    private final EmployeeRoutes employeeRoutes;

    public Serverside (ActorSystem system, ActorRef employeeActor){
        employeeRoutes = new EmployeeRoutes(system, employeeActor);
    }



//     public static  final String DB_Name="employee.db";
//
//     public static final String CONNECTION_STRING = "jdbc:sqlite:/C:/Users/User/Documents/akkaapi/"+DB_Name;
//     public static final String TABLE_NAME= "employee_records";
//     public static final String COLUMN_ID="_id";
//     public static final String COLUMN_NAME="name";
//     public static final String COLUMN_AGE="age";
//
//     private Connection conn;
//
//     public boolean open(){
//         try {
//             conn= DriverManager.getConnection(CONNECTION_STRING);
//             return true;
//         }catch(SQLException e){
//             System.out.println("Couldn't connect to database: " +e.getMessage());
//             return false;
//         }
//     }



    public static void main (String[] args)throws Exception{

        ActorSystem system = ActorSystem.create("routes");

        final Http http  = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);


        ActorRef employeeActor = system.actorOf(EmployeeActor.props(), "employeeActor");

        Serverside app = new Serverside(system, employeeActor);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
//        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
       http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/");


    }

    protected Route createRoute() {
        return employeeRoutes.routes();
    }



}
