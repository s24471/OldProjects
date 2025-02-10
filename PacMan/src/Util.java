import java.util.concurrent.TimeUnit;

public abstract class Util {
    public static void sleepMs(long ms){
        try {
            while (Model.stop) TimeUnit.MILLISECONDS.sleep(10);
            TimeUnit.MILLISECONDS.sleep(ms);
            while (Model.stop) TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
