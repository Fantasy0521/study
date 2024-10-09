package ThreadSafe;

import lombok.Data;

import javax.management.monitor.Monitor;

/**
 * 转账问题
 */
public class Transfor {

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account();
        a.setMoney(1000);
        Account b = new Account();
        b.setMoney(1000);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, 1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, 1);
            }
        });
        thread1.start();
        thread2.start();
        //主线程等待其他线程执行完
        thread2.join();
        thread1.join();
        System.out.println(a.getMoney());
        System.out.println(b.getMoney());
    }





}

@Data
class Account{
    private int money;

    public void transfer(Account target,int amount){
//        synchronized (this){//这样锁this只能保证this当前调用对象安全，无法保证另一个对象target对象安全
        synchronized (Account.class){//锁住整个类对象，可以解决这个问题
            if (this.money >= amount){
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }

}
