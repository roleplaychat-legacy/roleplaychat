package ru.xunto.roleplaychat.framework.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.CoreChat;
import ru.xunto.roleplaychat.framework.jtwig.JTwigState;
import ru.xunto.roleplaychat.framework.jtwig.JTwigTemplate;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.state.IProperty;
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

    private CoreChat core;
    private ITemplate<JTwigState> template = new JTwigTemplate("templates/default.twig");
    private boolean processed = false;


    private JTwigState state = new JTwigState();
    private Map<String, TextFormatting> colors = new HashMap<>();
    private Set<EntityPlayer> recipients = new HashSet<>();

    public Environment(String username, String text) {
        state.setValue(USERNAME, username);
        state.setValue(TEXT, text);

        colors.put("username", TextFormatting.GREEN);
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Map<String, TextFormatting> getColors() {
        return colors;
    }

    public Set<EntityPlayer> getRecipients() {
        return recipients;
    }

    public ITemplate<JTwigState> getTemplate() {
        return template;
    }

    public void setTemplate(JTwigTemplate template) {
        this.template = template;
    }

    @Override public Environment clone() {
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

    public JTwigState getState() {
        return state;
    }

    public CoreChat getCore() {
        return core;
    }

    public void setCore(CoreChat core) {
        this.core = core;
    }
}
