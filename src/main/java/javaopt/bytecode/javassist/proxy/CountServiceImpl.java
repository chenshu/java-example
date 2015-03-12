package javaopt.bytecode.javassist.proxy;

public class CountServiceImpl implements CountService {

    private int count = 0;

    public int count() {
        return count++;
    }
}
