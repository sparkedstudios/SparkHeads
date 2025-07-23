package com.github.sparkedstudios.handler;

import com.github.sparkedstudios.SparkHeads;
import com.github.sparkedstudios.data.HeadData;

import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class HeadHandler implements Listener {

    private final HeadManager manager;
    private final Map<String, ArmorStand> spawnedHeads = new HashMap<>();
    private final Map<String, BukkitRunnable> animationTasks = new HashMap<>();

    public HeadHandler(HeadManager manager) {
        this.manager = manager;
        Bukkit.getPluginManager().registerEvents(this, SparkHeads.getInstance());
    }

    public void spawnAll() {
        removeAll();
        for (HeadData head : manager.getAllHeads()) {
            spawn(head);
        }
    }

    public void spawn(HeadData head) {
        remove(head.getId());

        Location baseLocation = head.getLocation().clone();
        Location standLocation = baseLocation.clone().add(0, -1.25, 0);

        ArmorStand stand = (ArmorStand) baseLocation.getWorld().spawnEntity(standLocation, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setMarker(false);
        stand.setSmall(true);
        stand.setBasePlate(false);
        stand.setCustomNameVisible(false);
        stand.setPersistent(true);
        stand.setMetadata("sparkheads", new FixedMetadataValue(SparkHeads.getInstance(), true));

        if (head.getSkullType().equalsIgnoreCase("URL")) {
            stand.setHelmet(SkullCreator.itemFromUrl(head.getSkullValue()));
        } else if (head.getSkullType().equalsIgnoreCase("BASE64")) {
            stand.setHelmet(SkullCreator.itemFromBase64(head.getSkullValue()));
        }

        spawnedHeads.put(head.getId(), stand);

        if (head.isAnimationEnabled() || head.isParticleEnabled()) {
            BukkitRunnable task = new BukkitRunnable() {
                long startTime = System.currentTimeMillis();

                @Override
                public void run() {
                    if (!stand.isValid()) {
                        cancel();
                        return;
                    }

                    double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;

                    double floatY = Math.sin(elapsed * head.getFloatSpeed()) * head.getFloatDistance();
                    Location currentLoc = baseLocation.clone().add(0, floatY, 0);
                    stand.teleport(currentLoc);

                    double rotDirectionMultiplier = 1.0;
                    if (head.isRotateDirectionSwitch()) {
                        rotDirectionMultiplier = Math.signum(Math.sin(elapsed * head.getFloatSpeed()));
                    }
                    double rotationY = Math.sin(elapsed * head.getRotateSpeed()) * head.getRotateAmplitude() * rotDirectionMultiplier;
                    stand.setHeadPose(new EulerAngle(0, rotationY, 0));

                    if (head.isParticleEnabled()) {
                        double radius = head.getParticleRadius();
                        double particleAngle = elapsed * head.getParticleSpeed();
                        int particleCount = (int) Math.max(1, head.getParticleCount());

                        for (int i = 0; i < particleCount; i++) {
                            double angle = particleAngle + (2 * Math.PI / particleCount) * i;
                            double x = Math.cos(angle) * radius;
                            double z = Math.sin(angle) * radius;
                            Location particleLoc = currentLoc.clone().add(x, head.getParticleOffsetY(), z);

                            currentLoc.getWorld().spawnParticle(
                                    Particle.valueOf(head.getParticleType()),
                                    particleLoc, 1, 0, 0, 0, 0
                            );
                        }
                    }
                }
            };
            task.runTaskTimer(SparkHeads.getInstance(), 0L, 1L);
            animationTasks.put(head.getId(), task);
        }
    }

    public void remove(String id) {
        ArmorStand stand = spawnedHeads.remove(id);
        if (stand != null && !stand.isDead()) stand.remove();

        BukkitRunnable task = animationTasks.remove(id);
        if (task != null) task.cancel();
    }

    public void removeAll() {
        for (String id : new HashSet<>(spawnedHeads.keySet())) {
            remove(id);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand)) return;
        handleClick(event.getPlayer(), event.getRightClicked(), false);
    }

    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof ArmorStand)) return;

        event.setCancelled(true);
        handleClick((Player) event.getDamager(), event.getEntity(), true);
    }

    private void handleClick(Player player, Entity clicked, boolean isLeftClick) {
        for (Map.Entry<String, ArmorStand> entry : spawnedHeads.entrySet()) {
            if (entry.getValue().getUniqueId().equals(clicked.getUniqueId())) {
                HeadData head = manager.getHead(entry.getKey());

                if (isLeftClick) {
                    runActions(player, head.getLeftClickActions(), head.getId());
                } else {
                    runActions(player, head.getRightClickActions(), head.getId());
                }
                break;
            }
        }
    }

    private void runActions(Player player, List<String> actions, String id) {

        for (String action : actions) {
            if (action.startsWith("[MESSAGE] ")) {
                String msg = action.substring(10).replace("&", "§");
                player.sendMessage(msg);
            } else if (action.startsWith("[COMMAND] ")) {
                String cmd = action.substring(10);
                player.performCommand(cmd);
            } else if (action.startsWith("[CONSOLE] ")) {
                String cmd = action.substring(10).replace("%player%", player.getName()).replace("%id%", id);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } else if (action.startsWith("[SOUND] ")) {
                String sound = action.substring(8);
                try {
                    player.playSound(player.getLocation(), org.bukkit.Sound.valueOf(sound), 1.0f, 1.0f);
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }
}
