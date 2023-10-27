package me.decclient.mod.modules.impl.render;

import me.decclient.api.events.impl.PacketEvent;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FullBright
        extends Module {
    public Setting<Mode> mode = add(new Setting<Mode>("Mode", Mode.GAMMA));
    public Setting<Boolean> effects = add(new Setting<Boolean>("Effects", false));
    private float previousSetting = 1.0f;

    public FullBright() {
        super("Fullbright", "Makes your game brighter.", Category.RENDER);
    }

    public FullBright(String name, String description, Category category) {
        super(name, description, category);
    }

    @Override
    public void onEnable() {
        this.previousSetting = FullBright.mc.gameSettings.gammaSetting;
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.GAMMA) {
            FullBright.mc.gameSettings.gammaSetting = 1000.0f;
        }
        if (this.mode.getValue() == Mode.POTION) {
            FullBright.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.POTION) {
            FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
        FullBright.mc.gameSettings.gammaSetting = this.previousSetting;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue().booleanValue()) {
            SPacketEntityEffect packet = event.getPacket();
            if (FullBright.mc.player != null && packet.getEntityId() == FullBright.mc.player.getEntityId() && (packet.getEffectId() == 9 || packet.getEffectId() == 15)) {
                event.setCanceled(true);
            }
        }
    }

    public enum Mode {
        GAMMA,
        POTION

    }
}
