package com.github.sparkedstudios.manager;

import com.github.sparkedstudios.config.SparkYML;
import com.github.sparkedstudios.data.HeadData;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class HeadManager {

    private final Map<String, HeadData> heads = new HashMap<>();

    public void loadHeads() {
        heads.clear();

        SparkYML headsFile = Files.HEADS();
        if (!headsFile.contains("floatingheads")) {
            headsFile.set("floatingheads", null);
            headsFile.save();
            return;
        }

        ConfigurationSection section = headsFile.getConfig().getConfigurationSection("floatingheads");
        if (section == null) return;

        for (String id : section.getKeys(false)) {
            ConfigurationSection headSec = section.getConfigurationSection(id);
            if (headSec == null) continue;

            Location loc = new Location(
                    Bukkit.getWorld(headSec.getString("location.world")),
                    headSec.getDouble("location.x"),
                    headSec.getDouble("location.y"),
                    headSec.getDouble("location.z")
            );

            HeadData head = new HeadData(
                    id,
                    loc,
                    headSec.getString("skull.type"),
                    headSec.getString("skull.value"),
                    headSec.getBoolean("animation.enabled"),
                    headSec.getDouble("animation.float-distance"),
                    headSec.getDouble("animation.float-speed", 1.5),
                    headSec.getDouble("animation.rotate-speed", 2.0),
                    headSec.getDouble("animation.rotate-amplitude", Math.toRadians(20)),
                    headSec.getBoolean("animation.rotate-direction-switch", true),
                    headSec.getBoolean("particle.enabled"),
                    headSec.getString("particle.type"),
                    headSec.getDouble("particle.radius", 0.4),
                    headSec.getDouble("particle.speed", 2.0),
                    headSec.getInt("particle.count", 5),
                    headSec.getDouble("particle.offset-y", 0.25),
                    headSec.getStringList("left-click"),
                    headSec.getStringList("right-click")
            );

            heads.put(id, head);
        }
    }

    public void saveHeads() {
        Files.HEADS().save();
    }

    public HeadData getHead(String id) {
        return heads.get(id);
    }

    public Collection<HeadData> getAllHeads() {
        return heads.values();
    }

    public boolean exists(String id) {
        return heads.containsKey(id);
    }

    public Set<String> getAllHeadIds() {
        return new HashSet<>(heads.keySet());
    }
}
