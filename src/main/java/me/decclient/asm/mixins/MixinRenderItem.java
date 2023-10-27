package me.decclient.asm.mixins;

import me.decclient.mod.modules.impl.render.Model;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem {

    private float angle;
    private Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE))
    private void renderCustom(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        Model mod = Model.INSTANCE;

        float scale = 1.0f;
        float xOffset = 0.0f;
        float yOffset = 0.0f;
        float zOffset = 0.0f;

        if (leftHanded && mod.isOn() && mc.player.getHeldItemOffhand() == stack) {
            scale = mod.offScale.getValue().floatValue();
            xOffset = mod.offX.getValue().floatValue();
            yOffset = mod.offY.getValue().floatValue();
            zOffset = mod.mainZ.getValue().floatValue();
        } else if (mod.isOn() && mc.player.getHeldItemMainhand() == stack) {
            scale = mod.mainScale.getValue().floatValue();
            xOffset -= mod.mainX.getValue().floatValue();
            yOffset = mod.mainY.getValue().floatValue();
            zOffset = mod.mainZ.getValue().floatValue();
        }

        if (mod.isOn()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(xOffset, yOffset, zOffset);
            GlStateManager.translate(0.0, 0.0, 0.0);
        }
    }

    @Inject(method = "renderItemModel", at = @At("RETURN"))
    private void popMatrix(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        Model mod = Model.INSTANCE;

        if (mod.isOn()) {
            GlStateManager.popMatrix();
        }
    }

    @Inject(method = "renderItemModel", at = @At("HEAD"))
    private void renderItem(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        Model mod = Model.INSTANCE;

        if (mod.isOn() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {

            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {

                if (mc.player.getActiveHand() == EnumHand.OFF_HAND && mc.player.isHandActive()) {
                    return;
                }
            }

            if (mod.isOn() && (mod.spinX.getValue() || mod.spinY.getValue())) {
                GlStateManager.rotate(angle, mod.spinX.getValue() ? angle : 0, mod.spinY.getValue() ? angle : 0, 0);
                angle++;
            }
        } else {
            if (mc.player.getActiveHand() == EnumHand.MAIN_HAND && mc.player.isHandActive()) {
                return;
            }

            if (mod.isOn() && (mod.spinX.getValue() || mod.spinY.getValue())) {
                GlStateManager.rotate(angle, mod.spinX.getValue() ? angle : 0, mod.spinY.getValue() ? angle : 0, 0);
                angle++;
            }
        }
    }
}