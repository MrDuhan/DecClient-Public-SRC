package me.decclient.api.managers.impl;

import me.decclient.api.util.render.ColorUtil;
import me.decclient.mod.modules.impl.client.ClickGui;

import java.awt.*;



public class ColorManager {

    private Color current = new Color(-1);

    public boolean isRainbow() {
        return ClickGui.INSTANCE.rainbow.getValue();
    }

    //Getters

    public Color getCurrent() {
        if (isRainbow()) {
            return getRainbow();
        }

        return current;
    }

    public int getCurrentWithAlpha(int alpha) {
        if (isRainbow()) {
            return ColorUtil.toRGBA(ColorUtil.injectAlpha(getRainbow(), alpha));
        }
        return ColorUtil.toRGBA(ColorUtil.injectAlpha(current, alpha));
    }

    public int getCurrentGui(int alpha) {
        if (isRainbow()) {
            return ColorUtil.rainbow(me.decclient.mod.gui.click.Component.counter1[0] * ClickGui.INSTANCE.rainbowDelay.getValue()).getRGB();
        }
        return ColorUtil.toRGBA(ColorUtil.injectAlpha(current, alpha));
    }

    public Color getRainbow() {
        return ColorUtil.rainbow(ClickGui.INSTANCE.rainbowDelay.getValue());
    }

    public Color getFriendColor(int alpha) {
        return new Color(0, 191, 255, alpha);
    }

    //Setters

    public void setCurrent(Color color) {
        current = color;
    }
}

