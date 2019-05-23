package maven.java.workspace.ems.services;

import maven.java.workspace.ems.model.Attendance;
import maven.java.workspace.ems.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    /*
    public Optional<Attendance> getEmployeeByEmployeeId(Long employeeId,String workingDate){
        return attendanceRepository.findByEmployeeIdAndWorkingDate(employeeId, workingDate);
    }
    */
    public Set<Attendance> getEmployeeByEmployeeId(Long employeeId, String workingDate){
        return attendanceRepository.findByEmployeeIdAndWorkingDate(employeeId,workingDate);
    }

    public Set<Attendance> getEmpByEmployeeId(Long employeeId){
        return attendanceRepository.findByEmployeeId(employeeId);
    }

    public Set<Attendance> getWorkingDate(Long employeeId, String workingDate1, String workingDate2){
        return attendanceRepository.findByEmployeeIdAndWorkingDateLikeOrWorkingDateLike(employeeId, workingDate1, workingDate2);
    }

}
