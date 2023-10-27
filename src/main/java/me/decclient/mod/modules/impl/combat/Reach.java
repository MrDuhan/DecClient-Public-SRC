package me.decclient.mod.modules.impl.combat;

// Gördüğüm en boktan reach
// @Duhan eklemeler yaptım

import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;

public class Reach
        extends Module {
    private static Reach INSTANCE = new Reach();
    public Setting<Boolean> override = add(new Setting<Boolean>("Override", false));
    public Setting<Float> add = add(new Setting<Object>("Add", 3, 3, 6));
    public Setting<Float> reach = add(new Setting<Object>("Reach", 6, 0, 12));

    public Reach() {
        super("Reach", "Extends your block reach", Category.COMBAT);
        this.setInstance();
    }

    public static Reach getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Reach();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public String getDisplayInfo() {
        return this.override.getValue() != false ? this.reach.getValue().toString() : this.add.getValue().toString();
    }
}
