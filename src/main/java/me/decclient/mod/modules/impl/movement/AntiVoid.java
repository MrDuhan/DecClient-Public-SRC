package me.decclient.mod.modules.impl.movement;

import me.decclient.api.util.interact.BlockUtil;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;



public class AntiVoid extends Module {

    private final Setting<Integer> height =
            add(new Setting<>("Height", 100, 0, 256));

    public AntiVoid() {
        super("AntiVoid", "Allows you to fly over void blocks.", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {

        if (fullNullCheck()) return;

        boolean isVoid = true;

        for (int i = (int) mc.player.posY; i > -1; --i) {

            if (BlockUtil.getBlock(new BlockPos(mc.player.posX, i, mc.player.posZ)) != Blocks.AIR) {
                isVoid = false;
                break;
            }
        }
        if (mc.player.posY < height.getValue() && isVoid) {
            mc.player.motionY = 0.0;
        }
    }
}
