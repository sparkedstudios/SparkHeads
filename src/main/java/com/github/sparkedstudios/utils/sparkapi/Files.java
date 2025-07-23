package com.github.sparkedstudios.utils.sparkapi;

import com.github.sparkedstudios.config.SparkConfig;
import com.github.sparkedstudios.config.SparkFileLoader;
import com.github.sparkedstudios.config.SparkYML;
import com.github.sparkedstudios.config.annotations.FileType;
import com.github.sparkedstudios.config.annotations.SparkFile;
import org.bukkit.plugin.Plugin;

public class Files {

    @SparkFile(name = "config", type = FileType.CONFIG)
    private static SparkConfig CONFIG;

    @SparkFile(name = "messages", type = FileType.MESSAGES)
    private static SparkConfig MESSAGES;

    @SparkFile(name = "heads", type = FileType.GENERIC)
    private static SparkYML HEADS;

    public static void init(Plugin plugin) {
        SparkFileLoader.load(plugin, Files.class, "1.0", "1.0");
    }

    public static SparkConfig CONFIG() {
        return CONFIG;
    }

    public static SparkConfig MESSAGES() {
        return MESSAGES;
    }

    public static SparkYML HEADS() {
        return HEADS;
    }
}