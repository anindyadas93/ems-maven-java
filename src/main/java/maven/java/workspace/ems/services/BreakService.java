package maven.java.workspace.ems.services;

import maven.java.workspace.ems.model.Break;
import maven.java.workspace.ems.repositories.BreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BreakService {
    @Autowired
    private BreakRepository breakRepository;

    /*
    public List<Break> getBreaksByEmployeeId(Long employeeId){
        return breakRepository.findByEmployeeId(employeeId);
    }
    */

    public Set<Break> getBreaks(Long employeeId, String currentDate){
        Set<Break> breakSet = new HashSet<>();
        //breakRepository.findAll().iterator().forEachRemaining(breakSet::add);
        breakRepository.findByEmployeeIdAndBreakDate(employeeId,currentDate).iterator().forEachRemaining(breakSet::add);
        return breakSet;
    }

    public List<Break> getBreaks1(Long employeeId, String currentDate){
        List<Break> breakList = new ArrayList<>();
        breakRepository.findByEmployeeIdAndBreakDate(employeeId,currentDate).iterator().forEachRemaining(breakList::add);
        return breakList;
    }
}
