import java.io.Serializable;

public interface EmployeeMessages {
    class GetEmployees implements Serializable{

    }

    class ActionPerformed implements Serializable{
        private final String description;

        public ActionPerformed(String description){

            this.description=description;
        }

        public String getDescription() {

            return description;
        }
    }

    class CreateEmployee implements  Serializable{
        private final EmployeeActor.Employee employee;

        public CreateEmployee(EmployeeActor.Employee employee){
            this.employee = employee;
        }

        public EmployeeActor.Employee getEmployee(){
            return employee;
        }
    }

    class GetEmployee implements Serializable{
        private final String name;
        public GetEmployee(String name){
            this.name= name;
        }

        public String getName(){

            return name;
        }
    }

    class DeleteEmployee implements Serializable{
        private final String name;
        public DeleteEmployee(String name){

            this.name=name;
        }
        public String getName(){
            return name;
        }
    }



}
