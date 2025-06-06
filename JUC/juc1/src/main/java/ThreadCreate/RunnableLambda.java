package ThreadCreate;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.RunnableLambda")
public class RunnableLambda {


    public static void main(String[] args) {
        Runnable r = () -> log.debug("running");

        Thread t = new Thread(r,"t1");
        t.start();

    }


}
