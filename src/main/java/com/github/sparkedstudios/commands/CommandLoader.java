package com.github.sparkedstudios.commands;

import com.github.sparkedstudios.commands.subcommands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CommandLoader {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandLoader() {
        registerSubCommand(new CreateSubCommand());
        registerSubCommand(new DeleteSubCommand());
        registerSubCommand(new MoveHereSubCommand());
        registerSubCommand(new ReloadSubCommand());
    }

    public void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    public SubCommand getSubCommand(String name) {
        return subCommands.get(name.toLowerCase());
    }

    public Collection<SubCommand> getAll() {
        return subCommands.values();
    }
}
