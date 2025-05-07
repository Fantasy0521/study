package readWriteLock;


import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock
 */
public class StampedLockTest {

    public static void main(String[] args) {
        StampedLock stampedLock = new StampedLock();
        //读锁 加锁解锁
        long stamp = stampedLock.tryReadLock();
        stampedLock.unlockRead(stamp);			// 类似于 unpark，解指定的锁

        //写锁
        stamp = stampedLock.tryWriteLock();
        stampedLock.unlockWrite(stamp);

        //乐观锁
        stamp = stampedLock.tryOptimisticRead();
        //验戳
        if (!stampedLock.validate(stamp)){
            //失败则锁升级为 读锁
            try {
                stamp = stampedLock.readLock();
            }finally {
                stampedLock.unlock(stamp);
            }
        }
        //验证成功

    }

}
