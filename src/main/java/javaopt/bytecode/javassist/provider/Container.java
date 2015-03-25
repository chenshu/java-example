package javaopt.bytecode.javassist.provider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Container {

    private static final Logger logger = LogManager.getLogger(Container.class
            .getClass());

    /**
     * container id
     */
    private String id;

    /**
     * start time container
     */
    private long startTime;

    /**
     * register interfaces
     */
    private List<ServiceConfig<?>> interfaces;

    /**
     * all service of container
     */
    private ConcurrentHashMap<String, Service<?>> services;

    public Container() {
        this.id = UUID.randomUUID().toString();
        this.startTime = System.currentTimeMillis();
        this.services = new ConcurrentHashMap<String, Service<?>>();
    }

    public void init() {
        for (ServiceConfig<?> config : interfaces) {
            register(config.getName(), config.getInterfaceName(),
                    config.getInstanceClass());
        }
    }

    private <T> void register(final String name, final String interfaceName,
            final T instance) {
        if (services.containsKey(name)) {
            return;
        }
        Service<T> service = new Service<T>(this, name, interfaceName, instance);
        service.export();
        services.put(name, service);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<ServiceConfig<?>> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ServiceConfig<?>> interfaces) {
        this.interfaces = interfaces;
    }

    public ConcurrentHashMap<String, Service<?>> getServices() {
        return services;
    }

    public void setServices(ConcurrentHashMap<String, Service<?>> services) {
        this.services = services;
    }

}
