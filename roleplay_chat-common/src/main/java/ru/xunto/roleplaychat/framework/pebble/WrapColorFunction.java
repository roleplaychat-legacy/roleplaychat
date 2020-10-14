package ru.xunto.roleplaychat.framework.pebble;


import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WrapColorFunction {
    static class Function implements com.mitchellbosecke.pebble.extension.Function {
        @Override
        public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
            String value = (String) args.getOrDefault("value", "");
            String colorName = (String) args.getOrDefault("colorName", "");

            return PebbleChatTemplate.wrapWithColor(value, colorName);
        }

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("value");
            names.add("colorName");
            return names;
        }
    }

    static class Filter implements com.mitchellbosecke.pebble.extension.Filter {
        @Override
        public Object apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException {
            String colorName = (String) args.getOrDefault("colorName", "");
            return PebbleChatTemplate.wrapWithColor(String.valueOf(input), colorName);
        }

        @Override
        public List<String> getArgumentNames() {
            List<String> names = new ArrayList<>();
            names.add("colorName");
            return names;
        }
    }
}
