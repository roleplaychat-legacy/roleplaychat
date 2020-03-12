package ru.xunto.roleplaychat.framework.jtwig.functions;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

public abstract class AbstractFunction extends SimpleJtwigFunction {
    protected String getString(FunctionRequest request, int index) {
        return request.getEnvironment().getValueEnvironment().getStringConverter()
                .convert(request.get(index));
    }
}
