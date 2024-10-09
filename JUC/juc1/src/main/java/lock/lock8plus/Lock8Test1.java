package lock.lock8plus;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "c.Main")
public class Lock8Test1 {

    public static void main(String[] args) {
        Number number = new Number();
        new Thread(() -> {
            log.debug("Thread1 begin");
            number.a();
        }).start();
        new Thread(() -> {
            log.debug("Thread2 begin");
            number.b();
        }).start();
        new Thread(() -> {
            log.debug("Thread3 begin");
            number.c();
        }).start();

    }

}

/**
 * 结果分析
 * 3 1s后 1 2    Thread3先抢到CPU执行权 随后Thread1，或者是Thread1先抢 ，但是要sleep，Thread2要等待Thread1锁释放，所以Thread3就先执行了
 * 2 3 1s后 1    Thread2先抢
 * 3 2 1s后 1    Thread3先抢 Thread2随后
 */

@Slf4j(topic = "c.Number")
class Number {

    public synchronized void a() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }

    public void c() {
        log.debug("3");
    }

    public void test(){
        Hashtable<String, Object> hashtable = new Hashtable<>();
        if (hashtable.get("key") == null){
            hashtable.put("key",1);
        }

        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put();
//        new ConcurrentHashMap<>().put();
        new String();
    }


}
