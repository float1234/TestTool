import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by dextry on 2017/5/25.
 */
public class CronTriggerTest {

    public static void main(String[] args) {

        SchedulerFactory schedulerfactory=new StdSchedulerFactory();
        Scheduler scheduler=null;
        try{
            scheduler=schedulerfactory.getScheduler();
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity("myJob")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey("myTrigger", "myTriggerGroup"))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                           .withIntervalInMilliseconds(2000)
                            .repeatForever())
                    .startNow()
                    .build();

            scheduler.scheduleJob(job, trigger);
//       启动调度


            scheduler.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
