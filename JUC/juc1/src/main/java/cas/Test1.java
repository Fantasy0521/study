package cas;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class Test1 {

    private AtomicInteger balance;

    public void withDraw(Integer amount){

        while (true){
            // 获取余额的最新值
            int prev = balance.get();
            // 要修改的余额
            int next = prev - amount;
            // 真正修改
            if (balance.compareAndSet(prev, next)){
                break;
            }

        }
        balance.updateAndGet(i -> i * 10);
        myCompareAndSet(balance,i -> i * 10);

    }

    public static void myCompareAndSet(AtomicInteger i, IntUnaryOperator operator){
        while (true){
            int prev = i.get();
//            int next = prev + 1;//这里可以是加减乘除任意操作，可以使用lambda函数
            int next = operator.applyAsInt(prev);//根据传入的函数来修改，返回结果
            if (i.compareAndSet(prev, next)){
                break;
            }
        }
    }

    public static void main(String[] args) {
        AtomicInteger ac = new AtomicInteger(10);
        myCompareAndSet(ac,i -> i * 10);
        System.out.println(ac.get());
    }


}
