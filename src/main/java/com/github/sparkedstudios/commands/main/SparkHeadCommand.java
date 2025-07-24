package com.github.sparkedstudios.commands.main;

import com.github.sparkedstudios.commands.CommandLoader;
import com.github.sparkedstudios.commands.SubCommand;
import com.github.sparkedstudios.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SparkHeadCommand implements CommandExecutor {

    private final CommandLoader loader = new CommandLoader();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            MessagesUtils.USAGE.send(sender);
            return true;
        }

        SubCommand sub = loader.getSubCommand(args[0]);

        if (sub == null) {
            MessagesUtils.SUBCOMMANDNOTFOUND.send(sender);
            return true;
        }

        if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            MessagesUtils.NOPERMISSION.send(sender);
            return true;
        }

        sub.perform(sender, args);
        return true;
    }

    public CommandLoader getLoader() {
        return loader;
    }
}