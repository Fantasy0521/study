package ThreadPool2.jdk;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
public class Test {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        Future<Integer> submit1 = pool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return 100 * 100;
            }
        });

        Future<Integer> submit2 = pool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return 300 * 300;
            }
        });

        try {
            int r1 = submit1.get();
            int r2 = submit2.get();
            System.out.println(r1 + r2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
