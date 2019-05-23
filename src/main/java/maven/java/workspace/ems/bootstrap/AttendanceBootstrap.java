package maven.java.workspace.ems.bootstrap;

import maven.java.workspace.ems.model.Attendance;
import maven.java.workspace.ems.repositories.AttendanceRepository;

import java.util.ArrayList;
import java.util.List;

public class AttendanceBootstrap {

    private AttendanceRepository attendanceRepository;

    public AttendanceBootstrap(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void setAttendance(String inTime, String workingDate, Long employeeId){
        List<Attendance> attendance = new ArrayList<>(2);
        Attendance attn = new Attendance();
        attn.setInTime(inTime);
        attn.setWorkingDate(workingDate);
        attn.setEmployeeId(employeeId);
        attendance.add(attn);
        attendanceRepository.saveAll(attendance);
    }
}
