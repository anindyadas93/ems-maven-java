package maven.java.workspace.ems.bootstrap;

import maven.java.workspace.ems.model.Employee;
import maven.java.workspace.ems.repositories.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeBootstrap {

    private EmployeeRepository employeeRepository;

    public EmployeeBootstrap(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void/*List<Employee>*/ setEmployee(String firstname,String lastname,String dateofbirth,String address,String phoneno,String emailid,String username,String password){
        List<Employee> employee = new ArrayList<>();
        Employee emp = new Employee();
        emp.setFirstname(firstname);
        emp.setLastname(lastname);
        emp.setDateofbirth(dateofbirth);
        emp.setAddress(address);
        emp.setPhoneno(phoneno);
        emp.setEmailid(emailid);
        emp.setUsername(username);
        emp.setPassword(password);

        employee.add(emp);
        employeeRepository.saveAll(employee);
        //return employee;

    }
    //@Override
    //public void onApplicationEvent(ContextRefreshedEvent event) {

    //employeeRepository.saveAll(setEmployee());

    //}
}
