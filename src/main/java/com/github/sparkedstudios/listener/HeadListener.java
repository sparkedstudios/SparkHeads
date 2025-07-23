package com.github.sparkedstudios.listener;

import com.github.sparkedstudios.SparkHeads;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class HeadListener implements Listener {

    public HeadListener() {
        SparkHeads.getInstance().getServer().getPluginManager().registerEvents(this, SparkHeads.getInstance());
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent event) {
        if (event.getRightClicked().hasMetadata("sparkheads")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand stand = (ArmorStand) event.getEntity();
            if (stand.hasMetadata("sparkheads")) {
                event.setCancelled(true);
            }
        }
    }
}