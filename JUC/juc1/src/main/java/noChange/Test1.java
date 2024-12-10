package noChange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * 不可变类型
 */
public class Test1 {

    public static void main(String[] args) {
//        m1();
        m2();
    }

    //可变类 线程不安全
    public static void m1(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    System.out.println(sdf.parse("2024-12-05"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    //使用不可变类改进
    public static void m2(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                System.out.println(dtf.parse("2024-12-05"));
            }).start();
        }
    }

}
