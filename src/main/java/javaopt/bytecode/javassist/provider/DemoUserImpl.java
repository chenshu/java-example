package javaopt.bytecode.javassist.provider;

import java.util.HashMap;
import java.util.Map;

public class DemoUserImpl implements DemoUser {

    @Override
    public Map<String, String> getInfo() {
        Map<String, String> info = new HashMap<String, String>();
        info.put("name", "fred");
        info.put("age", "100");
        info.put("sex", "male");
        return info;
    }

}
