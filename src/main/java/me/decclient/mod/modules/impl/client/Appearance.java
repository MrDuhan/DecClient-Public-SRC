package me.decclient.mod.modules.impl.client;

import me.decclient.mod.gui.screen.DecAppearance;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;

public class Appearance extends Module {

    public Appearance() {
        super("Appearance", "Drag HUD elements all over your screen.", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(DecAppearance.getClickGui());
    }

    @Override
    public void onTick() {
        if (!(mc.currentScreen instanceof DecAppearance)) {
            disable();
        }
    }
}
