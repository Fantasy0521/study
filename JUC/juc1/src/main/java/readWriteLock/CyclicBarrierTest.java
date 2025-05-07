package readWriteLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.JucUtil.sleep;

/**
 * 和CountDownLatch一样，CyclicBarrier也是用来做线程间通信的，但是可以重用
 */
@Slf4j(topic = "c.CyclicBarrierTest")
public class CyclicBarrierTest {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2,() -> {
            log.debug("task1, task2 finish...");
        });

        threadPool.submit(() -> {
            log.debug("task1 begin...");
            sleep(1000);
            try {
                barrier.await();//2 - 1 = 1 没到0，栅栏拦截不让结束
                log.debug("task1 end...");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        threadPool.submit(() -> {
            log.debug("task2 begin...");
            sleep(1000);
            try {
                barrier.await();//1 - 1 = 0 到0则向下执行
                log.debug("task2 end...");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        threadPool.shutdown();
    }

}
