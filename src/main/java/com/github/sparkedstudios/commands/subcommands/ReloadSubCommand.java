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

public class ReloadSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getSyntax() {
        return "/sparkheads reload";
    }

    @Override
    public String getPermission() {
        return "sparkheads.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        HeadManager manager = SparkHeads.getInstance().getHeadManager();
        HeadHandler handler = SparkHeads.getInstance().getHeadHandler();

        Files.HEADS().reload();
        Files.MESSAGES().reload();

        handler.removeAll();
        manager.loadHeads();
        handler.spawnAll();

        MessagesUtils.RELOADED.send(sender);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}