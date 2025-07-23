package com.github.sparkedstudios.clear;

import com.github.sparkedstudios.manager.HeadManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ArmorStand;

public class HeadCleaner {

    public static void cleanInvalidHeads(HeadManager manager) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand && entity.hasMetadata("sparkheads")) {
                    boolean matched = manager.getAllHeads().stream()
                            .anyMatch(head -> entity.getLocation().getBlock().equals(head.getLocation().getBlock()));

                    if (!matched) {
                        entity.remove();
                    }
                }
            }
        }
    }
}