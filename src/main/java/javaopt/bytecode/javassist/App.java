package javaopt.bytecode.javassist;

import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

public class App {

    public static void sample1() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        // ClassPool pool = new ClassPool(true);
        // ClassPool pool = new ClassPool();
        // pool.appendSystemPath();

        CtClass cPoint = pool.makeClass("Point");
        cPoint.addConstructor(CtNewConstructor.defaultConstructor(cPoint));

        cPoint.writeFile();

        CtClass cPair = pool.makeClass("Pair");
        cPair.setSuperclass(cPoint);

        CtField cf = new CtField(pool.get("java.lang.String"), "key", cPair);
        cf.setModifiers(Modifier.PUBLIC | Modifier.VOLATILE);
        cPair.addField(cf);

        CtMethod cm = CtNewMethod.setter("setKey", cf);
        cPair.addMethod(cm);

        CtMethod cm2 = CtNewMethod.getter("getKey", cf);
        cPair.addMethod(cm2);

        CtField cf2 = new CtField(CtClass.intType, "value", cPair);
        cf2.setModifiers(Modifier.PUBLIC);
        cPair.addField(cf2);

        CtMethod cm3 = CtNewMethod.setter("setValue", cf2);
        cPair.addMethod(cm3);

        CtMethod cm4 = CtNewMethod.getter("getValue", cf2);
        cPair.addMethod(cm4);

        cPair.writeFile();

        cPair.defrost();
        cPair.setName("Line");
        cPair.writeFile();

        cPoint.detach();
        cPair.detach();
    }

    public static void sample2() throws Exception {
        ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        CtClass cc = pool.get("javaopt.bytecode.javassist.Foo");
        CtClass cc1 = pool.get("javaopt.bytecode.javassist.Foo");
        System.out.println(cc.equals(cc1));
        cc.setName("javaopt.bytecode.javassist.Bar");
        System.out.println(cc.equals(cc1));
        CtClass cc2 = pool.get("javaopt.bytecode.javassist.Bar");
        System.out.println(cc2.equals(cc));
        System.out.println(cc2.equals(cc1));
        CtClass cc3 = pool.get("javaopt.bytecode.javassist.Foo");
        System.out.println(cc3.equals(cc));
        System.out.println(cc3.equals(cc1));
        System.out.println(cc3.equals(cc2));
    }

    public static void sample3() throws Exception {
        ClassPool pool = new ClassPool(true);
        CtClass c1 = pool.makeClass("Parent");

        CtMethod m1 = new CtMethod(CtClass.voidType, "study", null, c1);
        m1.setBody(null);
        c1.addMethod(m1);

        CtClass c2 = pool.makeClass("Child");
        c2.setSuperclass(c1);

        CtMethod m2 = CtNewMethod.copy(m1, c2, null);
        m2.setBody(null);
        c2.addMethod(m2);

        System.out.println(m1.equals(m2));

        CtMethod m3 = c2.getDeclaredMethod("study");
        CtMethod m4 = c2.getMethod("study", m1.getSignature());

        System.out.println(m1.equals(m3));
        System.out.println(m1.equals(m4));
        System.out.println(m3.equals(m4));

        c1.writeFile();
        c2.writeFile();
    }

    public static void sample4() throws Exception {
        ClassPool pool = new ClassPool(true);
        CtClass cc = pool.get("javaopt.bytecode.javassist.Point");

        CtMethod cm = cc.getDeclaredMethod("move");
        cm.insertBefore("{System.out.println($0);System.out.println($1);System.out.println($2);}");
        CtClass etype = pool.get("java.lang.Exception");
        cm.addCatch("{System.out.println($e); throw $e;}", etype);
        CtMethod cm2 = CtNewMethod.make(
                "public int xmove(int dx) { x += dx; return x;}", cc);
        cc.addMethod(cm2);
        CtMethod cm3 = CtNewMethod.make(
                "public int ymove(int dy) { $proceed(0, dy); return y;}", cc,
                "this", "move");
        cc.addMethod(cm3);

        CtField f = new CtField(CtClass.intType, "z", cc);
        cc.addField(f, "0");

        cc.writeFile();
    }

    public static void main(String[] args) throws Exception {
        sample1();
        sample2();
        sample3();
        sample4();
    }
}
