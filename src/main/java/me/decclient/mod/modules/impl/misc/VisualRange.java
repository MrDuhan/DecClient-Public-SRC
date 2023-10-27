
/**
 * @Duhan
 */

package me.decclient.mod.modules.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.decclient.mod.commands.Command;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Set;

public class VisualRange extends Module {

    private Set<String> playersInRange = new HashSet<>();

    public VisualRange() {
        super("VisualRange", "Visual Range Module", Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (mc.world == null || mc.player == null) {
            return;
        }

        Set<String> playersInRangeThisUpdate = new HashSet<>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.player) {
                double distance = mc.player.getDistanceSq(entity);
                if (distance <= 64 * 64) {
                    String playerName = ((EntityPlayer) entity).getName();
                    playersInRangeThisUpdate.add(playerName);
                    if (!playersInRange.contains(playerName)) {
                        Command.sendMessage(ChatFormatting.GREEN + playerName + " is in visual range!");
                        VisualRange.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
            }
        }

        for (String playerInSet : playersInRange) {
            if (!playersInRangeThisUpdate.contains(playerInSet)) {
                Command.sendMessage(ChatFormatting.RED + playerInSet + " left visual range.");
                VisualRange.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }

        playersInRange = playersInRangeThisUpdate;
    }
}
