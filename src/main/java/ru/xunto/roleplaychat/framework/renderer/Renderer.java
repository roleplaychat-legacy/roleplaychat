package ru.xunto.roleplaychat.framework.renderer;

import net.minecraft.util.text.ITextComponent;
import ru.xunto.roleplaychat.framework.api.Environment;

public abstract class Renderer {
    public abstract ITextComponent render(Environment environment);
}
