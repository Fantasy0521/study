package lock.synchronizedLock;


public class SynchronizedTest {

//    static int counter = 0;
    //static修饰，则元素是属于类本身的，不属于对象  ，与类一起加载一次，只有一个
//    static final Object room = new Object();

    public static void main(String[] args) throws InterruptedException {
        Room room1 = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room1) {
                    room1.increment();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room1) {
                    room1.decrement();
                }
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(room1.getCounter());
    }

}

//面相对象进行优化
//Room对象作为锁
class Room{
    public int counter = 0;

    public void increment(){
        //这里this只会由Room对象来调用，this为调用这个方法的room对象
        synchronized (this){
            counter ++;
        }
    }

    public void decrement(){
        synchronized (this){
            counter --;
        }
    }

    public int getCounter(){
        synchronized (this){
            return counter;
        }
    }

    /**
     * synchronized写在方法上也相当于锁this
     * @return
     */
//    public synchronized int getCounter(){
//        return counter;
//    }


}
