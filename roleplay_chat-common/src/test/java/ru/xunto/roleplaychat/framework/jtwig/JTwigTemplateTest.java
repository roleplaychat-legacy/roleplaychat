package ru.xunto.roleplaychat.framework.jtwig;

import org.junit.Test;
import ru.xunto.roleplaychat.framework.api.Environment;
import ru.xunto.roleplaychat.framework.renderer.ITemplate;
import ru.xunto.roleplaychat.framework.renderer.text.TextColor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static ru.xunto.roleplaychat.features.endpoints.ActionEndpoint.ACTION_PARTS;
import static ru.xunto.roleplaychat.features.endpoints.ActionEndpoint.START_WITH_ACTION;


public class JTwigTemplateTest {
    public static void testCase(ITemplate template, JTwigState state, String expectedResult) {
        Map<String, TextColor> colors = new HashMap<>();
        String result = template.render(state, colors).getUnformattedText();
        assertEquals(result, expectedResult);
    }


    public static JTwigState setUpState() {
        JTwigState state = new JTwigState();
        state.setValue(Environment.USERNAME, "username");
        state.setValue(Environment.TEXT, "text");
        return state;
    }

    @Test public void testRenderDefault() {
        ITemplate template;
        JTwigState state;

        state = setUpState();
        template = new JTwigTemplate("templates/default.twig");
        testCase(template, state, "username: text");
        state.setValue(Environment.LABEL, "label");

        testCase(template, state, "username (label): text");

    }

    @Test public void testRenderGmOOC() {
        JTwigState state = setUpState();
        ITemplate template = new JTwigTemplate("templates/gm_ooc.twig");
        testCase(template, state, "[TGM] username: (( text ))");
    }

    @Test public void testRenderOOC() {
        ITemplate template;
        JTwigState state;

        state = setUpState();
        template = new JTwigTemplate("templates/ooc.twig");
        testCase(template, state, "[ООС] username: (( text ))");
        state.setValue(Environment.LABEL, "label");
        testCase(template, state, "[ООС] username (label): (( text ))");
    }

    @Test public void testRenderAction() {
        ITemplate template;
        JTwigState state;

        state = setUpState();
        template = new JTwigTemplate("templates/action.twig");

        state.setValue(ACTION_PARTS, new String[] {"text1", "text2", "text3", "text4"});

        state.setValue(START_WITH_ACTION, false);
        testCase(template, state, "username: text1 * text2 * text3 * text4 *");
        state.setValue(START_WITH_ACTION, true);
        testCase(template, state, "* username text1 * text2 * text3 * text4");

        state.setValue(Environment.LABEL, "label");
        state.setValue(START_WITH_ACTION, false);
        testCase(template, state, "username (label): text1 * text2 * text3 * text4 *");
        state.setValue(START_WITH_ACTION, true);
        testCase(template, state, "* username (label) text1 * text2 * text3 * text4");
    }
}
