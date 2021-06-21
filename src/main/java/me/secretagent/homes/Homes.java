package me.secretagent.homes;

import me.secretagent.homes.commands.Commands;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Homes extends JavaPlugin {

    public static Homes homes = null;

    @Override
    public void onEnable() {
        homes = this;
        saveDefaultConfig();
        createHomeYaml();
        Commands commands = new Commands();
        getCommand("sethome").setExecutor(commands);
        getCommand("warphome").setExecutor(commands);
        getCommand("allowguest").setExecutor(commands);
        getCommand("visit").setExecutor(commands);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createHomeYaml() {
        try {
            File file = new File(getDataFolder() + File.separator + "homes.yml");
            file.createNewFile();
        } catch (IOException e) {
            getLogger().info("homes.yml creation triggered an IO Exception");
        }
    }
}
