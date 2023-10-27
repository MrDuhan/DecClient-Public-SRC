package me.decclient.mod.modules.impl.movement;

import me.decclient.api.managers.Managers;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;



public class FastWeb extends Module {

    private final Setting<Mode> mode =
            add(new Setting<>("Mode", Mode.FAST));
    private final Setting<Float> fastSpeed =
            add(new Setting<>("FastSpeed", 3.0f, 0.0f, 5.0f, v -> mode.getValue() == Mode.FAST));

    public FastWeb() {
        super("FastWeb", "So you don't need to keep timer on keybind", Category.MOVEMENT);
    }

    private enum Mode {
        FAST,
        STRICT
    }

    @Override
    public void onDisable() {
        Managers.TIMER.reset();
    }

    @Override
    public String getInfo() {
        return Managers.TEXT.normalizeCases(mode.getValue());
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return;

        if (mc.player.isInWeb) {

            if (mode.getValue() == Mode.FAST && mc.gameSettings.keyBindSneak.isKeyDown()) {
                Managers.TIMER.reset();
                mc.player.motionY -= fastSpeed.getValue();

            } else if (mode.getValue() == Mode.STRICT && !mc.player.onGround && mc.gameSettings.keyBindSneak.isKeyDown()) {
                Managers.TIMER.set(8);

            } else {
                Managers.TIMER.reset();
            }

        } else {
            Managers.TIMER.reset();
        }
    }
}
