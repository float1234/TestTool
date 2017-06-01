package jupiter.simple_test;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by dextry on 2017/5/24.
 */
public class ReportTask extends TimerTask {
    @Override
    public void run() {
        ReadConfig readConfig = new ReadConfig();
        try {
            readConfig.countAll();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
