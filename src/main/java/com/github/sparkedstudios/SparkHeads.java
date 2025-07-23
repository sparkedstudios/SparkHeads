package com.github.sparkedstudios;

import com.github.sparkedstudios.clear.HeadCleaner;
import com.github.sparkedstudios.commands.CommandRegister;
import com.github.sparkedstudios.commands.main.SparkHeadCommand;
import com.github.sparkedstudios.handler.HeadHandler;
import com.github.sparkedstudios.listener.HeadListener;
import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.plugin.java.JavaPlugin;

public final class SparkHeads extends JavaPlugin {

    private static SparkHeads instance;
    private HeadManager headManager;
    private HeadHandler headHandler;

    public static SparkHeads getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Files.init(this);

        headManager = new HeadManager();
        headHandler = new HeadHandler(headManager);

        headManager.loadHeads();
        HeadCleaner.cleanInvalidHeads(headManager);
        headHandler.spawnAll();

        new HeadListener();

        CommandRegister.register(new SparkHeadCommand());
    }

    @Override
    public void onDisable() {
        headHandler.removeAll();
        headManager.saveHeads();
    }

    public HeadManager getHeadManager() {
        return headManager;
    }

    public HeadHandler getHeadHandler() {
        return headHandler;
    }
}