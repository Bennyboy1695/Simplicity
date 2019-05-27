package io.github.bennyboy1695.simplicity;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.TextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Simplicity extends JavaPlugin {

    private Logger logger = LoggerFactory.getLogger("Simplicity");

    @Override
    public void onEnable() {
        try {
            this.getCommand("simplicity").setExecutor(new SimplicityCommand(this));
            this.getCommand("simplicity").setTabCompleter(new SimplicityCommand(this));
            getPluginLogger().info("Simplicity has been enabled!");
        } catch (Exception e) {
         getPluginLogger().error("Simplicity failed to enable!", e);
        }
    }

    @Override
    public void onDisable() {
        getPluginLogger().warn("Simplicity has been disabled!");
    }

    public Logger getPluginLogger() {
        return logger;
    }

    public class SimplicityCommand implements CommandExecutor, TabCompleter {

        private Simplicity plugin;

        public SimplicityCommand(Simplicity plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (command.getName().equalsIgnoreCase("simplicity")) {
                String[] actualArgs = quotesOrSpaceSplits(String.join(" ", args));
                if (args.length > 0) {
                    String message, sound = "";
                    int lengthInSeconds = 5 * 20;
                    switch (args[0].toLowerCase()) {
                        case "title-sound":
                            if (args.length > 2) {
                                message = actualArgs[1];
                                lengthInSeconds = Integer.valueOf(actualArgs[2]) * 20;
                                final Integer[] length = {lengthInSeconds};
                                final String[] finalMessage = {message};
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), "", 1.0f, 0.5f);
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', finalMessage[0]));
                                        }
                                        length[0]--;
                                        if (length[0] < 1) {
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                            }
                            break;
                        case "chat-sound":
                            if (args.length > 1) {
                                message = actualArgs[1];
                                lengthInSeconds = Integer.valueOf(actualArgs[2]) * 20;
                                final Integer[] length = {lengthInSeconds};
                                final String[] finalMessage = {message};
                                Bukkit.broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
                                }
                            }
                            break;
                        case "title-chat":
                            if (args.length > 2) {
                                message = actualArgs[1];
                                lengthInSeconds = Integer.valueOf(actualArgs[2]) * 20;
                                final Integer[] length = {lengthInSeconds};
                                final String[] finalMessage = {message};
                                Bukkit.broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', finalMessage[0]));
                                        }
                                        length[0]--;
                                        if (length[0] < 1) {
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                            }
                            break;
                        case "title-chat-sound":
                            if (args.length > 2) {
                                message = actualArgs[1];
                                lengthInSeconds = Integer.valueOf(actualArgs[2]) * 20;
                                final Integer[] length = {lengthInSeconds};
                                final String[] finalMessage = {message};
                                Bukkit.broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', finalMessage[0]));
                                        }
                                        length[0]--;
                                        if (length[0] < 1) {
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0, 1);
                            }
                            break;
                    }
                }
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (command.getName().equalsIgnoreCase("simplicity")) {
                return Arrays.asList("title-sound", "chat-sound", "title-chat", "title-chat-sound");
            }
            return null;
        }

        public String[] quotesOrSpaceSplits(String str) {
            str += " ";
            ArrayList<String> strings = new ArrayList<String>();
            boolean inQuote = false;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '"' || c == ' ' && !inQuote) {
                    if (c == '"')
                        inQuote = !inQuote;
                    if (!inQuote && sb.length() > 0) {
                        strings.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                } else
                    sb.append(c);
            }
            return strings.toArray(new String[strings.size()]);
        }
    }
}
