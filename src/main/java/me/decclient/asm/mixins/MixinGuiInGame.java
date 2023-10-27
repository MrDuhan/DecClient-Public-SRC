package me.decclient.asm.mixins;

import me.decclient.mod.modules.impl.client.HUD;
import me.decclient.mod.modules.impl.render.NoLag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import me.decclient.mod.modules.impl.render.NoRender;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngame.class})
public class MixinGuiInGame extends Gui {

    @Inject(method={"renderPotionEffects"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
        HUD mod = HUD.getInstance();

        if (mod.potionIcons.getValue() && mod.isOn()) {
            info.cancel();
        }
    }
    @Inject(method={"renderPumpkinOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().pumpkin.getValue()) {
            info.cancel();
        }
    }
    @Inject(method={"renderPortal"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().portal.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderScoreboard"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderScoreboardHook(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo info) {
        NoLag mod = NoLag.INSTANCE;

        if (mod.scoreBoards.getValue() && mod.isOn()) {
            info.cancel();
        }
    }
}
