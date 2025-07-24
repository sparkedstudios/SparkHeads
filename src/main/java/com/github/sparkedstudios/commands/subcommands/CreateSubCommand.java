package com.github.sparkedstudios.commands.subcommands;

import com.github.sparkedstudios.SparkHeads;
import com.github.sparkedstudios.commands.SubCommand;
import com.github.sparkedstudios.config.SparkYML;
import com.github.sparkedstudios.manager.HeadManager;
import com.github.sparkedstudios.utils.MessagesUtils;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getSyntax() {
        return "/sparkheads create <id>";
    }

    @Override
    public String getPermission() {
        return "sparkheads.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MessagesUtils.NO_CONSOLE.send(sender);
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            MessagesUtils.USAGE.send(sender, "%syntax%", getSyntax());
            return;
        }

        String id = args[1];
        HeadManager manager = SparkHeads.getInstance().getHeadManager();

        if (manager.exists(id)) {
            MessagesUtils.HEAD_EXISTS.send(sender, "%id%", id);
            return;
        }

        Location loc = player.getLocation().getBlock().getLocation().add(0.5, 1, 0.5);
        SparkYML heads = Files.HEADS();

        String base = "floatingheads." + id;
        heads.set(base + ".location.world", loc.getWorld().getName());
        heads.set(base + ".location.x", loc.getX());
        heads.set(base + ".location.y", loc.getY());
        heads.set(base + ".location.z", loc.getZ());

        heads.set(base + ".skull.type", "URL");
        heads.set(base + ".skull.value", "http://textures.minecraft.net/texture/e7c9b26d0a4b26e97a47cca48eb264577a047f7dfe5c6b4d81a659135eebf97");

        heads.set(base + ".animation.enabled", true);
        heads.set(base + ".animation.float-distance", 0.5);
        heads.set(base + ".animation.float-speed", 1.5);
        heads.set(base + ".animation.rotate-speed", 2.0);
        heads.set(base + ".animation.rotate-amplitude", Math.toRadians(20));
        heads.set(base + ".animation.rotate-direction-switch", true);

        heads.set(base + ".particle.enabled", true);
        heads.set(base + ".particle.type", "VILLAGER_HAPPY");
        heads.set(base + ".particle.radius", 0.4);
        heads.set(base + ".particle.speed", 2.0);
        heads.set(base + ".particle.count", 5);
        heads.set(base + ".particle.offset-y", 0.25);

        heads.set(base + ".left-click", Collections.singletonList("[MESSAGE] &aHello from left click!"));
        heads.set(base + ".right-click", Arrays.asList(
                "[COMMAND] warp spawn",
                "[MESSAGE] &bTeleporting to spawn..."
        ));

        heads.save();
        manager.loadHeads();
        SparkHeads.getInstance().getHeadHandler().spawn(manager.getHead(id));

        MessagesUtils.HEAD_CREATED.send(sender, "%id%", id);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}