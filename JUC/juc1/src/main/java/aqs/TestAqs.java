package aqs;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;


/**
 * 自定义aqs Lock锁
 */
@Slf4j(topic = "c.TestAqs")
public class TestAqs {

    public static void main(String[] args) {
        MyLock lock = new MyLock();
        new Thread(() -> {
            lock.lock();
            //不可重入，再次加锁会阻塞
//            lock.lock();
            try {
                log.debug("locking...");
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.debug("locking...");
            }finally {
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();
    }

}

//自定义不可重入锁
class MyLock implements Lock{

    //独占锁
    class MySync extends AbstractQueuedSynchronizer{

        //尝试获取锁
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)){//使用cas设置state，成功则设置独占锁
                //加上锁了
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            //这里的顺序需要注意，由于state为volatile修饰，在它之前的指令可以被其他线程可见（写屏障）
            // 而exclusiveOwnerThread非volatile修饰，要让exclusiveOwnerThread的修改对其他线程可见就要写在setState前
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        //判断是否持有独占锁
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }


    private MySync sync = new MySync();

    //加锁，不成功则加入等待队列，并等待
    @Override
    public void lock() {
        //尝试获取锁，获取失败加入等待队列，失败则自我打断
        sync.acquire(1);
    }

    //加锁，可打断
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //尝试加锁（一次）
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }


    //尝试加锁，带超时
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    //解锁
    @Override
    public void unlock() {
        //释放锁并唤醒等待队列中的线程
        sync.release(1);
    }

    //创建条件变量
    @Override
    public Condition newCondition(){
        return sync.newCondition();
    }
}
