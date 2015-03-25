package javaopt.bytecode.javassist.provider;

public class ServiceConfig<T> {

    private String name;

    private String interfaceName;

    private T instanceClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public T getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(T instanceClass) {
        this.instanceClass = instanceClass;
    }

}
