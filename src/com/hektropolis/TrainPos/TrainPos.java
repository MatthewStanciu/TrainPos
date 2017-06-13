package com.hektropolis.TrainPos;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
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

    @SuppressWarnings("deprecation") //getData and setData are deprecated but there is no un-deprecated method
    public void getBlocks(String color) {
        //List<Block> blocks = new ArrayList<>();
        for (String key : getConfig().getKeys(false)) {
            double minX = getConfig().getDouble("blocks." + key + ".minX");
            double minY = getConfig().getDouble("blocks." + key + ".minY");
            double minZ = getConfig().getDouble("blocks." + key + ".minZ");
            double maxX = getConfig().getDouble("blocks." + key + ".maxX");
            double maxY = getConfig().getDouble("blocks." + key + ".maxY");
            double maxZ = getConfig().getDouble("blocks." + key + ".maxZ");
            Location min = new Location(getServer().getWorld("hektor_city"), minX, minY, minZ);
            Location max = new Location(getServer().getWorld("hektor_city"), maxX, maxY, maxZ);

            /*int minBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
            int maxBlockX = (loc1.getBlockY() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
            int minBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
            int maxBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
            int minBlockZ = (loc1.getBlockX() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
            int maxBlockZ = (loc1.getBlockX() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());*/

            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                        Block posBlock = getServer().getWorld("hektor_city").getBlockAt(x, y, z);
                        byte data = posBlock.getData();
                        //posBlock.setType(Material.STAINED_GLASS);
                        log.log(Level.WARNING, "got data");
                        log.log(Level.WARNING, posBlock.toString());
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
                                //posBlock.setType(Material.ICE);
                                break;
                        }
                        //blocks.add(posBlock);
                        posBlock.setType(Material.STAINED_GLASS); //maybe it's only setting in the list and not in the game?
                        posBlock.setData(data);
                        //b.setData(b.getData());
                        TrainPos.plugin.log.log(Level.WARNING, "Set data and everything");
                        TrainPos.plugin.log.log(Level.WARNING, posBlock.toString());
                        log.log(Level.WARNING, "added block");
                        log.log(Level.WARNING, posBlock.toString());
                    }
                }
            }

            /*for (double x = minX; x <= maxX; x++) {
                for (double y = minY; y <= maxY; y++) {
                    for (double z = minZ; z <= maxZ; z++) {
                        Location blockLoc = new Location(getServer().getWorld("hektor_city"), x, y, z);
                        blocks.add(blockLoc.getBlock());
                    }
                }
            }*/
            /*for (int i = minBlockX; i <= maxBlockX; i++) {
                for (int j = minBlockY; j <= maxBlockY; j++) {
                    for (int k = minBlockZ; k <= maxBlockZ; k++) {
                        Block block = loc1.getWorld().getBlockAt(i, j, k);
                        blocks.add(block);
                    }
                }
            }*/
        }
        //log.log(Level.WARNING, "Got blocks");
        //return blocks;
    }
}
