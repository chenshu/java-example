package javaopt.bytecode.javassist.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Service<T> {

    private static final Logger logger = LogManager.getLogger(Service.class
            .getClass());

    private final Container container;

    private final String name;

    private final String interfaceName;

    private final Class<?> interfaceClass;

    private final T ref;

    private final Proxy proxy;

    public Service(Container container, String name, String interfaceName,
            T instance) {
        this.container = container;
        this.name = name;
        this.interfaceName = interfaceName;
        try {
            this.interfaceClass = Class.forName(interfaceName, true, Thread
                    .currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
        this.ref = instance;
        proxy = Proxy.getProxy(interfaceClass);
    }

    public void export() {
        // TODO
        String svc = "osp.demo.message.sayHi";
        String osp_svc = svc.substring(0, svc.lastIndexOf("."));
        String methodName = svc.substring(svc.lastIndexOf(".") + 1);
        Class<?>[] parameterTypes = proxy.getParameterType(methodName);
        if (!proxy.hasMethod(methodName) && !proxy.hasMethod(".__call")) {
            if (logger.isWarnEnabled()) {
                logger.warn("service not found: {}", svc);
            }
        } else {
            if (!proxy.hasMethod(methodName)) {
                Object[] params = null;

                Object ret = proxy.invokeMethod(ref, "___call", parameterTypes,
                        new Object[] { svc, params });
            } else {
                Object[] params = new Object[parameterTypes.length];
                Object ret = proxy.invokeMethod(ref, methodName,
                        parameterTypes, params);
            }
        }
    }

    public Container getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public T getRef() {
        return ref;
    }

    public Proxy getProxy() {
        return proxy;
    }

}
