package com.github.sparkedstudios.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getName();
    String getSyntax();
    String getPermission();
    void perform(CommandSender sender, String[] args);
    List<String> getTabComplete(CommandSender sender, String[] args);
}