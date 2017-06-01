package thread;

/**
 * Created by dextry on 2017/5/26.
 */
public class testmain {
    public static void main(String[] args) {
        MyThread myThread1 = new MyThread();
        MyThread myThread2 = new MyThread();
        myThread1.start();
        myThread2.start();
    }
}
