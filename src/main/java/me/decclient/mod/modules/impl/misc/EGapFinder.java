package me.decclient.mod.modules.impl.misc;

import java.util.HashSet;
import java.util.Set;

import me.decclient.mod.commands.Command;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@EventBusSubscriber
public class EGapFinder extends Module {
    public EGapFinder() {
        super("EGapFinder", "Finds Egaps!", Category.MISC);
    }

    private final Set<Entity> chests = new HashSet<>();
    private final Set<TileEntity> chests2 = new HashSet<>();

    //"Enabled"

    @Override
    public void onEnable() {
        chests.clear();
        chests2.clear();
    }

    @Override
    public void onUpdate() {
        if (mc.world == null || mc.player == null)
            return;

        World world = mc.world;
        EntityPlayer player = mc.player;

    //"Fix it use it"

        for (TileEntity tile : EGapFinder.mc.world.loadedTileEntityList) {
            if (chests2.contains(tile))
                continue;

            if (tile instanceof TileEntityLockableLoot) {
                TileEntityLockableLoot lockable = (TileEntityLockableLoot) tile;
                if (lockable.getLootTable() != null) {
                    lockable.fillWithLoot(player);
                    for (int i = 0; i < lockable.getSizeInventory(); i++) {
                        ItemStack stack = lockable.getStackInSlot(i);
                        if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1)
                            Command.sendMessage("Dungeon Chest with ench gapple at: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                        if (stack.getItem() == Items.ENCHANTED_BOOK &&
                                EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0)
                            Command.sendMessage("Dungeon Chest with Mending Book: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                    }
                }
                chests2.add(tile);
            }
        }

        for (Entity entity1 : EGapFinder.mc.world.loadedEntityList) {
            if (chests.contains(entity1))
                continue;

            if (entity1 instanceof EntityMinecartContainer) {
                EntityMinecartContainer cart = (EntityMinecartContainer) entity1;
                if (cart.getLootTable() != null) {
                    cart.addLoot(player);
                    for (int i = 0; i < cart.itemHandler.getSlots(); i++) {
                        ItemStack stack = cart.itemHandler.getStackInSlot(i);
                        if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1)
                            Command.sendMessage("Minecart with ench gapple at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                        if (stack.getItem() == Items.ENCHANTED_BOOK &&
                                EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0)
                            Command.sendMessage("Minecart with Mending at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                    }
                }
                chests.add(entity1);
            }
        }
    }
}
