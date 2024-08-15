package ThreadCreate;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.ThreadMethodsTest")
public class ThreadMethodsTest {

    public static void main(String[] args) {
        m3();
    }

    private static void m1() {
        Thread thread = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
            }
        };
        //获取线程状态 getState
        System.out.println(thread.getState());
        thread.start();
        //不能再次调用start
//        thread.start();
        System.out.println(thread.getState());
    }

    private static void m2() {
        //sleep yield
        Thread thread = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //获取线程状态 getState
        thread.start();
        log.debug("t1 {}", thread.getState());//RUNNABLE
        try {
            //sleep写在哪个线程就是让哪个线程睡眠
//            Thread.sleep(500);
            //建议使用TimeUnit
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("t1 {}", thread.getState());//TIMED_WAITING


        //interrupt打断，可以打断其他线程的休眠
        thread.interrupt();
        log.debug("t1 {}", thread.getState());//TIMED_WAITING,不一定立马Runnable

        //yield 让出CPU执行权 依赖于操作系统任务调度器 可能存在让不出去的情况(没有其他线程可运行，操作系统仍会吧使用权交给当前线程)
        Thread.yield();
    }

    static int r = 0;

    private static void m3(){
        //join
        Thread thread = new Thread("t1") {
            @Override
            public void run() {
                r = 10;
            }
        };
        thread.start();
        System.out.println(r);//0
        //如何让主线程获取到其他线程最终执行结果然后打印
        //join 主线程等待thread线程执行结束，阻塞在join这里
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(r);//10
    }


}
