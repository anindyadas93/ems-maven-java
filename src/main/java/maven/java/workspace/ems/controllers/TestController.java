package maven.java.workspace.ems.controllers;

import maven.java.workspace.ems.bootstrap.AttendanceBootstrap;
import maven.java.workspace.ems.bootstrap.BreakBootstrap;
import maven.java.workspace.ems.model.Attendance;
import maven.java.workspace.ems.model.Break;
import maven.java.workspace.ems.model.Employee;
import maven.java.workspace.ems.repositories.AttendanceRepository;
import maven.java.workspace.ems.repositories.BreakRepository;
import maven.java.workspace.ems.repositories.EmployeeRepository;
import maven.java.workspace.ems.services.AttendanceService;
import maven.java.workspace.ems.services.BreakService;
import maven.java.workspace.ems.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


@Controller
public class TestController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private BreakService breakService;

    private EmployeeRepository employeeRepository;

    private AttendanceRepository attendanceRepository;

    private BreakRepository breakRepository;

    //LocalDateTime now = LocalDateTime.now();

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    Date breakStart = new Date();
    Date breakEnd = new Date();

    String totalbreak;
    Long employeeId = null;

    LocalDateTime localDateTime = LocalDateTime.now();

    String currentDate = localDateTime.getDayOfMonth() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getYear();

    @Autowired
    JdbcTemplate jdbcTemplate;

    public TestController(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository, BreakRepository breakRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.breakRepository = breakRepository;
    }

    public void onLoad() {

        String setOutTime = "20:0:0";
        List<Attendance> attendanceList = attendanceRepository.findAll();

        int size = attendanceList.size();
        int i = 0;
        while (i < size) {
            String inTime = attendanceList.get(i).getInTime();
            String outTime = attendanceList.get(i).getOutTime();
            String workingDate = attendanceList.get(i).getWorkingDate();
            String breakTotalTime = attendanceList.get(i).getBreakTotaltime();
            if(outTime==null){
                System.out.println("Checked");
            }
            if (breakTotalTime == null) {
                breakTotalTime = "0:0:0";
            }
            if (inTime != null && outTime == null) {
                jdbcTemplate.update("update attendance set out_time='" + setOutTime + "' where working_date!='" + currentDate + "'");
            }
            String time = null;
            try {
                breakStart = dateFormat.parse(inTime);
                breakEnd = dateFormat.parse(outTime);

                time = calculateBreak(breakStart, breakEnd);

                breakStart = dateFormat.parse(time);
                breakEnd = dateFormat.parse(breakTotalTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String workingTime = calculateBreak(breakEnd, breakStart);
            jdbcTemplate.update("update attendance set total_time='" + time + "',working_time='" + workingTime + "' where in_time='" + inTime + "' and out_time='" + outTime + "' and working_date='" + workingDate + "'");
            i++;
        }
    }

    @RequestMapping("/test")
    public String getTestPage() {
        return "Test";
    }

    @RequestMapping("/showemployee")
    public String showEmployee(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployee());
        return "showemployee";
    }

    @RequestMapping(value = "/index")
    public String getIndexPage() {
        return "index";
    }

    /*
    @RequestMapping(value = "/register")
    public String getRegisterPage() {
        return "register";
    }

     */

    @RequestMapping(value = "/delete")
    public String getDeletePage(Long id) {
        id = (long) 4;
        employeeService.deleteEmployee(id);
        return "delete";
    }

    @RequestMapping(value = "/login")
    public String getLoginPage() {
        onLoad();
        return "login";
    }

    @RequestMapping(value = "/attendanceRegister")
    public String attendanceRegister(Model model){
        if(employeeId==null){
            return "login";
        }
        List<Break> breakList = breakService.getBreaks1(employeeId,currentDate);
        model.addAttribute("breaks",breakList);
        return "attendance-register";
    }

    @RequestMapping(value = "/attendances")
    public String getAttendance(Model model){
        if(employeeId==null){
            return "login";
        }
        Set<Attendance> attendanceSet = attendanceService.getEmpByEmployeeId(employeeId);
        model.addAttribute("attendances",attendanceSet);
        return "attendances";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String getLogin(@ModelAttribute Employee employee,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByUsername(username);
        String pass = employeeOptional.get().getPassword();

        if (pass.equals(password)) {
            employeeId = employeeOptional.get().getId();
            LocalDateTime now = LocalDateTime.now();
            String inTime = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
            String workingDate = now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
            currentDate = workingDate;
            AttendanceBootstrap attnb = new AttendanceBootstrap(attendanceRepository);
            Set<Attendance> attendanceOptional = attendanceService.getEmployeeByEmployeeId(employeeId, workingDate);
            int noOfLate = checkLate(employeeId);
            model.addAttribute("noOfLate", noOfLate);

            if (attendanceOptional.isEmpty()) {
                attnb.setAttendance(inTime, workingDate, employeeId);
                attendanceOptional = attendanceService.getEmployeeByEmployeeId(employeeId, workingDate);
            }
            model.addAttribute("employee", employeeOptional.get());
            model.addAttribute("attendances", attendanceOptional);
            return "profile";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/profile",method = RequestMethod.GET)
    public String viewProfile(Model model){
        if(employeeId==null){
            return "login";
        }

        Optional<Employee> employeeOptional = employeeService.getEmployee(employeeId);
        model.addAttribute("employee",employeeOptional.get());

        return "profile";
    }

    /*
    @RequestMapping(value = "/logindata", method = RequestMethod.POST)
    public ModelAndView saveLogin(@ModelAttribute Employee employee,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  Model model) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByUsername(username);

        ModelAndView modelAndView = new ModelAndView();

        String pass = employeeOptional.get().getPassword();

        modelAndView.setViewName("login-data");
        if (pass.equals(password)) {
            employeeId = employeeOptional.get().getId();
            LocalDateTime now = LocalDateTime.now();
            String inTime = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
            String workingDate = now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
            currentDate = workingDate;
            AttendanceBootstrap attnb = new AttendanceBootstrap(attendanceRepository);
            //attnb.setAttendance(inTime,workingDate,employeeId);

            Set<Attendance> attendanceOptional = attendanceService.getEmployeeByEmployeeId(employeeId, workingDate);

            int noOfLate = checkLate(employeeId);
            model.addAttribute("noOfLate", noOfLate);

            if (attendanceOptional.isEmpty()) {
                attnb.setAttendance(inTime, workingDate, employeeId);
                attendanceOptional = attendanceService.getEmployeeByEmployeeId(employeeId, workingDate);
            }
            model.addAttribute("employee", employeeOptional.get());
            model.addAttribute("attendances", attendanceOptional);

            /*
            ...............................
            if (!attendanceOptional.isEmpty()) {
                modelAndView.addObject("employee", employeeOptional.get());
                //modelAndView.addObject("intime", attendanceOptional.get().getInTime());
                //modelAndView.addObject("workingdate", attendanceOptional.get().getWorkingDate());
                //modelAndView.addObject("totalbreaktime",attendanceOptional.get().getBreakTotaltime());

                model.addAttribute("attendances",attendanceOptional);

                //modelAndView.addObject("breaks",breakList);
                return modelAndView;
            } else {
                attnb.setAttendance(inTime, workingDate, employeeId);
                modelAndView.addObject("employee", employeeOptional.get());
                //modelAndView.addObject("intime", inTime);
                //modelAndView.addObject("workingdate", workingDate);
                attendanceOptional = attendanceService.getEmployeeByEmployeeId(employeeId, workingDate);

                model.addAttribute("attendances",attendanceOptional);

                //modelAndView.addObject("breaks",breakList);
                return modelAndView;
            }
            .........................
            */
            /*String workingDateDb = attendanceOptional.get().getWorkingDate();
            if(!workingDate.equals(workingDateDb)){
                System.out.println("Ok");
                attnb.setAttendance(inTime,workingDate,employeeId);
            }*/
            /*
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login-data");
            modelAndView.addObject("employee",employeeOptional.get());
            modelAndView.addObject("intime",attendanceOptional.get().getInTime());
            modelAndView.addObject("workingdate",attendanceOptional.get().getWorkingDate());
            return modelAndView;
            */
        /*} else {
            System.out.println("Failed");
        }
        //List<Break> breakList = breakService.getEmployeeByEmployeeId((long) 1);
        //System.out.println(breakList);
        return modelAndView;
    }*/

    public String checkEmployeeIdPresence() {
        if (employeeId == null) {
            return "login";
        }
        return null;
    }

    @RequestMapping(value = "breakstart")
    public String viewBreakStart() {
        checkEmployeeIdPresence();
        LocalDateTime now = LocalDateTime.now();
        String inTime = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        try {
            breakStart = dateFormat.parse(inTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BreakBootstrap brb = new BreakBootstrap(breakRepository);
        brb.setBreak(inTime, currentDate, employeeId);
        return "break-start";
    }

    @RequestMapping(value = "breakend")
    public String viewBreakEnd() {
        checkEmployeeIdPresence();
        LocalDateTime now = LocalDateTime.now();
        String outTime = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        //Long employeeId = (long) 1;
        int status = 0;
        try {
            breakEnd = dateFormat.parse(outTime);
            //System.out.println(breakEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String breakTime = calculateBreak(breakStart, breakEnd);
        jdbcTemplate.update("update break set break_end='" + outTime + "',status='" + status + "',break_time='" + breakTime + "' where employee_id='" + employeeId + "' and status='" + 1 + "'");
        jdbcTemplate.update(
                String.format(
                        Locale.ENGLISH, "update break set break_end='%s',status='%s',break_time='%s' where employee_id='%d' and status='%d'",
                        outTime,
                        status,
                        breakTime,
                        employeeId,
                        1
                )
        );
        calculateTotalBreak();
        //jdbcTemplate.update("update attendance set break_total_time='"+totalbreak+"' where employee_id='"+employeeId+"'");

        return "break-end";
    }

    public String calculateBreak(Date breakStart, Date breakEnd) {
        /*
        int hour = 0, minute = 0, second;

        if(breakEnd.getSeconds()>=breakStart.getSeconds()){
            second = breakEnd.getSeconds() - breakStart.getSeconds();
        }
        else {
            second = breakEnd.getSeconds() + (60 - breakStart.getSeconds());
            minute--;
        }
        if (second >= 60) {
            second-=60;
            minute++;
        }
        if(breakEnd.getMinutes()>=breakStart.getMinutes()){
            minute += breakEnd.getMinutes() - breakStart.getMinutes();
        }
        else {
            minute += breakEnd.getMinutes() + (60 - breakStart.getMinutes());
            hour--;
        }
        if (minute >= 60) {
            minute-=60;
            hour++;
        }
        hour += breakEnd.getHours() - breakStart.getHours();
        */
        /*
        if (breakStart.getMinutes() == d2.getMinutes()) {
            second = d2.getSeconds() - breakStart.getSeconds();
        } else {
            second = d2.getSeconds() + (60 - breakStart.getSeconds());
            minute--;
        }
        if (second >= 60) {
            second-=60;
            minute++;
        }

        if (breakStart.getHours() == d2.getHours()) {
            minute += d2.getMinutes() - breakStart.getMinutes();
        } else {
            minute += d2.getMinutes() + (60 - breakStart.getMinutes());
            hour--;
        }
        if (minute >= 60) {
            minute-=60;
            hour++;
        }

        hour += d2.getHours() - breakStart.getHours();
        Integer.toString(hour) + ":" + Integer.toString(minute) + ":" + Integer.toString(second);
        */

        Long hr, min, sec;
        Long diff = (breakEnd.getTime() - breakStart.getTime()) / 1000;
        hr = diff / 60 / 60;
        min = (diff / 60) % 60;
        sec = (diff % 60 % 60 % 60);

        String breakTime = hr.toString() + ":" + min.toString() + ":" + sec.toString();
        return breakTime;
    }

    @RequestMapping("/showbreaks")
    public String showBreaks(Model model) {
        checkEmployeeIdPresence();

        //model.addAttribute("breaks", breakService.getBreaks(employeeId,currentDate));
        model.addAttribute("breaks", breakService.getBreaks1(employeeId, currentDate));
        //calculateBreak();
        //model.addAttribute("totalBreak", totalbreak);
        return "show-breaks";
    }

    public void calculateTotalBreak() {
        String breakTime;
        Date newBreak;
        Set<Break> breakSet = breakService.getBreaks(employeeId, currentDate);
        Iterator<Break> breakIterator = breakSet.iterator();
        int hr = 0, min = 0, sec = 0;
        while (breakIterator.hasNext()) {
            breakTime = breakIterator.next().getBreakTime();
            try {
                newBreak = dateFormat.parse(breakTime);
                sec += newBreak.getSeconds();
                if (sec >= 60) {
                    sec -= 60;
                    min += 1;
                }
                min += newBreak.getMinutes();
                if (min >= 60) {
                    min -= 60;
                    hr += 1;
                }
                hr += newBreak.getHours();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        totalbreak = hr + ":" + min + ":" + sec;
        //Iterator<Attendance> attendanceIterator = attendanceService.getEmpByEmployeeId(empid).iterator();
        //while (attendanceIterator.hasNext()){
        //if(attendanceIterator.next().getBreakTotaltime().isEmpty()){
        System.out.println(currentDate);
        jdbcTemplate.update("update attendance set break_total_time='" + totalbreak + "' where employee_id='" + employeeId + "' and working_date='" + currentDate + "'");
        //}
        //}
    }

    public int checkLate(Long employeeId) {
        String entry = null;
        int noOfLate = 0;
        int hr, min;
        Date newDate = new Date();

        Set<Attendance> attendanceSet = attendanceService.getEmpByEmployeeId(employeeId);
        Iterator<Attendance> attendanceIterator = attendanceSet.iterator();
        while (attendanceIterator.hasNext()) {
            entry = attendanceIterator.next().getInTime();
            try {
                newDate = dateFormat.parse(entry);
                hr = newDate.getHours();
                min = newDate.getMinutes();
                if ((hr >= 10 && min > 30) || hr > 10) {
                    noOfLate += 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return noOfLate;
    }

    @RequestMapping("/logout")
    public String logOut() {
        Set<Attendance> optionalAttendance = attendanceService.getEmployeeByEmployeeId(employeeId, currentDate);
        String logout = optionalAttendance.iterator().next().getOutTime();//.get().getOutTime();
        if (logout == null) {
            LocalDateTime now = LocalDateTime.now();
            String logoutTime = now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();

            String inTime = optionalAttendance.iterator().next().getInTime();//.get().getInTime();
            String breakTime = optionalAttendance.iterator().next().getBreakTotaltime();//.get().getBreakTotaltime();
            try {
                breakStart = dateFormat.parse(inTime);
                breakEnd = dateFormat.parse(logoutTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String totalTime = calculateBreak(breakStart, breakEnd);
            try {
                breakStart = dateFormat.parse(breakTime);
                breakEnd = dateFormat.parse(totalTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String workingTime = calculateBreak(breakStart, breakEnd);

            jdbcTemplate.update("update attendance set out_time='" + logoutTime + "',total_time='" + totalTime + "',working_time='" + workingTime + "' where employee_id='" + employeeId + "' and working_date='" + currentDate + "'");
        }

        employeeId = null;
        return "login";
    }

    //@Autowired
    //JdbcTemplate jdbc;

    /*
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

     */


    @RequestMapping(value = "/employeeAttendances", method = RequestMethod.GET)
    public String getEmployeesAttendance(Model model) {
        List<Attendance> attendanceList = attendanceRepository.findAll();
        model.addAttribute("attendances", attendanceList);
        return "employee-attendances";
    }

    @RequestMapping(value = "/attendanceSearch", method = RequestMethod.POST)
    public String searchAttendance(Model model,
                                   @ModelAttribute("employeeId") String employeeId,
                                   @ModelAttribute("month") String month) {
        model.addAttribute("employeeId", employeeId);
        //Set<Attendance> attendanceList = attendanceService.getEmpByEmployeeId(Long.parseLong(employeeId));
        //model.addAttribute("attendances",attendanceList);

        String workingDate1 = "__" + month + "_____";
        String workingDate2 = "___" + month + "_____";

        Set<Attendance> attendanceSet = attendanceService.getWorkingDate(Long.parseLong(employeeId), workingDate1, workingDate2);
        model.addAttribute("attendances", attendanceSet);
        return "attendance-search";
    }
}
