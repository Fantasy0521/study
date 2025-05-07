package ThreadDesignPattern;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步模式-工作线程
 * 使用工作线程处理多个任务
 */
@Slf4j(topic = "c.WorkThreadPattern")
public class WorkThreadPattern {

    public static void main(String[] args) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01");
        System.out.println(simpleDateFormat.parse("2024-10-01").getMonth());
        System.out.println(simpleDateFormat.parse("2024-01-01").getMonth());
        System.out.println(simpleDateFormat.parse("2024-12-01").getMonth());
        System.out.println(simpleDateFormat.format(new Date()));
//        isWorkThreadPattern();
    }

    /**
     * input 2024-10-01 output 2024年10-11
     * input 2024-12-01 output 2024年12-2025年1
     * @param twoMonthsAgoFirstDay
     * @return
     */
    public static String getSendDate(Date twoMonthsAgoFirstDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM");
        String format = sdf.format(twoMonthsAgoFirstDay);
        String[] split = format.split("年");
        String month = split[1];
        int nextMonth = Integer.parseInt(month) + 1;
        if (nextMonth > 12){
            int nextYear = Integer.parseInt(split[0]) + 1;
            return format + "-" + nextYear + "年" + 1;
        }
        return format + "-" + nextMonth;
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
