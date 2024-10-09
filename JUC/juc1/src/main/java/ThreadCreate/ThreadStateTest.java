package ThreadCreate;

public class ThreadStateTest {

    public static void main(String[] args) {
        Thread t4 = new Thread(()->{
            synchronized (ThreadStateTest.class){
                try {
                    Thread.sleep(10000);//获得了锁，并进入TIME_WAITING即时等待
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t4.start();

        Thread t5 = new Thread(()->{
            synchronized (ThreadStateTest.class){
                try {
                    Thread.sleep(10000);//没获得锁，进入BLOCKED阻塞状态
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t5.start();

        System.out.println(t4.getState());
        System.out.println(t5.getState());

    }



}
