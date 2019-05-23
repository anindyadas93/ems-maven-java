package maven.java.workspace.ems.controllers;

import maven.java.workspace.ems.bootstrap.EmployeeBootstrap;
import maven.java.workspace.ems.model.Employee;
import maven.java.workspace.ems.repositories.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    private EmployeeRepository employeeRepository;

    public AdminController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(value = "/register")
    public String employeeRegister(){
        return "employee-register";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute Employee employee,
                             @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("dateofbirth") String dateofbirth,
                             @RequestParam("address") String address,
                             @RequestParam("phoneno") String phoneno,
                             @RequestParam("emailid") String emailid,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-data");
        modelAndView.addObject("employee", employee);
        EmployeeBootstrap empb = new EmployeeBootstrap(employeeRepository);
        empb.setEmployee(firstname, lastname, dateofbirth, address, phoneno, emailid, username, password);
        return modelAndView;
    }

}
