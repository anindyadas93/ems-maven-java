package maven.java.workspace.ems.repositories;

import maven.java.workspace.ems.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Set<Attendance> findByEmployeeIdAndWorkingDate(Long employeeId,String workingDate);

    Set<Attendance> findByEmployeeId(Long employeeId);

    Set<Attendance> findByEmployeeIdAndWorkingDateLikeOrWorkingDateLike(Long employeeId, String workingDate1, String workingDate2);

}
