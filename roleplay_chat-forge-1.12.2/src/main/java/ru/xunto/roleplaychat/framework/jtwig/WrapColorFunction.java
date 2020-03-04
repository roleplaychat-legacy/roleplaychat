package ru.xunto.roleplaychat.framework.jtwig;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

public class WrapColorFunction extends SimpleJtwigFunction {
    @Override public String name() {
        return "wrapColor";
    }

    @Override public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(2);

        String value = getString(request, 0);
        String colorName = getString(request, 1);

        return JTwigTemplate.wrapWithColor(value, colorName);
    }

    private String getString(FunctionRequest request, int index) {
        return request.getEnvironment().getValueEnvironment().getStringConverter()
            .convert(request.get(index));
    }
}
