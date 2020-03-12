package ru.xunto.roleplaychat.framework.jtwig.functions;

import org.jtwig.functions.FunctionRequest;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;

public class WrapColorFunction extends AbstractFunction {
    @Override
    public String name() {
        return "wrapColor";
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2).maximumNumberOfArguments(2);

        String value = getString(request, 0);
        String colorName = getString(request, 1);

        return JTwigTemplate.wrapWithColor(value, colorName);
    }
}
