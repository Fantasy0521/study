package Async;

import lombok.extern.slf4j.Slf4j;


/**
 * 同步异步
 */
@Slf4j(topic = "c.")
public class Sync {

    public static void main(String[] args) {
        //异步
        new Thread(()-> log.info("do thread things")).start();
        log.info("do main things");
    }

}
