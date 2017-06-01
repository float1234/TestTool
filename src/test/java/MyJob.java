
import java.util.Date;

import org.quartz.*;

/**
 * Created by dextry on 2017/5/25.
 */
public class MyJob implements Job {
    private static int count;

    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        System.out.println("--------------------------------------------------------------------");

        System.out.println("MyJob start: " + jobContext.getFireTime());

        JobDetail jobDetail = jobContext.getJobDetail();

        System.out.println("Example name is: " + jobDetail.getJobDataMap().getString("example"));

        System.out.println("MyJob end: " + jobContext.getJobRunTime() + ", key: " + jobDetail.getKey());

        System.out.println("MyJob next scheduled time: " + jobContext.getNextFireTime());


        System.out.println("--------------------------------------------------------------------");

        ILatch latch = (ILatch) jobDetail.getJobDataMap().get("latch");
        StudentBean student = (StudentBean) jobDetail.getJobDataMap().get("student");
        System.out.println(student.getAge()+"---"+ student.getName() );


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

