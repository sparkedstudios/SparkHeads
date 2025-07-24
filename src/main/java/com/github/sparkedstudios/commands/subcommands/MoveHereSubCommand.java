package com.github.sparkedstudios.commands.subcommands;

import com.github.sparkedstudios.SparkHeads;
import com.github.sparkedstudios.commands.SubCommand;
import com.github.sparkedstudios.handler.HeadHandler;
import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.MessagesUtils;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MoveHereSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "movehere";
    }

    @Override
    public String getSyntax() {
        return "/sparkheads movehere <id>";
    }

    @Override
    public String getPermission() {
        return "sparkheads.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessagesUtils.NO_CONSOLE.send(sender);
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            MessagesUtils.USAGE.send(sender, "%syntax%", getSyntax());
            return;
        }

        String id = args[1];
        HeadManager manager = SparkHeads.getInstance().getHeadManager();
        HeadHandler handler = SparkHeads.getInstance().getHeadHandler();

        if (!manager.exists(id)) {
            MessagesUtils.HEAD_NOT_FOUND.send(sender);
            return;
        }

        Location loc = player.getLocation().getBlock().getLocation().add(0.5, 1, 0.5);

        Files.HEADS().set("floatingheads." + id + ".location.world", loc.getWorld().getName());
        Files.HEADS().set("floatingheads." + id + ".location.x", loc.getX());
        Files.HEADS().set("floatingheads." + id + ".location.y", loc.getY());
        Files.HEADS().set("floatingheads." + id + ".location.z", loc.getZ());

        Files.HEADS().save();

        handler.removeAll();
        manager.loadHeads();
        handler.spawnAll();

        MessagesUtils.HEAD_MOVED.send(sender, "%id%", id);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}