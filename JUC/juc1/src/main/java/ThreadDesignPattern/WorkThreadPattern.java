package ThreadDesignPattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步模式-工作线程
 * 使用工作线程处理多个任务
 */
@Slf4j(topic = "c.WorkThreadPattern")
public class WorkThreadPattern {

    public static void main(String[] args) {
        isWorkThreadPattern();
    }

    public static void noWorkThreadPattern() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> {
            log.debug("处理点餐");
            Future<String> f = pool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            log.debug("处理点餐");
            Future<String> f = pool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void isWorkThreadPattern() {
        //不同类型的任务要使用不同线程池来处理
        ExecutorService pool = Executors.newFixedThreadPool(1);
        ExecutorService cookPool = Executors.newFixedThreadPool(1);

        pool.execute(()->{
            log.debug("处理点餐");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        pool.execute(()->{
            log.debug("处理点餐");
            Future<String> f = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜: {}", f.get());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    private static String cooking() {
        return "MENU.get";
    }


}
