package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.state.IProperty;
import ru.xunto.roleplaychat.framework.state.MessageState;
import ru.xunto.roleplaychat.framework.state.Property;
import ru.xunto.roleplaychat.framework.template.ITemplate;

import java.util.Map;

public class LabeledTemplate implements ITemplate {
    public static final IProperty<String> LABEL = new Property<>("label");

    private final ITemplate template;
    private final ITemplate labeledTemplate;

    public LabeledTemplate(ITemplate template, ITemplate labeledTemplate) {
        this.template = template;
        this.labeledTemplate = labeledTemplate;
    }

    @Override
    public TextComponentBase build(MessageState state, Map<String, TextFormatting> colors) {
        if (state.getValue(LABEL) != null) {
            return labeledTemplate.build(state, colors);
        }

        return template.build(state, colors);
    }
}
