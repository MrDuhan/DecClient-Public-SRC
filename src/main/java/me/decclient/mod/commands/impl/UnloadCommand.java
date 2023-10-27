package me.decclient.mod.commands.impl;

import me.decclient.Dec;
import me.decclient.mod.commands.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Dec.unload(true);
    }
}

