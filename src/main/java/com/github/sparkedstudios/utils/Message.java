package com.github.sparkedstudios.utils;

import com.github.sparkedstudios.config.SparkConfig;
import com.github.sparkedstudios.utils.sparkapi.Files;
import org.bukkit.command.CommandSender;

public class Message {

    private final String path;

    public Message(String path) {
        this.path = path;
    }

    private static SparkConfig config() {
        return Files.MESSAGES();
    }

    public void send(CommandSender sender) {
        Object raw = config().get(path);

        if (raw instanceof String) {
            sender.sendMessage(ColorUtils.Format((String) raw));
        } else if (raw instanceof Iterable) {
            for (Object line : (Iterable<?>) raw) {
                if (line != null) {
                    sender.sendMessage(ColorUtils.Format(line.toString()));
                }
            }
        } else {
            sender.sendMessage("§cMessage not found: " + path);
        }
    }

    public void send(CommandSender sender, String placeholder, String replacement) {
        send(sender, new String[]{placeholder, replacement});
    }

    public void send(CommandSender sender, String... replacements) {
        Object raw = config().get(path);

        if (raw instanceof String) {
            String msg = applyPlaceholders((String) raw, replacements);
            sender.sendMessage(ColorUtils.Format(msg));
        } else if (raw instanceof Iterable) {
            for (Object line : (Iterable<?>) raw) {
                if (line != null) {
                    String msg = applyPlaceholders(line.toString(), replacements);
                    sender.sendMessage(ColorUtils.Format(msg));
                }
            }
        } else {
            sender.sendMessage("§cMessage not found: " + path);
        }
    }

    public String asString() {
        Object raw = config().get(path);
        if (raw instanceof String) {
            return ColorUtils.Format((String) raw);
        } else if (raw instanceof Iterable) {
            StringBuilder sb = new StringBuilder();
            for (Object line : (Iterable<?>) raw) {
                if (line != null) {
                    sb.append(ColorUtils.Format(line.toString())).append("\n");
                }
            }
            return sb.toString().trim();
        }
        return "§cMessage not found: " + path;
    }

    private String applyPlaceholders(String text, String... replacements) {
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            text = text.replace(replacements[i], replacements[i + 1]);
        }
        return text;
    }
}