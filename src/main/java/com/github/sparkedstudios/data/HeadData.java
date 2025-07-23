package com.github.sparkedstudios.data;

import org.bukkit.Location;
import java.util.List;

public class HeadData {

    private final String id;
    private final Location location;
    private final String skullType;
    private final String skullValue;

    private final boolean animationEnabled;
    private final double floatDistance;
    private final double floatSpeed;
    private final double rotateSpeed;
    private final double rotateAmplitude;
    private final boolean rotateDirectionSwitch;

    private final boolean particleEnabled;
    private final String particleType;
    private final double particleOffsetY;
    private final double particleRadius;
    private final double particleSpeed;
    private final double particleCount;

    private final List<String> leftClickActions;
    private final List<String> rightClickActions;

    public HeadData(String id, Location location, String skullType, String skullValue,
                    boolean animationEnabled, double floatDistance, double floatSpeed,
                    double rotateSpeed, double rotateAmplitude, boolean rotateDirectionSwitch,
                    boolean particleEnabled, String particleType, double particleOffsetY,
                    double particleRadius, double particleSpeed, double particleCount,
                    List<String> leftClickActions, List<String> rightClickActions) {
        this.id = id;
        this.location = location;
        this.skullType = skullType;
        this.skullValue = skullValue;
        this.animationEnabled = animationEnabled;
        this.floatDistance = floatDistance;
        this.floatSpeed = floatSpeed;
        this.rotateSpeed = rotateSpeed;
        this.rotateAmplitude = rotateAmplitude;
        this.rotateDirectionSwitch = rotateDirectionSwitch;
        this.particleEnabled = particleEnabled;
        this.particleType = particleType;
        this.particleOffsetY = particleOffsetY;
        this.particleRadius = particleRadius;
        this.particleSpeed = particleSpeed;
        this.particleCount = particleCount;
        this.leftClickActions = leftClickActions;
        this.rightClickActions = rightClickActions;
    }

    public String getId() { return id; }
    public Location getLocation() { return location; }
    public String getSkullType() { return skullType; }
    public String getSkullValue() { return skullValue; }
    public boolean isAnimationEnabled() { return animationEnabled; }
    public double getFloatDistance() { return floatDistance; }
    public double getFloatSpeed() { return floatSpeed; }
    public double getRotateSpeed() { return rotateSpeed; }
    public double getRotateAmplitude() { return rotateAmplitude; }
    public boolean isRotateDirectionSwitch() { return rotateDirectionSwitch; }
    public boolean isParticleEnabled() { return particleEnabled; }
    public String getParticleType() { return particleType; }
    public double getParticleOffsetY() { return particleOffsetY; }
    public double getParticleRadius() { return particleRadius; }
    public double getParticleSpeed() { return particleSpeed; }
    public double getParticleCount() { return particleCount; }
    public List<String> getLeftClickActions() { return leftClickActions; }
    public List<String> getRightClickActions() { return rightClickActions; }
}