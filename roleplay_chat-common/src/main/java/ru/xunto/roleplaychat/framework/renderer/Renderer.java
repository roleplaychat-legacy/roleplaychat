package ru.xunto.roleplaychat.framework.renderer;

import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.text.Text;

public abstract class Renderer {
    public abstract Text render(Environment environment);
}
