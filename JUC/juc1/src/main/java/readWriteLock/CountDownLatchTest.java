package readWriteLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

import static util.JucUtil.sleep;

/**
 * CountDownLatch
 */
@Slf4j(topic = "c.CountDownLatchTest")
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            log.debug("t1 start...");
            sleep(2000);
            countDownLatch.countDown();
            log.debug("t1 end...{}", countDownLatch.getCount());
        }).start();

        new Thread(() -> {
            log.debug("t2 start...");
            sleep(1000);
            countDownLatch.countDown();
            log.debug("t2 end...{}", countDownLatch.getCount());
        }).start();

        new Thread(() -> {
            log.debug("t3 start...");
            sleep(1000);
            countDownLatch.countDown();
            log.debug("t3 end...{}", countDownLatch.getCount());
        }).start();

        //上面每个线程执行结束都会让count-1，等待计数器归零，然后再向下执行
        countDownLatch.await();
        log.debug("main end...");

        //countDownLatch配合线程池使用

    }

}



