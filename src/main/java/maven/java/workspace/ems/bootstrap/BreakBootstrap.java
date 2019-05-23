package maven.java.workspace.ems.bootstrap;

import maven.java.workspace.ems.model.Break;
import maven.java.workspace.ems.repositories.BreakRepository;

import java.util.ArrayList;
import java.util.List;

public class BreakBootstrap {
    private BreakRepository breakRepository;

    public BreakBootstrap(BreakRepository breakRepository) {
        this.breakRepository = breakRepository;
    }

    public void setBreak(String breakStart,String currentDate,Long employeeId){
        int status=1;
        List<Break> breaks = new ArrayList<>(2);
        Break br = new Break();
        br.setBreakStart(breakStart);
        br.setBreakDate(currentDate);
        br.setEmployeeId(employeeId);
        br.setStatus(status);
        breaks.add(br);
        breakRepository.saveAll(breaks);
    }
}
