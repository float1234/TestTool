package jupiter.simple_test.entry;

import jupiter.simple_test.ReportTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dextry on 2017/5/24.
 */
public class Main {
    public static void main(String[] args) {
        ReportTask reportTask = new ReportTask();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
         service.scheduleAtFixedRate(reportTask,1, 3600*24,TimeUnit.SECONDS);
    }
}
