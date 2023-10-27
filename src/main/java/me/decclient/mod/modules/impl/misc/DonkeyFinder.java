//I rewrited this shit from GhastNotifier @Duhan



package me.decclient.mod.modules.impl.misc;

import me.decclient.mod.commands.Command;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import me.decclient.mod.modules.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.init.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class DonkeyFinder extends Module {

    private final Setting<Boolean> chat =
            add(new Setting<>("Chat", true).setParent());
    private final Setting<Boolean> censorCoords =
            add(new Setting<>("CensorCoords", false, v -> chat.isOpen()));
    private final Setting<Boolean> sound =
            add(new Setting<>("Sound", true));
    private final Setting<Boolean> Donkey =
            add(new Setting<>("Donkey", true));
    private final Setting<Boolean> Llama =
            add(new Setting<>("Llama", true));

    private final Set<Entity> donkeys = new HashSet<>();
    private final Set<Entity> llamas = new HashSet<>();

    public DonkeyFinder() {
        super("DonkeyFinder", "Finds Llama and Donkey", Category.MISC);
    }

    @Override
    public void onEnable() {
        donkeys.clear();
    }

    @Override
    public void onUpdate() {
        for (Entity entity1 : GhastNotifier.mc.world.getLoadedEntityList()) {

            if (!(entity1 instanceof EntityDonkey) || donkeys.contains(entity1)) continue;
            if (Donkey.getValue())
                if (chat.getValue()) {
                    if (censorCoords.getValue()) {
                        Command.sendMessage("There is a §lDonkey!");

                    } else {
                        Command.sendMessage(
                                "There is a §lDonkey at: "
                                        + entity1.getPosition().getX()
                                        + "X, "
                                        + entity1.getPosition().getY()
                                        + "Y, "
                                        + entity1.getPosition().getZ()
                                        + "Z.");
                    }
                }
            donkeys.add(entity1);

            if (!sound.getValue()) continue;

            GhastNotifier.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }

        {
            for (Entity entity2 : GhastNotifier.mc.world.getLoadedEntityList()) {

                if (!(entity2 instanceof EntityLlama) || llamas.contains(entity2)) continue;
                if (Llama.getValue())
                    if (chat.getValue()) {
                        if (censorCoords.getValue()) {
                            Command.sendMessage("There is a §lLlama!");

                        } else {
                            Command.sendMessage(
                                    "There is a §lLlama §rat: "
                                            + entity2.getPosition().getX()
                                            + "X, "
                                            + entity2.getPosition().getY()
                                            + "Y, "
                                            + entity2.getPosition().getZ()
                                            + "Z.");
                        }
                    }
                llamas.add(entity2);

                if (!sound.getValue()) continue;

                GhastNotifier.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }
}
