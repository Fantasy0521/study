package DebugThread;

/**
 * 看主线程和另外一个线程调用
 * debug需要选择Thread
 */
public class ThreadDebugTest {

    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                method1(10);
            }
        };
        t1.setName("t1");
        t1.start();

        method1(100);
    }

    private static Object method2(){
        Object n = new Object();
        return n;
    }

    private static void method1(Integer n){
        int y = n + 1;
        Object o = method2();
        System.out.println(n);
    }
}
