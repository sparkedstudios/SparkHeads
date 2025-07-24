package com.github.sparkedstudios.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.sparkedstudios.SparkHeads;
import com.github.sparkedstudios.data.HeadData;
import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.SkullCreator;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.*;

public class TestPacketHead extends PacketListenerCommon implements PacketListener {

    private final HeadManager manager;
    private final Map<String, ArmorStand> spawnedHeads = new HashMap<>();
    private final Map<String, BukkitRunnable> animationTasks = new HashMap<>();
    private final Map<Integer, String> entityIdToHeadId = new HashMap<>();

    public TestPacketHead(HeadManager manager) {
        this.manager = manager;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    public void spawnAll() {
        removeAll();
        for (HeadData head : manager.getAllHeads()) {
            spawn(head);
        }
    }

    public void spawn(HeadData head) {
        remove(head.getId());

        Location baseLocation = head.getLocation();
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
        entityIdToHeadId.put(stand.getEntityId(), head.getId());

        if (head.isAnimationEnabled() || head.isParticleEnabled()) {
            BukkitRunnable task = new BukkitRunnable() {
                final long startTime = System.currentTimeMillis();
                double lastY = 0;

                @Override
                public void run() {
                    if (!stand.isValid()) {
                        cancel();
                        return;
                    }

                    double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;

                    // Floating
                    double floatY = Math.sin(elapsed * head.getFloatSpeed()) * head.getFloatDistance();
                    if (Math.abs(floatY - lastY) > 0.01) {
                        Location newLoc = baseLocation.clone().add(0, floatY, 0);
                        stand.teleport(newLoc);
                        lastY = floatY;
                    }

                    // Rotation
                    double rotationY = Math.sin(elapsed * head.getRotateSpeed()) * head.getRotateAmplitude();
                    if (head.isRotateDirectionSwitch()) {
                        rotationY *= Math.signum(Math.sin(elapsed * head.getFloatSpeed()));
                    }
                    stand.setHeadPose(new EulerAngle(0, rotationY, 0));

                    // Particles
                    if (head.isParticleEnabled()) {
                        World world = baseLocation.getWorld();
                        double radius = head.getParticleRadius();
                        double particleAngle = elapsed * head.getParticleSpeed();
                        int particleCount = (int) Math.max(1, head.getParticleCount());

                        for (int i = 0; i < particleCount; i++) {
                            double angle = particleAngle + (2 * Math.PI / particleCount) * i;
                            double x = Math.cos(angle) * radius;
                            double z = Math.sin(angle) * radius;
                            Location particleLoc = baseLocation.clone().add(x, head.getParticleOffsetY(), z);
                            world.spawnParticle(
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
        if (stand != null && !stand.isDead()) {
            entityIdToHeadId.remove(stand.getEntityId());
            stand.remove();
        }

        BukkitRunnable task = animationTasks.remove(id);
        if (task != null) task.cancel();
    }

    public void removeAll() {
        for (String id : new HashSet<>(spawnedHeads.keySet())) {
            remove(id);
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
        WrapperPlayClientInteractEntity.InteractAction action = wrapper.getAction();

        if (action != WrapperPlayClientInteractEntity.InteractAction.ATTACK && action != WrapperPlayClientInteractEntity.InteractAction.INTERACT) return;

        Player player = event.getPlayer();
        int entityId = wrapper.getEntityId();

        String headId = entityIdToHeadId.get(entityId);
        if (headId == null) return;

        HeadData head = manager.getHead(headId);
        if (head == null) return;

        List<String> actions = (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK)
                ? head.getLeftClickActions()
                : head.getRightClickActions();

        runActions(player, actions, headId);
    }

    private void runActions(Player player, List<String> actions, String id) {
        for (String action : actions) {
            try {
                if (action.startsWith("[MESSAGE] ")) {
                    String msg = action.substring(10).replace("&", "§");
                    player.sendMessage(msg);
                } else if (action.startsWith("[COMMAND] ")) {
                    player.performCommand(action.substring(10));
                } else if (action.startsWith("[CONSOLE] ")) {
                    String cmd = action.substring(10)
                            .replace("%player%", player.getName())
                            .replace("%id%", id);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                } else if (action.startsWith("[SOUND] ")) {
                    String sound = action.substring(8);
                    player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);
                }
            } catch (Exception ignored) {}
        }
    }
}
