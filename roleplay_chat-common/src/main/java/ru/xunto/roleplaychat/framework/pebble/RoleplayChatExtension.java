package ru.xunto.roleplaychat.framework.pebble;


import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;

import java.util.HashMap;
import java.util.Map;

public class RoleplayChatExtension extends AbstractExtension {
    @Override
    public Map<String, Function> getFunctions() {
        HashMap<String, Function> functions = new HashMap<>();
        functions.put("wrapColor", new WrapColorFunction.Function());
        functions.put("batch", new BatchFunction());
        return functions;
    }

    @Override
    public Map<String, Filter> getFilters() {
        HashMap<String, Filter> functions = new HashMap<>();
        functions.put("colorless", new ColorlessFilter());
        functions.put("wrapColor", new WrapColorFunction.Filter());
        return functions;
    }
}
