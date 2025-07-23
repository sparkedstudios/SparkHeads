package com.github.sparkedstudios.commands;

import com.github.sparkedstudios.commands.main.CommandWrapper;
import com.github.sparkedstudios.commands.main.SparkHeadCommand;
import com.github.sparkedstudios.commands.tabcompletion.SparkHeadTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class CommandRegister {

    public static void register(SparkHeadCommand executor) {
        try {
            SimplePluginManager manager = (SimplePluginManager) Bukkit.getServer().getPluginManager();
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(manager);

            CommandWrapper command = new CommandWrapper("sparkheads", executor, new SparkHeadTabCompleter(executor.getLoader()));
            commandMap.register("sparkheads", command);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}