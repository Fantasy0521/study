package lock.lock8;

/**
 * 线程八锁其五
 * @description: 同一个对象的普通同步方法和普通代码块之间是互斥的，因为它们使用的是同一个对象实例的锁
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:52
 */
public class SynchronizedExample5 {
    public synchronized void synchronizedMethod() {
        // 同步方法
        System.out.println("进入同步方法");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("退出同步方法");
    }

    public void synchronizedBlock() {
        // 同步代码块
        synchronized (this) {
            System.out.println("进入同步代码块");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("退出同步代码块");
        }
    }

    public static void main(String[] args) {
        SynchronizedExample5 example = new SynchronizedExample5();
        //这里锁同一个this，互斥
        // 创建两个线程，分别调用同步方法和同步代码块
        Thread thread1 = new Thread(() -> {
            example.synchronizedMethod();
        });

        Thread thread2 = new Thread(() -> {
            example.synchronizedBlock();
        });

        thread1.start();
        thread2.start();
    }
}

