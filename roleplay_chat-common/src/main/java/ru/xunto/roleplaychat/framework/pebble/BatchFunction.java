package ru.xunto.roleplaychat.framework.pebble;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchFunction implements Function {
    private static List<Object> newCursor(List<List<Object>> resultArray) {
        List<Object> cursor = new ArrayList<>();
        resultArray.add(cursor);
        return cursor;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        List<Object> array = (List<Object>) args.getOrDefault("array", "");
        Number batchSize = (Number) args.getOrDefault("batch_size", 5);

        List<List<Object>> resultArray = new ArrayList<>();
        List<Object> cursor = BatchFunction.newCursor(resultArray);
        for (Object o : array) {
            cursor.add(o);

            if (cursor.size() >= batchSize.intValue()) {
                cursor = BatchFunction.newCursor(resultArray);
            }
        }

        return resultArray;
    }

    @Override
    public List<String> getArgumentNames() {
        List<String> names = new ArrayList<>();
        names.add("array");
        names.add("batch_size");
        return names;
    }
}
