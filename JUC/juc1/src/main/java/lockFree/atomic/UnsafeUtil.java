package lockFree.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe工具类
 */
public class UnsafeUtil {

    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Unsafe getUnsafe(){
        return unsafe;
    }

}
