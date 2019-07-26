import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeRoutesTest extends JUnitRouteTest {
private TestRoute employeeRoute;
static ActorSystem system;

  @BeforeClass
   public static void initClass(){
    system = ActorSystem.create();
}

    @Test
public void  testNoEmployees(){
        employeeRoute.run(HttpRequest.GET("/employees"))
                     .assertStatusCode(StatusCodes.OK)
                     .assertMediaType("application/json")
                     .assertEntity("{\"employees\":[]}");

    }









}
