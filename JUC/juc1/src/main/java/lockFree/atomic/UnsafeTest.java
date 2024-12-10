package lockFree.atomic;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author fantasy0521
 * unsafe对象提供了非常底层的 操作内存，线程的方法，Unsafe对象不能直接调用，只能通过反射获取
 * Unsafe不是代表线程不安全，是指它的操作都偏向底层不建议开发者使用
 */
public class UnsafeTest {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //Unsafe类中的theUnsafe属性是私有的，只能通过反射获取，不能直接访问
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        //私有属性需要设置为可访问才能get到
        theUnsafe.setAccessible(true);
        //get方法获取Unsafe对象，因为是静态属性，所以参数为null
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        System.out.println(unsafe);
        //unsafe cas相关方法

        //1 获取指定域的偏移地址
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long uidOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("uid"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        System.out.println(idOffset);
        System.out.println(uidOffset);
        System.out.println(nameOffset);

        Teacher t = new Teacher();
        //2 执行cas操作
        unsafe.compareAndSwapInt(t, idOffset, 0, 1);
        unsafe.compareAndSwapObject(t, nameOffset, null, "张三");

        //3 验证
        System.out.println(t);
    }


}

class Teacher{
    volatile private String name;
    volatile private int id;
    volatile private long uid;

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
