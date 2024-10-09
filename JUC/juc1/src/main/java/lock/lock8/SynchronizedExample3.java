package lock.lock8;

/**
 * 线程八锁其三
 * @description: 一个线程访问对象的普通同步方法，另一个线程访问类的静态同步方法。
 * 由于锁对象不同，因此这两个方法不会互斥执行。
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:52
 */
public class SynchronizedExample3 {
    public synchronized void method1() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 正在执行普通同步方法 method1.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程 " + Thread.currentThread().getName() + " 完成执行普通同步方法 method1.");
    }

    public static synchronized void method2() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 正在执行静态同步方法 method2.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程 " + Thread.currentThread().getName() + " 完成执行静态同步方法 method2.");
    }

    public static void main(String[] args) {
        SynchronizedExample3 example = new SynchronizedExample3();
        //这里thread1是锁this(对象锁)，thread2类锁，不会互斥
        Thread thread1 = new Thread(() -> {
            example.method1();
        });

        Thread thread2 = new Thread(() -> {
            SynchronizedExample3.method2();
        });
        thread1.start();
        thread2.start();
    }
}

