package ru.xunto.roleplaychat.features;

import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextFormatting;
import ru.xunto.roleplaychat.framework.template.ITemplate;

import java.util.Map;

public class LabeledTemplate implements ITemplate {
    private final ITemplate template;
    private final ITemplate labeledTemplate;

    public LabeledTemplate(ITemplate template, ITemplate labeledTemplate) {
        this.template = template;
        this.labeledTemplate = labeledTemplate;
    }

    @Override
    public TextComponentBase build(Map<String, String> values, Map<String, TextFormatting> colors) {
        if (values.get("label") != null) {
            return labeledTemplate.build(values, colors);
        }

        return template.build(values, colors);
    }
}
