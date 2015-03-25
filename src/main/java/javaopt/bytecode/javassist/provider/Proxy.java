package javaopt.bytecode.javassist.provider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public abstract class Proxy {

    private static String modifier(int mod) {
        if (Modifier.isPublic(mod)) {
            return "public";
        }
        if (Modifier.isProtected(mod)) {
            return "protected";
        }
        if (Modifier.isPrivate(mod)) {
            return "private";
        }
        return "";
    }

    private static String getName(Class<?> c) {
        if (c.isArray()) {
            StringBuilder sb = new StringBuilder(c.getName());
            do {
                sb.append("[]");
                c = c.getComponentType();
            } while (c.isArray());
            return sb.toString();
        }
        return c.getName();
    }

    private static String args(Class<?>[] cs, String name) {
        int len = cs.length;
        if (len == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append('(').append(getName(cs[i])).append(')').append(name)
                    .append('[').append(i).append(']');
        }
        return sb.toString();
    }

    public static Proxy getProxy(Class<?> clazz) {
        List<String> mns = new ArrayList<String>();
        Map<String, Class<?>[]> ptsm = new HashMap<String, Class<?>[]>();
        String name = clazz.getName();
        StringBuilder sb = new StringBuilder(
                "public Object invokeMethod(Object o, String m, Class[] t, Object[] v) {");
        sb.append(name).append(" w = (").append(name).append(") $1;");
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getDeclaringClass() == Object.class) {
                continue;
            }
            String mn = m.getName();
            mns.add(mn);
            Class<?> rt = m.getReturnType();
            Class<?>[] pts = m.getParameterTypes();
            ptsm.put(mn, pts);
            sb.append("if (\"").append(mn).append("\".equals($2)")
                    .append(" && $3.length == ").append(pts.length);
            if (pts.length > 0) {
                for (int i = 0; i < pts.length; i++) {
                    sb.append(" && $3[").append(i)
                            .append("].getName().equals(\"")
                            .append(m.getParameterTypes()[i].getName())
                            .append("\")");
                }
            }
            sb.append(") {");
            if (rt == Void.TYPE) {
                sb.append("w.").append(mn).append('(').append(args(pts, "$4"))
                        .append(");").append(" return;");
            } else {
                sb.append("return ($w)w.").append(mn).append('(')
                        .append(args(pts, "$4")).append(");");
            }
            sb.append("} throw new ")
                    .append(NoSuchMethodException.class.getName())
                    .append("(\"Not found method \\\"\" + $2 + \"\\\" in class ")
                    .append(clazz.getName()).append(".\");}");
        }
        ClassPool pool = ClassPool.getDefault();
        CtClass cls = null;
        try {
            cls = pool.makeClass(clazz.getName() + "Proxy");
            CtClass superClass = pool.get(Proxy.class.getName());
            cls.setSuperclass(superClass);
            cls.addField(CtField.make("public " + Map.class.getName()
                    + " ptsm;", cls));
            cls.addMethod(CtNewMethod
                    .make("public Class[] getParameterType(String m){ return (Class[]) ptsm.get($1); }",
                            cls));
            cls.addField(CtField.make("public String[] mns;", cls));
            cls.addMethod(CtNewMethod.make(
                    "public String[] getMethodNames(){ return mns; }", cls));
            cls.addMethod(CtNewMethod.make(sb.toString(), cls));
            Class<?> pc = cls.toClass();
            Proxy proxy = (Proxy) pc.newInstance();
            pc.getField("ptsm").set(proxy, ptsm);
            pc.getField("mns").set(proxy, mns.toArray(new String[0]));
            return proxy;
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (cls != null) {
                cls.detach();
            }
        }
    }

    abstract public Class<?>[] getParameterType(String methodName);

    abstract public String[] getMethodNames();

    abstract public Object invokeMethod(Object instance, String methodName,
            Class<?>[] parameterTypes, Object[] parameterValues);

    public boolean hasMethod(String name) {
        for (String mn : getMethodNames())
            if (mn.equals(name))
                return true;
        return false;
    }

}
