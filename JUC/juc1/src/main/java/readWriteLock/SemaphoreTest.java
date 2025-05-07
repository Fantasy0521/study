package readWriteLock;

import lombok.extern.slf4j.Slf4j;
import util.JucUtil;

import java.util.concurrent.Semaphore;

/**
 * Semaphore
 * Semaphore（信号量）用来限制能同时访问共享资源的线程上限，非重入锁
 */
@Slf4j(topic = "c.SemaphoreTest")
public class SemaphoreTest {

    public static void main(String[] args) {
        //1 创建Semaphore对象
        Semaphore semaphore = new Semaphore(3);

        //2 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    JucUtil.sleep(1000);
                    log.debug("end...");
                } finally {
                    semaphore.release();
                }
            }).start();
        }

    }

}
