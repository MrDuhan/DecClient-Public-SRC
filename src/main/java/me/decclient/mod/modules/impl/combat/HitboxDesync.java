package me.decclient.mod.modules.impl.combat;

import me.decclient.mod.commands.Command;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HitboxDesync extends Module {

    private final double MAGIC_NUMBER = .200009968835369999878673424677777777777761;

    public HitboxDesync() {
        super("HitboxDesync", "pvp module", Category.COMBAT);
    }

    private final Setting<Boolean> autoDisable =
            add(new Setting<>("AutoDisable", true));



    public void onUpdate() {


        final EnumFacing facing = mc.player.getHorizontalFacing();
        final AxisAlignedBB bb = mc.player.getEntityBoundingBox();
        final Vec3d center = bb.getCenter();
        final Vec3d offset = new Vec3d(facing.getDirectionVec());
        final Vec3d fin = merge(new Vec3d(new BlockPos(center)).add(0.5, 0.0, 0.5).add(offset.scale(MAGIC_NUMBER)), facing);


        centerMotion((fin.x == 0.0) ? mc.player.posX : fin.x, mc.player.posY, (fin.z == 0.0) ? mc.player.posZ : fin.z);
        if (autoDisable.getValue())
            toggle();

    }

    private Vec3d merge(final Vec3d a, final EnumFacing facing) {
        return new Vec3d(a.x * Math.abs(facing.getDirectionVec().getX()), a.y * Math.abs(facing.getDirectionVec().getY()), a.z * Math.abs(facing.getDirectionVec().getZ()));

    }

    private void centerMotion(double x, double y, double z) {

        double[] centerPos = {x, y, z};

        mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
        mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;


    }

}