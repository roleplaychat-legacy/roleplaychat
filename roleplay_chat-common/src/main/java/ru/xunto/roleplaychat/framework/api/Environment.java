package ru.xunto.roleplaychat.framework.api;

import ru.xunto.roleplaychat.RoleplayChatCore;
import ru.xunto.roleplaychat.api.ISpeaker;
import ru.xunto.roleplaychat.framework.pebble.PebbleChatTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - EntityPlayer
            - TextFormatting
*/


public class Environment implements Cloneable {
    public final static IProperty<String> USERNAME = new Property<>("username");
    public final static IProperty<String> LABEL = new Property<>("label", false);
    public final static IProperty<String> TEXT = new Property<>("text");

    private RoleplayChatCore core;
    private ITemplate template = new PebbleChatTemplate("templates/default.pebble.twig");
    private boolean processed = false;

    private MessageState state = new MessageState();
    private Map<String, TextColor> colors = new HashMap<>();
    private Set<ISpeaker> recipients = new HashSet<>();

    public Environment(String username, String text) {
        state.setValue(USERNAME, username);
        state.setValue(TEXT, text);

        colors.put("username", TextColor.GREEN);
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Map<String, TextColor> getColors() {
        return colors;
    }

    public Set<ISpeaker> getRecipients() {
        return recipients;
    }

    public ITemplate getTemplate() {
        return template;
    }

    public void setTemplate(ITemplate template) {
        this.template = template;
    }

    @Override
    public Environment clone() {
        try {
            Environment environment = (Environment) super.clone();
            environment.colors = new HashMap<>(colors);
            environment.state = state.clone();
            environment.recipients = new HashSet<>(recipients);

            return environment;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public MessageState getState() {
        return state;
    }

    public RoleplayChatCore getCore() {
        return core;
    }

    public void setCore(RoleplayChatCore core) {
        this.core = core;
    }
}
