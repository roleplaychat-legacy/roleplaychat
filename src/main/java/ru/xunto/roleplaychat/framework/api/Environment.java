package ru.xunto.roleplaychat.framework.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.template.ITemplate;
import ru.xunto.roleplaychat.framework.template.Template;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment implements Cloneable {
    private Map<String, String> variables = new HashMap<>();
    private Map<String, TextFormatting> colors = new HashMap<>();

    private Set<EntityPlayer> recipients = new HashSet<>();

    private ITemplate template = new Template("{{ username }}: {{ text }}");

    private boolean processed = false;

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Map<String, TextFormatting> getColors() {
        return colors;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public Set<EntityPlayer> getRecipients() {
        return recipients;
    }

    public ITemplate getTemplate() {
        return template;
    }

    public void setTemplate(ITemplate template) {
        this.template = template;
    }

    @Override public Environment clone() {
        try {
            Environment environment = (Environment) super.clone();
            environment.colors = new HashMap<>(colors);
            environment.variables = new HashMap<>(variables);
            environment.recipients = new HashSet<>(recipients);

            return environment;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
