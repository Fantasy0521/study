package lock.lock8;

/**
 * @description: 两个线程分别访问同一个类的两个静态同步方法。由于静态方法默认使用的锁对象是类的Class对象，因此这两个方法会互斥执行。
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:52
 */
public class SynchronizedExample7 {
    public static synchronized void method1() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 正在执行静态同步方法 method1.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程 " + Thread.currentThread().getName() + " 完成执行静态同步方法 method1.");
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
        //锁类的Class对象，互斥
        Thread thread1 = new Thread(() -> {
            SynchronizedExample7.method1();
        });
        Thread thread2 = new Thread(() -> {
            SynchronizedExample7.method2();
        });
        thread1.start();
        thread2.start();
    }
}




