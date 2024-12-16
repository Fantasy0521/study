package ThreadPool2;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 */
@Slf4j(topic = "c.MyThreadPool")
public class MyThreadPool {

    public static void main(String[] args) {
//        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.SECONDS, 1,new ToWaitPolicy());
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1,
            ((queue, task) -> {
                //1 死等
//                queue.put(task);
                //2 带超时等待
//                queue.offer(task, 1500, TimeUnit.MILLISECONDS);
                //3 放弃任务执行
//                log.debug("放弃{}", task);
                //4 抛出异常
//                throw new RuntimeException("队列已满,任务执行失败"+task);
                //5 让调用着自己执行任务
//                task.run();
            })
        );
        for (int i = 0; i < 3; i++) {
            int i1 = i;
            threadPool.execute(()->{
                //线程池的线程可能已满，主线程会阻塞一直等待，需要给主线程制定一个拒绝策略供主线程选择
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", i1);
            });
        }
    }


}

@FunctionalInterface
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue,T task);
}

/**
 * 死等策略
 */
class ToWaitPolicy implements RejectPolicy<Runnable>{
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        queue.put(task);
    }
}





@Slf4j(topic = "c.ThreadPool")
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;

    //工作线程集合
    private HashSet<Worker> workers = new HashSet<>();

    //拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    //核心线程数
    private int coreSize;

    //获取任务的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit,int queueSize,RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<Runnable>(queueSize);
        this.rejectPolicy = rejectPolicy;
    }

    //执行任务
    public void execute(Runnable task){
        //当任务数没有超过核心数时，直接交给woker对象执行
        //如果超过了，加入任务队列中
        synchronized (workers){
            if(workers.size() < coreSize){
                Worker worker = new Worker(task);
                log.debug("新增 worker{},task{}" , worker ,task);
                workers.add(worker);
                worker.start();
            }else {
//                taskQueue.put(task);
//                boolean offerResult = taskQueue.offer(task, timeout, timeUnit);
                //1 死等
                //2 带超时等待
                //3 放弃任务执行
                //4 抛出异常
                //5 让调用着自己执行任务
                //这里使用策略模式：策略模式将策略封装到接口中，由调用者选择哪种策略并传入
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{

        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务
            //1 当task不为空，执行任务
            //2 当task执行完毕，接着从任务队列中获取任务并执行
            while (task != null || (task = taskQueue.poll(timeout,timeUnit)) != null){
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            //所有任务都执行完毕，线程池中的worker线程也移除
            synchronized (workers){
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }

}

/**
 * 任务队列，这里使用泛型是为了满足多线程实现方式 如Thread Runnable
 * @param <T>
 */
@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T>{
    //1 任务队列 双向链表
    private Deque<T> queue = new ArrayDeque<>();

    //2 锁 可能有多个线程在线程链表中添加元素
    private ReentrantLock lock = new ReentrantLock();

    //3 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();


    //4 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    //take优化 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try {
            //将timeout统一转化为纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()){
                try {
                    //防止虚假唤醒 需要将nanos重新赋值，返回的是剩余的时间
                    if (nanos <= 0){
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            fullWaitSet.signalAll();
            return first;
        }finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T take(){
        lock.lock();
        try {
            while (queue.isEmpty()){
                try {
                    //队列没有元素，进入消费者条件变量中等待。当有元素时会被唤醒
//                    fullWaitSet.signalAll();
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            fullWaitSet.signalAll();
            return first;
        }finally {
            lock.unlock();
        }
    }

    //阻塞添加
    public void put(T task){
        lock.lock();
        try {
            while (queue.size() == capacity){
                log.debug("等待加入任务队列中 put wait join task{}", task);
                fullWaitSet.await();
            }
            log.debug("加入任务队列中 offer task{}", task);
            queue.addLast(task);
            emptyWaitSet.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    //带超时时间的阻塞添加
    public boolean offer(T task,long timeout,TimeUnit unit){
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.size() == capacity){
                log.debug("等待加入任务队列中 offer wait join task{}", task);
                if (nanos <= 0){
                    return false;
                }
                nanos = fullWaitSet.awaitNanos(nanos);
            }
            log.debug("加入任务队列中 offer task{}", task);
            queue.addLast(task);
            emptyWaitSet.signalAll();
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    // 获取大小
    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }


    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            //判断队列是否已满
            if (queue.size() == capacity) {
                log.debug("队列已满，拒绝策略：{}", rejectPolicy);
                rejectPolicy.reject(this, task);
            } else {
                log.debug("加入任务队列：{}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
