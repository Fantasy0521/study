package ThreadCreate;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.ThreadMethodsTest")
public class ThreadMethodsTest {

    public static void main(String[] args) throws InterruptedException {
        synchronized (Thread.currentThread()){
            Thread.currentThread().wait(1,1);
        }
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

    static int r1 = 0;
    static int r2 = 0;

    private static void m4() throws InterruptedException {
        Thread thread1 = new Thread(()->{
            sleep(1);
            r1 = 10;
        }) ;
        Thread thread2 = new Thread(()->{
            sleep(2);
            r2 = 20;
        }) ;
        thread1.start();
        thread2.start();
        long start = System.currentTimeMillis();
        log.debug("t1 join begin");
        thread1.join();
        log.debug("t1 join end");
        log.debug("t2 join begin");
        thread2.join();
        log.debug("t2 join end");
        long end = System.currentTimeMillis();
        System.out.println(end - start);//2000 2秒，因为thread1.join()等待一秒时thread2也运行了一秒
    }

    private static void m5() throws InterruptedException {
        //打断阻塞线程
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        //打断标记
        System.out.println(thread.isInterrupted());//false

        //打断正常线程
        Thread thread2 = new Thread(() -> {
            while (true){

            }
        });
        thread2.start();
        Thread.sleep(1000);
        //执行完打断后线程2还在运行，主线程只是给出打断提醒，是否打断需要线程2自己决定
        thread2.interrupt();
        //打断标记
        System.out.println(thread2.isInterrupted());//true

        //打断正常线程
        Thread thread3 = new Thread(() -> {
            while (true){
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("被打断了");
                    break;
                }
            }
        });
        thread2.start();
        Thread.sleep(1000);
        thread2.interrupt();
        //打断标记
        System.out.println(thread2.isInterrupted());//true

    }

    private static void m6() throws InterruptedException {
        //打断park线程，如果打断标记为true，park会失效
        Thread t1 = new Thread(() -> {
            System.out.println("park...");
            LockSupport.park();
            System.out.println("unpark...");
            System.out.println("打断状态：" + Thread.currentThread().isInterrupted());//打断状态：true
            LockSupport.park();//失效
        }, "t1");
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }

    private static void sleep(int i){
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




}
