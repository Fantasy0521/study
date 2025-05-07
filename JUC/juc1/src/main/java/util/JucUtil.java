package util;

public class JucUtil {

    public static void sleep(Integer time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
