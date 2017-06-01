import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Created by dextry on 2017/5/25.
 */
public class CronTriggerTest2 {
    public static void main(String[] args) {
        //通过SchedulerFactory获得一个调度器
        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            //获取调度器实例
            scheduler = schedulerfactory.getScheduler();

            // 指明job的名称，所在组的名称，以及绑定job类    创建jobDetail实例，绑定Job实现类
            JobDetailImpl jobDetail = new JobDetailImpl("job1", "jgroup1", MyJob.class);
            //定义调度出发规则 每隔2秒执行一次
            CronTriggerImpl cornTrigger = new CronTriggerImpl("jobname", "jobgroup");

            //括号里面的*号依此为秒，分，时，天，周，月，年
            //里面有具体的使用规则，非常灵活
            cornTrigger.setCronExpression("0/2 * * * * ? *");

            //把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, cornTrigger);
            //启动调度

            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
