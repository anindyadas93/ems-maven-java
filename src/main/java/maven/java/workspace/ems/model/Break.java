package maven.java.workspace.ems.model;

import javax.persistence.*;

@Entity
@Table(name = "break")
public class Break {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "break_id")
    private Long id;

    @Column(name = "break_start")
    private String breakStart;

    @Column(name = "break_end")
    private String breakEnd;

    @Column(name="break_time")
    private String breakTime;

    @Column(name = "break_date")
    private String breakDate;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "status")
    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(String breakStart) {
        this.breakStart = breakStart;
    }

    public String getBreakEnd() {
        return breakEnd;
    }

    public void setBreakEnd(String breakEnd) {
        this.breakEnd = breakEnd;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public String getBreakDate() {
        return breakDate;
    }

    public void setBreakDate(String breakDate) {
        this.breakDate = breakDate;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
