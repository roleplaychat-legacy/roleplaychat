package ru.xunto.roleplaychat.framework.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
    public Map<String, String> variables = new HashMap<>();
    private Set<EntityPlayer> recipients = new HashSet<>();
    private TextComponentBase component = new TextComponentString("");
    private boolean processed = false;

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Set<EntityPlayer> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<EntityPlayer> recipients) {
        this.recipients = recipients;
    }

    public TextComponentBase getComponent() {
        return component;
    }

    public void setComponent(TextComponentBase component) {
        this.component = component;
    }
}
