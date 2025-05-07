package readWriteLock;

import lombok.extern.slf4j.Slf4j;
import util.JucUtil;

import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 读写锁
 */
public class ReentrantReadWriteLockTest {

    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();
        new Thread(() -> {
            dataContainer.write(1);
        },"t1").start();

        JucUtil.sleep(100);
        new Thread(() -> {
            dataContainer.write(2);
        },"t2").start();
    }

}

@Slf4j(topic = "c.DataContainer")
class DataContainer{

    private Object data;

    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = rw.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = rw.writeLock();

    public Object read(){
        log.debug("获取读锁...");
        readLock.lock();
        try {
            log.debug("读取数据...");
            JucUtil.sleep(1000);
            return data;
        }finally {
            log.debug("释放读锁...");
            readLock.unlock();
        }
    }
    public void write(Object data){
        log.debug("获取写锁...");
        writeLock.lock();
        try {
            log.debug("写入数据...");
            this.data = data;
        }finally {
            log.debug("释放写锁...");
            writeLock.unlock();
        }
    }

}