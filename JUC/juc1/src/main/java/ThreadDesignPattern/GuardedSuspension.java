package ThreadDesignPattern;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 保护性暂停模式 : 用于一个线程等待另一个线程的执行结果
 */
@Slf4j(topic = "c.GuardedSuspension")
public class GuardedSuspension {

    //线程1等待线程2的下载结果
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            log.debug("等待结果");
            List<String> result = (List<String>)guardedObject.get(2000l);
            log.debug("结果大小{}",result);
        },"t1").start();

        new Thread(()->{
            log.debug("执行下载");
            try {
                List<String> download = Downloader.download();
                log.debug("执行完成");
                guardedObject.complete(download);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },"t2").start();

    }

}


/**
 * 用于保存一个线程的执行结果
 */
class GuardedObject {

    //结果
    private Object response;

    //获取结果的方法
    public Object get(Long timeout){
        synchronized (this){
            //优化1 超时取消等待
            // 记录等待开始时间
            long begin = System.currentTimeMillis();
            // 记录经历时间
            long parseTime = 0;
            while (response == null){
                long waitTime = timeout - parseTime;
                if (waitTime <= 0){
                    break;
                }
                //还没有结果，进行等待
                try {
                    this.wait(waitTime);//不能直接写timeout会出现虚假唤醒
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                parseTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response){
        synchronized (this){
            //给结果成员变量赋值
            this.response = response;
            //有值了就唤醒其他线程
            this.notifyAll();
        }
    }

}
