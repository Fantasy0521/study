//package ThreadSafe;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class MyAspect {
//
//    //线程不安全，SpringBoot中MyAspect默认为单例只有一份，存在多个线程修改start成员变量的情况
//    //解决办法，使用环绕通知并且将start作为局部变量
////    private long start = 0L;
////
////    @Before("execution(* *(..))")
////    public void before(){
////        start = System.nanoTime();
////    }
////
////    @After("execution(* *(..))")
////    public void after(){
////        long end = System.nanoTime();
////        System.out.println("cost time:" + (end - start));
////    }
//
//    @Around("execution(* *(..))")
//    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        //前置方法
//        long start = System.nanoTime();
//        try {
//            //执行目标方法
//            Object result = proceedingJoinPoint.proceed();
//            return result;
//        }finally {
//            //后置方法
//            long end = System.nanoTime();
//            System.out.println("cost time:" + (end - start));
//        }
//    }
//
//}
