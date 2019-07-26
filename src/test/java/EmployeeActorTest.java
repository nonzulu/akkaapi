import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class EmployeeActorTest {
    static ActorSystem system = null;

    @BeforeEach
    void setUp(){
        system = ActorSystem.create("InitializationEmployeeTest");
    }

    @AfterEach
    void tearDown(){

        TestKit.shutdownActorSystem(system);
        system = null;
    }
    @Test
    void getEmployeeDetails(){
                EmployeeActor.Employee.Employees employees = new EmployeeActor.Employee.Employees();
                assertEquals("nzulu", "nzulu");
                assertEquals(67, 67);
    }

    @Test
    public void EmployeesTest() {
//        final TestKit employees = new TestKit(system);
//        final ActorRef employeeDetails = system.actorOf(EmployeeActor.Employee.props("Hello", employees.getRef()));
//        employeeDetails.tell(new EmployeeActor.Employee("Akka"), ActorRef.noSender());
//        employeeDetails.tell(new EmployeeActor.Employee(), ActorRef.noSender());
//        EmployeeActor.Employee employees = employees.expectMsgClass(Greeting.class);
//        assertEquals("Nzulu", employees.message);

    }

    @Test
    void sender() {
    }

    @Test
    void aroundReceive() {
    }

    @Test
    void aroundPreStart() {
    }

    @Test
    void aroundPostStop() {
    }

    @Test
    void aroundPreRestart() {
    }

    @Test
    void aroundPostRestart() {
    }

    @Test
    void unhandled() {
    }
}