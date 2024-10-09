package lock.lock8;

/**
 * @description: 不会互斥执行，因为它们使用的是不同的锁对象，一个是对象实例的锁，一个是类的Class对象的锁。
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:52
 */
public class SynchronizedExample8 {
    public synchronized void synchronizedMethod() {
        // 普通同步方法
        System.out.println("进入普通同步方法");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("退出普通同步方法");
    }

    public static void synchronizedBlock() {
        // 静态同步代码块
        synchronized (SynchronizedExample8.class) {
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
        SynchronizedExample8 example = new SynchronizedExample8();
        // 创建两个线程，分别调用普通同步方法和静态同步代码块
        //这里thread1是锁this(对象锁)，thread2类的Class对象，不会互斥
        Thread thread1 = new Thread(() -> {
            example.synchronizedMethod();
        });

        Thread thread2 = new Thread(() -> {
            SynchronizedExample8.synchronizedBlock();
        });

        thread1.start();
        thread2.start();
    }
}


