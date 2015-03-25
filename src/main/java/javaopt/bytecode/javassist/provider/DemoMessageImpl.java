package javaopt.bytecode.javassist.provider;

public class DemoMessageImpl implements DemoMessage {

    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }

}
