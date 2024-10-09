package lock.lock8;

/**
 * 线程八锁其一
 * @description:  两个线程分别访问同一个对象的两个普通同步方法。
 *                由于普通方法默认使用的锁对象是this，因此这两个方法会互斥执行。
 * @copyright: @Copyright (c) 2022
 * @company: Aiocloud
 * @author: pany
 * @version: 1.0.0
 * @createTime: 2023-07-04 19:39
 */
public class SynchronizedExampl1 {

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
        SynchronizedExampl1 example = new SynchronizedExampl1();
        //这里锁的this就是example，所以会互斥
        Thread thread1 = new Thread(() -> {
            example.method1();
        });

        Thread thread2 = new Thread(() -> {
            example.method2();
        });

        thread1.start();
        thread2.start();
    }

}

