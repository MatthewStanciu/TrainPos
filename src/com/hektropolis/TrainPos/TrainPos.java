package com.hektropolis.TrainPos;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mattbstanciu on 6/12/17.
 */
public class TrainPos extends JavaPlugin {

    public static FileConfiguration config;
    public static TrainPos plugin;
    Logger log = this.getLogger();
    WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

    public void onEnable() {
        plugin = this;
        TrainPos.config = this.getConfig();

        saveDefaultConfig();
        getConfig().addDefault("boards", null);
        saveConfig();
        reloadConfig();

        getWorldEdit();

        getCommand("setboard").setExecutor(new Commands());
        getCommand("delboard").setExecutor(new Commands());
        getCommand("trainpos").setExecutor(new Commands());
    }

    public void getWorldEdit() {
        if (we == null) {
            log.log(Level.SEVERE, "This plugin requires WorldEdit 6.1.5 or above.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @SuppressWarnings("deprecation") //getData and setData are deprecated but there is no un-deprecated method afaik
    public void setBlocks(String color) {
        for (String key : getConfig().getKeys(false)) {
            double minX = getConfig().getDouble("blocks." + key + ".minX");
            double minY = getConfig().getDouble("blocks." + key + ".minY");
            double minZ = getConfig().getDouble("blocks." + key + ".minZ");
            double maxX = getConfig().getDouble("blocks." + key + ".maxX");
            double maxY = getConfig().getDouble("blocks." + key + ".maxY");
            double maxZ = getConfig().getDouble("blocks." + key + ".maxZ");
            Location min = new Location(getServer().getWorld("hektor_city"), minX, minY, minZ);
            Location max = new Location(getServer().getWorld("hektor_city"), maxX, maxY, maxZ);

            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                        if (maxX - minX == 0) {
                            x = (int)minX;
                        } else if (maxZ - minZ == 0) {
                            z = (int)minZ;
                        }
                        Block posBlock = getServer().getWorld("hektor_city").getBlockAt(x, y, z);
                        byte data = posBlock.getData();

                        switch (color) {
                            case "blue":
                                data = 3;
                                break;
                            case "orange":
                                data = 1;
                                break;
                            case "green":
                                data = 5;
                                break;
                            case "red":
                                data = 14;
                                break;
                            default:
                                log.log(Level.WARNING, "Command sender tried to set a TrainPos block that doesn't exist!");
                                break;
                        }
                        posBlock.setType(Material.STAINED_GLASS);
                        posBlock.setData(data);
                    }
                }
            }
        }
    }
}
