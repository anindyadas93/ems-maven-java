package maven.java.workspace.ems.repositories;

import maven.java.workspace.ems.model.Break;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BreakRepository extends JpaRepository<Break, Long> {
    //Set<Break> findByEmployeeId(Long employeeId);
    Set<Break> findByEmployeeIdAndBreakDate(Long employeeId,String currentDate);

}
