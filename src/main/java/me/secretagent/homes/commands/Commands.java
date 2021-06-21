package me.secretagent.homes.commands;

import me.secretagent.homes.Homes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor {
    Homes plugin = Homes.homes;
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "homes.yml"));
    File file = new File(plugin.getDataFolder() + File.separator + "homes.yml");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("sethome")) {
                config.set(player.getUniqueId().toString() + ".location", player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Your home's location was set to your location.");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cmd.getName().equalsIgnoreCase("warphome")) {
                if (config.contains(player.getUniqueId().toString() + ".location")) {
                    player.teleport(config.getLocation(player.getUniqueId().toString() + ".location"));
                    player.sendMessage(ChatColor.GREEN + "Teleported you to your home.");
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have a home set!");
                }
            }
            else if (cmd.getName().equalsIgnoreCase("allowguest")) {
                if (args.length >= 1) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (!config.contains(offlinePlayer.getUniqueId().toString() + ".canvisit")) {
                        List<String> list = new ArrayList<String>();
                        list.add(player.getUniqueId().toString());
                        config.set(offlinePlayer.getUniqueId().toString() + ".canvisit", list);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        List<String> list = config.getStringList(offlinePlayer.getUniqueId().toString() + ".canvisit");
                        list.add(player.getUniqueId().toString());
                        config.set(offlinePlayer.getUniqueId().toString() + ".canvisit", list);
                        try {
                            config.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + offlinePlayer.getName() + " can now visit your home!");
                } else {
                    player.sendMessage(ChatColor.RED + "Specify a player to allow to warp to your home!");
                }
            } else if (cmd.getName().equalsIgnoreCase("visit")) {
                if (args.length >= 1) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (config.getStringList(player.getUniqueId().toString() + ".canvisit").contains(offlinePlayer.getUniqueId().toString())) {
                        if (config.contains(player.getUniqueId().toString() + ".location")) {
                            player.teleport(config.getLocation(player.getUniqueId().toString() + ".location"));
                        } else {
                            player.sendMessage(ChatColor.RED + "This player has not setup a home!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not allowed to visit this player's home!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Specify the player you want to visit!");
                }
            }
        }
        return true;
    }
}
