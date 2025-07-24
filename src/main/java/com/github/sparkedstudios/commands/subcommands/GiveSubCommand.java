package com.github.sparkedstudios.commands.subcommands;

import com.github.sparkedstudios.commands.SubCommand;
import com.github.sparkedstudios.utils.MessagesUtils;
import com.github.sparkedstudios.utils.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiveSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getSyntax() {
        return "/sparkheads give <player|texture|base64> <value>";
    }

    @Override
    public String getPermission() {
        return "sparkheads.give";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            MessagesUtils.NO_CONSOLE.send(sender);
            return;
        }

        if (args.length < 3) {
            MessagesUtils.USAGE.send(sender);
            return;
        }

        String type = args[1];
        String value = args[2];
        ItemStack head = null;

        switch (type.toLowerCase()) {
            case "player":
                head = SkullCreator.itemFromName(value);
                break;
            case "texture":
                head = SkullCreator.itemFromUrl(value);
                break;
            case "base64":
                head = SkullCreator.itemFromBase64(value);
                break;
            default:
                MessagesUtils.USAGE.send(sender);
                return;
        }

        player.getInventory().addItem(head);
        MessagesUtils.GIVE.send(sender);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("player", "texture", "base64");
        } else if (args.length == 3 && args[1].equalsIgnoreCase("player")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
