package lock.lock8;

/**
 * 线程八锁其六
 * @description: 对象的静态同步方法和静态代码块之间：同一个类的静态同步方法和静态代码块之间是互斥的，因为它们使用的是类的Class对象的锁
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:52
 */
public class SynchronizedExample6 {
    public static synchronized void synchronizedMethod() {
        // 静态同步方法
        System.out.println("进入静态同步方法");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("退出静态同步方法");
    }

    public static void synchronizedBlock() {
        // 静态同步代码块
        synchronized (SynchronizedExample6.class) {
            System.out.println("进入静态同步代码块");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("退出静态同步代码块");
        }
    }

    public static void main(String[] args) {
        // 创建两个线程，分别调用静态同步方法和静态同步代码块
        //锁类的Class对象，互斥
        Thread thread1 = new Thread(() -> {
            SynchronizedExample6.synchronizedMethod();
        });

        Thread thread2 = new Thread(() -> {
            SynchronizedExample6.synchronizedBlock();
        });

        thread1.start();
        thread2.start();
    }
}


