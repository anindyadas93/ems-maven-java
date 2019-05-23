package maven.java.workspace.ems.controllers;

import maven.java.workspace.ems.model.Employee;
import maven.java.workspace.ems.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

//import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/")
    public List<Employee> getAllEmployee(){
        return employeeService.getAllEmployee();
    }

    @RequestMapping(value="/add-user",method= RequestMethod.POST)
    public void addEmployee(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

    @RequestMapping(value="/employee/{id}",method=RequestMethod.GET)
    public Optional<Employee> getEmployee(@PathVariable Long id){
        return employeeService.getEmployee(id);
    }

    @RequestMapping(value="/employee/users/{username}",method = RequestMethod.GET)
    public Optional<Employee> getEmployeeByUser(@PathVariable String username){
        return employeeService.getEmployeeByUsername(username);
    }

    @Autowired
    JdbcTemplate jdbc;

    @RequestMapping("/insert")
    public String insert() {
        String first_name = "Anindya Das";
        String last_name = "Das";
        String date_of_birth = "03/02/1993";
        String address = "Bhadreswar";
        String phone_no = "9007595923";
        String email_id = "anindya@gmail.com";
        String user_name = "anindya";
        String password = "12345";
        jdbc.execute("insert into employee(first_name,last_name,date_of_birth,address,phone_no,email_id,user_name,password)"
                + "values('"+first_name+"','"+last_name+"','"+date_of_birth+"','"+address+"','"+phone_no+"','"+email_id+"','"+user_name+"','"+password+"')");
        return "data inserted successfully";
    }
}
