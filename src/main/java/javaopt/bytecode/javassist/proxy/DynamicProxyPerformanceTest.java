package javaopt.bytecode.javassist.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class DynamicProxyPerformanceTest {

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        CountService delegate = new CountServiceImpl();
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Instance: " + time + " ms");

        time = System.currentTimeMillis();
        Class<?> c = Class.forName(CountServiceImpl.class.getName());
        CountService reflection = (CountService) c.newInstance();
        Method method = c.getMethod("count");
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Reflection: " + time + " ms");

        time = System.currentTimeMillis();
        CountService jdkProxy = createJdkDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        CountService javassistProxy = createJavassistDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        CountService javassistBytecodeProxy = createJavassistBytecodeDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Bytecode Proxy: " + time + " ms");

        System.out.println();
        for (int i = 0; i < 3; i++) {
            test(delegate, "Run JDK Instance: ");
            test(reflection, method, "Run JDK Reflection: ");
            test(jdkProxy, "Run JDK Proxy: ");
            test(javassistProxy, "Run JAVAASSIST Proxy: ");
            test(javassistBytecodeProxy, "Run JAVAASSIST Bytecode Proxy: ");
            System.out.println("----------------");
        }
    }

    private static void test(CountService service, Method method, String label)
            throws Exception {
        method.invoke(service); // warm up
        int count = 10000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            method.invoke(service);
        }
        time = System.currentTimeMillis() - time;
        System.out.println(label + time + " ms, "
                + new DecimalFormat().format(count * 1000L / time) + " t/s");
    }

    private static void test(CountService service, String label)
            throws Exception {
        service.count(); // warm up
        int count = 10000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            service.count();
        }
        time = System.currentTimeMillis() - time;
        System.out.println(label + time + " ms, "
                + new DecimalFormat().format(count * 1000L / time) + " t/s");
    }

    private static CountService createJdkDynamicProxy(
            final CountService delegate) {
        CountService jdkProxy = (CountService) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[] { CountService.class }, new JdkHandler(delegate));
        return jdkProxy;
    }

    private static class JdkHandler implements InvocationHandler {
        final Object delegate;

        JdkHandler(Object delegate) {
            this.delegate = delegate;
        }

        public Object invoke(Object object, Method method, Object[] objects)
                throws Throwable {
            return method.invoke(delegate, objects);
        }
    }

    private static CountService createJavassistDynamicProxy(
            final CountService delegate) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[] { CountService.class });
        Class<?> proxyClass = proxyFactory.createClass();
        CountService javassistProxy = (CountService) proxyClass.newInstance();
        ((ProxyObject) javassistProxy).setHandler(new JavaAssitInterceptor(
                delegate));
        return javassistProxy;
    }

    private static class JavaAssitInterceptor implements MethodHandler {
        final Object delegate;

        JavaAssitInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        public Object invoke(Object self, Method m, Method proceed,
                Object[] args) throws Throwable {
            return m.invoke(delegate, args);
        }
    }

    private static CountService createJavassistBytecodeDynamicProxy(
            CountService delegate) throws Exception {
        ClassPool mPool = new ClassPool(true);
        CtClass mCtc = mPool.makeClass(CountService.class.getName()
                + "JavaassistProxy");
        mCtc.addInterface(mPool.get(CountService.class.getName()));
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        mCtc.addField(CtField.make("public " + CountService.class.getName()
                + " delegate;", mCtc));
        mCtc.addMethod(CtNewMethod.make(
                "public int count() { return delegate.count(); }", mCtc));
        Class<?> pc = mCtc.toClass();
        CountService bytecodeProxy = (CountService) pc.newInstance();
        Field field = bytecodeProxy.getClass().getField("delegate");
        field.set(bytecodeProxy, delegate);
        return bytecodeProxy;
    }
}
