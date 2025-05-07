package ThreadSafe;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 */
@Slf4j(topic = "c.ConcurrentHashMapTest")
public class ConcurrentHashMapTest {

    public static void main(String[] args) {
//        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
//        List<String> list = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
//        new Thread(() -> {
//            for (String s : list) {
//                Integer integer = map.get(s);
//                int newValue = integer == null ? 1 : integer + 1;
//                map.put(s, newValue);
//            }
//        });

        ConcurrentHashMap<String,LongAdder> map = new ConcurrentHashMap<>();
        List<String> list = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
        new Thread(() -> {
            for (String s : list) {
                //如果缺少一个key，则生成一个value 然后将key value放入map
                //这里需要加一 需要额外使用累加器才能保证原子性
                LongAdder value = map.computeIfAbsent(s, (key) -> new LongAdder());
                //因为每次返回的都是之前的累加器，所以value可以成功累加
                value.increment();
            }
        });

        //原理

    }

}
