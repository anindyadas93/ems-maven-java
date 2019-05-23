package maven.java.workspace.ems.model;

import javax.persistence.*;
import javax.swing.text.Style;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @Column(name = "in_time")
    private String inTime;

    @Column(name = "break_in")
    private String breakIn;

    @Column(name = "break_out")
    private String breakOut;

    @Column(name = "break_total_time")
    private String breakTotaltime;

    @Column(name = "out_time")
    private String outTime;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "working_time")
    private String workingTime;

    @Column(name = "working_date")
    private String workingDate;

    @Column(name = "employee_id")
    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getBreakIn() {
        return breakIn;
    }

    public void setBreakIn(String breakIn) {
        this.breakIn = breakIn;
    }

    public String getBreakOut() {
        return breakOut;
    }

    public void setBreakOut(String breakOut) {
        this.breakOut = breakOut;
    }

    public String getBreakTotaltime() {
        return breakTotaltime;
    }

    public void setBreakTotaltime(String breakTotaltime) {
        this.breakTotaltime = breakTotaltime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
