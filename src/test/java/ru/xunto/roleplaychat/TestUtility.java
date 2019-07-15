package ru.xunto.roleplaychat;

import ru.xunto.roleplaychat.framework.middleware_flow.Flow;

import java.util.Collections;

public class TestUtility {
    public final static Flow DO_NOTHING = new Flow(Collections.EMPTY_LIST, null, null, (e) -> {
    });
}
