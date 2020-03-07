package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.api.ISpeaker;

public class Request {
    private final String text;
    private final ISpeaker requester;

    public Request(String text, ISpeaker requester) {
        this.text = text;
        this.requester = requester;
    }

    public ISpeaker getRequester() {
        return requester;
    }

    public String getText() {
        return text;
    }
}
