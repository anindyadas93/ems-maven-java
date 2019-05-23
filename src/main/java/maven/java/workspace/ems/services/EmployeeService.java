package maven.java.workspace.ems.services;

import maven.java.workspace.ems.model.Employee;
import maven.java.workspace.ems.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    //Find all record of employee
    public List<Employee> getAllEmployee(){
        List<Employee> employee = new ArrayList<Employee>();
        employeeRepository.findAll().forEach(employee::add);
        return employee;
    }
    //Find record of specific employee
    public Optional<Employee> getEmployee(Long id){
        return employeeRepository.findById(id);
    }
    //Save record of new employee
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
    //Delete record of specific employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    //Find record by username
    public Optional<Employee> getEmployeeByUsername(String username){
        return employeeRepository.findByFirstname(username);
    }
}
