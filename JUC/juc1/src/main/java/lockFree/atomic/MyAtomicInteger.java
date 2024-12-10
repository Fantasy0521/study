package lockFree.atomic;

import sun.misc.Unsafe;

/**
 * 使用Unsafe对象模拟实现AtomicInteger
 */
public class MyAtomicInteger {

    static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();

    private volatile int value;
    private static final long valueOffset;

    static {
        try {
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue(){
        return value;
    }

    public int getAndSet(int newValue){
        while (true){
            int pre = value;
            if(UNSAFE.compareAndSwapInt(this,valueOffset,value,newValue)){
                return pre;
            }
        }
    }

    public int decrementAndGet(int amount){
        while (true){
            int pre = value;
            int next = pre - amount;
            if(UNSAFE.compareAndSwapInt(this,valueOffset,pre,next)){
                return next;
            }
        }
    }

    public static void main(String[] args) {
        MyAtomicInteger myAtomicInteger = new MyAtomicInteger();
        System.out.println(myAtomicInteger.getAndSet(1));
        System.out.println(myAtomicInteger.getValue());
        System.out.println(myAtomicInteger.decrementAndGet(2));
    }


}
