package lock.lock8;

/**
 * 线程八锁其四
 * @description: 两个线程分别访问两个不同对象的同步方法。由于锁对象不同，因此这两个方法不会互斥执行。
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:49
 */
public class SynchronizedExample4 {

    public synchronized void method1() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 正在执行 method1.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程 " + Thread.currentThread().getName() + " 完成执行 method1.");
    }

    public synchronized void method2() {
        System.out.println("线程 " + Thread.currentThread().getName() + " 正在执行 method2.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程 " + Thread.currentThread().getName() + " 完成执行 method2.");
    }

    public static void main(String[] args) {
        SynchronizedExample4 example1 = new SynchronizedExample4();
        SynchronizedExample4 example2 = new SynchronizedExample4();
        //这里锁的this分别为example1何example2，不会互斥
        Thread thread1 = new Thread(() -> {
            example1.method1();
        });
        Thread thread2 = new Thread(() -> {
            example2.method2();
        });
        thread1.start();
        thread2.start();
    }
}

