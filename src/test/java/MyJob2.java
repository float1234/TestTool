import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by dextry on 2017/5/26.
 */
public class MyJob2 implements Job {

    private static int count;



    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        System.out.println("——————————————————————————————");



        JobDetail jobDetail = jobContext.getJobDetail();

        System.out.println(new Date());

        System.out.println("——————————————————————————————");

        ILatch latch = (ILatch) jobDetail.getJobDataMap().get("latch");

        latch.countDown();

        count++;

        System.out.println("Job count " + count);

        if (count == 2) {

            throw new RuntimeException("Some RuntimeException!");
        }
        if (count == 4) {

            throw new JobExecutionException("Some JobExecutionException!");
        }
    }
}
