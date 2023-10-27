package me.decclient.mod.modules.impl.player;

import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;

public class NameProtect extends Module {

    public static NameProtect INSTANCE;

    public final Setting<String> name =
            add(new Setting("Name", "Me"));

    public NameProtect() {
        super("NameProtect", "To keep your alts in secret.", Category.PLAYER);
        INSTANCE = this;
    }
}
