package ru.xunto.roleplaychat;

import ru.xunto.roleplaychat.framework.MiddlewareCallback;

import java.util.Collections;

public class TestUtility {
    public final static MiddlewareCallback DO_NOTHING =
        new MiddlewareCallback(Collections.EMPTY_LIST, null, null, (e) -> {
        });
}
