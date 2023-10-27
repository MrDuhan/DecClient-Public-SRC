package me.decclient.asm.mixins;

import com.google.common.base.Predicate;
import me.decclient.api.events.impl.PerspectiveEvent;
import me.decclient.mod.modules.impl.exploit.NoHitBox;
import me.decclient.mod.modules.impl.render.Ambience;
import me.decclient.mod.modules.impl.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = {EntityRenderer.class})
public class MixinEntityRenderer {

    @Shadow
    public ItemStack itemActivationItem;

    Minecraft mc = Minecraft.getMinecraft();

    @Redirect(method = {"getMouseOver"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
        NoHitBox mod = NoHitBox.INSTANCE;

        if (mod.isOn()
                && (mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && mod.pickaxe.getValue()
                || mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && mod.crystal.getValue()
                || mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE && mod.gapple.getValue()
                || mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) && mod.obby.getValue()
                || mc.player.getHeldItemMainhand().getItem() == Items.FLINT_AND_STEEL
                || mc.player.getHeldItemMainhand().getItem() == Items.TNT_MINECART)) {

            return new ArrayList<>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Redirect(method = {"setupCameraTransform"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onSetupCameraTransform(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent(mc.displayWidth / (float) mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAngle(), f3, f4);
    }

    @Redirect(method = {"renderWorldPass"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent(mc.displayWidth / (float) mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAngle(), f3, f4);
    }

    @Redirect(method = {"renderCloudsCheck"}, at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderCloudsCheck(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent(mc.displayWidth / (float) mc.displayHeight);
        MinecraftForge.EVENT_BUS.post(perspectiveEvent);
        Project.gluPerspective(f, perspectiveEvent.getAngle(), f3, f4);
    }

    @ModifyVariable(method = "updateLightmap", at = @At("STORE"), index = 20)
    public int red(int red) {
        Ambience mod = Ambience.INSTANCE;

        if (mod.isOn() && mod.lightMap.booleanValue) {
            red = mod.lightMap.getValue().getRed();
        }
        return red;
    }

    @ModifyVariable(method = "updateLightmap", at = @At("STORE"), index = 21)
    public int green(int green) {
        Ambience mod = Ambience.INSTANCE;

        if (mod.isOn() && mod.lightMap.booleanValue) {
            green = mod.lightMap.getValue().getGreen();
        }
        return green;
    }
    @Inject(method={"setupFog"}, at={@At(value="HEAD")}, cancellable=true)
    public void setupFogHook(int startCoords, float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.NOFOG) {
            info.cancel();
        }
    }
    @Redirect(method={"setupFog"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpointHook(World worldIn, Entity entityIn, float p_186703_2_) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fog.getValue() == NoRender.Fog.AIR) {
            return Blocks.AIR.defaultBlockState;
        }
        return ActiveRenderInfo.getBlockStateAtEntityViewpoint((World)worldIn, (Entity)entityIn, (float)p_186703_2_);
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().hurtcam.getValue()) {
            info.cancel();
        }
    }
    @Redirect(method={"setupCameraTransform"}, at=@At(value="FIELD", target="Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
    public float prevTimeInPortalHook(EntityPlayerSP entityPlayerSP) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().nausea.getValue().booleanValue()) {
            return -3.4028235E38f;
        }
        return entityPlayerSP.prevTimeInPortal;
    }
    @Inject(method={"renderItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemActivationHook(CallbackInfo info) {
        if (this.itemActivationItem != null && NoRender.getInstance().isOn() && NoRender.getInstance().totemPops.getValue().booleanValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }

    @Inject(method={"updateLightmap"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }
    @ModifyVariable(method = "updateLightmap", at = @At("STORE"), index = 22)
    public int blue(int blue) {
        Ambience mod = Ambience.INSTANCE;

        if (mod.isOn() && mod.lightMap.booleanValue) {
            blue = mod.lightMap.getValue().getBlue();
        }
        return blue;
    }
}








