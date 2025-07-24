package com.github.sparkedstudios.commands.tabcompletion;

import com.github.sparkedstudios.commands.CommandLoader;
import com.github.sparkedstudios.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SparkHeadTabCompleter implements TabCompleter {

    private final CommandLoader loader;

    public SparkHeadTabCompleter(CommandLoader loader) {
        this.loader = loader;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (SubCommand sub : loader.getAll()) {
                if (sender.hasPermission(sub.getPermission())) {
                    suggestions.add(sub.getName());
                }
            }
            return suggestions;
        }

        SubCommand sub = loader.getSubCommand(args[0]);
        if (sub != null && sender.hasPermission(sub.getPermission())) {
            return sub.getTabComplete(sender, args);
        }

        return Collections.emptyList();
    }
}
