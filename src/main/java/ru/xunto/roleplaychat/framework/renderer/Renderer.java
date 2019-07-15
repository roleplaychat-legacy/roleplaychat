package ru.xunto.roleplaychat.framework.renderer;

import net.minecraft.util.text.ITextComponent;
import ru.xunto.roleplaychat.framework.api.Environment;

/*
    TODO:
        incapsulate the minecraft-forge dependencies:
            - ITextComponent
*/


public abstract class Renderer {
    public abstract ITextComponent render(Environment environment);
}
