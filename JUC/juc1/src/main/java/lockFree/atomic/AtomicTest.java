package lockFree.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 无锁编程
 * Atomic 原子的
 */
public class AtomicTest {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        //以原子的方式加1并返回新值
        atomicInteger.addAndGet(1);
        atomicInteger.getAndSet(1);
        System.out.println(atomicInteger.get());
//        atomicInteger.compareAndSet(1,2);
    }

}
