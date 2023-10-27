package me.decclient.mod.modules.impl.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.decclient.Dec;
import me.decclient.mod.modules.Category;
import me.decclient.mod.modules.Module;
import net.minecraft.client.gui.GuiMainMenu;

import java.util.Random;

public class DiscordRPC extends Module {

    private final club.minnced.discord.rpc.DiscordRPC rpc = club.minnced.discord.rpc.DiscordRPC.INSTANCE;

    private final DiscordRichPresence presence = new DiscordRichPresence();

    private Thread thread;

    private final String[] state = {
            "",
            "CrYsTaL PvP GoD",
            "Griefing the Servers",
            "",
            "",
            "playing minecraft with decclient",
            "Anti newfag",
            "Just destroying the entire server",
            "DecClient",
    };

    public DiscordRPC() {
        super("DiscordRPC", "Discord rich presence", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (isOn()) {
            start();
        }
    }

    private void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        rpc.Discord_Initialize("1116799463474868326", handlers, true, "");

        presence.startTimestamp = System.currentTimeMillis() / 1000L;

        presence.details = "Dec " + Dec.MODVER;

        presence.state = state[new Random().nextInt(state.length)];

        presence.largeImageKey = "a";
        presence.largeImageText = "dec " + Dec.MODVER;

        presence.smallImageKey = ((mc.currentScreen instanceof GuiMainMenu ? "idling" :
                (mc.currentServerData != null ? "multiplayer" : "singleplayer")));

        presence.smallImageText = ((mc.currentScreen instanceof GuiMainMenu ? "Idling." :
                (mc.currentServerData != null ? "Playing multiplayer." : "Playing singleplayer.")));

        rpc.Discord_UpdatePresence(presence);

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();

                presence.details = "Dec " + Dec.MODVER;

                presence.state = state[new Random().nextInt(state.length)];

                presence.smallImageKey = ((mc.currentScreen instanceof GuiMainMenu ? "iding" :
                        (mc.currentServerData != null ? "multiplayer" : "singleplayer")));

                presence.smallImageText = ((mc.currentScreen instanceof GuiMainMenu ? "Iding." :
                        (mc.currentServerData != null ? "Playing multiplayer." : "Playing singleplayer.")));

                rpc.Discord_UpdatePresence(presence);

                try {
                    Thread.sleep(2000L);

                } catch (InterruptedException ignored) {

                }
            }
        }, "DiscordRPC-Callback-Handler");

        thread.start();
    }

    private void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }

        rpc.Discord_Shutdown();
    }
}