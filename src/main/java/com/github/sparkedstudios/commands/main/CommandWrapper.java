package com.github.sparkedstudios.commands.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;

public class CommandWrapper extends Command {

    private final SparkHeadCommand executor;
    private final TabCompleter completer;

    public CommandWrapper(String name, SparkHeadCommand executor, TabCompleter completer) {
        super(name);
        this.executor = executor;
        this.completer = completer;
        this.setPermission("sparkheads.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return executor.onCommand(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return completer.onTabComplete(sender, this, alias, args);
    }
}
