package javaopt.bytecode.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;

public class Main {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass cPoint = pool.makeClass("Point");
        cPoint.addConstructor(CtNewConstructor.defaultConstructor(cPoint));

        CtClass cRectangle = pool.makeClass("Rectangle");
        cRectangle.setSuperclass(cPoint);

        CtField cf = new CtField(pool.get("java.lang.String"), "key",
                cRectangle);
        cf.setModifiers(Modifier.PUBLIC | Modifier.VOLATILE);
        cRectangle.addField(cf);

        CtMethod cm = CtNewMethod.setter("setKey", cf);
        cRectangle.addMethod(cm);

        CtMethod cm2 = CtNewMethod.getter("getKey", cf);
        cRectangle.addMethod(cm2);

        CtField cf2 = new CtField(CtClass.intType, "value", cRectangle);
        cf2.setModifiers(Modifier.PUBLIC);
        cRectangle.addField(cf2);

        CtMethod cm3 = CtNewMethod.setter("setValue", cf2);
        cRectangle.addMethod(cm3);

        CtMethod cm4 = CtNewMethod.getter("getValue", cf2);
        cRectangle.addMethod(cm4);

        cRectangle.writeFile();
    }

}
