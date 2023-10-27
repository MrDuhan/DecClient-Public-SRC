package me.decclient;

import me.decclient.api.managers.Managers;
import me.decclient.api.util.git.GitUtil;
import me.decclient.api.util.render.RenderUtil;
import me.decclient.mod.gui.screen.DecClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;

@Mod(modid = Dec.MODID, name = "Dec", version = Dec.MODVER)
public class Dec {

    @Mod.Instance
    public static Dec INSTANCE;

    public static final String MODID = "decclient";
    public static final String MODVER = "beta0.4";
    public static final String VERHASH = GitUtil.GIT_SHA.substring(0, 12);
    public static final Logger LOGGER = LogManager.getLogger("Dec");

    public static void load() {
        LOGGER.info("Loading Dec...");

        Managers.load();

        if (DecClickGui.INSTANCE == null) {
            DecClickGui.INSTANCE = new DecClickGui();
        }

        LOGGER.info("Dec successfully loaded!\n");
    }

    public static void unload(boolean force) {
        LOGGER.info("Unloading Dec...");

        Managers.unload(force);

        LOGGER.info("Dec successfully unloaded!\n");
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle(MODID);

        if (Util.getOSType() != Util.EnumOS.OSX) {

            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream(""); InputStream inputStream32x = Minecraft.class.getResourceAsStream("")) {

                ByteBuffer[] icons = new ByteBuffer[] {
                        RenderUtil.readImageToBuffer(inputStream16x), RenderUtil.readImageToBuffer(inputStream32x)
                };

                Display.setIcon(icons);

            } catch (Exception e) {
                LOGGER.error("Dec couldn't set the window icon!", e);
            }
        }
        load();
    }
}

