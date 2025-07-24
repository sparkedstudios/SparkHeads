package com.github.sparkedstudios.commands.subcommands;

import com.github.sparkedstudios.SparkHeads;
import com.github.sparkedstudios.commands.SubCommand;
import com.github.sparkedstudios.handler.HeadHandler;
import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.MessagesUtils;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DeleteSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getSyntax() {
        return "/sparkheads delete <id>";
    }

    @Override
    public String getPermission() {
        return "sparkheads.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
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

        Files.HEADS().set("floatingheads." + id, null);
        Files.HEADS().save();

        handler.removeAll();
        manager.loadHeads();
        handler.spawnAll();

        MessagesUtils.HEAD_DELETED.send(sender, "%id%", id);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}