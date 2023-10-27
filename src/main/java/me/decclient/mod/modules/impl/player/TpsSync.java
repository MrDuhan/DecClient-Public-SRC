package me.decclient.mod.modules.impl.player;

import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;

public class TpsSync extends Module {

    public static TpsSync INSTANCE;

    public Setting<Boolean> attack =
            add(new Setting<>("Attack", false));
    public Setting<Boolean> mining =
            add(new Setting<>("Mine", true));

    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Category.PLAYER);
        INSTANCE = this;
    }
}

