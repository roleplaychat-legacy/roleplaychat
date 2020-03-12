package ru.xunto.roleplaychat.framework.jtwig.functions;

import org.jtwig.functions.FunctionRequest;

public class ColorlessFunction extends AbstractFunction {
    @Override
    public String name() {
        return "colorless";
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);

        String value = getString(request, 0);

        return value.replaceAll("§[0123456789abcdef]", "");
    }
}
