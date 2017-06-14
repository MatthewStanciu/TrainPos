package com.hektropolis.TrainPos;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mattbstanciu on 6/12/17.
 */
public class TrainPos extends JavaPlugin {

    Logger log = this.getLogger();
    WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

    public void onEnable() {
        this.saveDefaultConfig();
        this.saveConfig();
        this.reloadConfig();

        getWorldEdit();

        getCommand("setboard").setExecutor(new Commands(this));
        getCommand("delboard").setExecutor(new Commands(this));
        getCommand("trainpos").setExecutor(new Commands(this));
    }

    public void getWorldEdit() {
        if (we == null) {
            log.log(Level.SEVERE, "This plugin requires WorldEdit 6.1.5 or above.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @SuppressWarnings("deprecation") //getData and setData are deprecated but there is no un-deprecated method afaik
    public void getBlocks(String color, int x, int y, int z) {
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
            case "reset":
                data = 0;
                break;
            default:
                break;
        }

        if (data != (byte)0) {
            posBlock.setType(Material.STAINED_GLASS);
            posBlock.setData(data);
        } else {
            posBlock.setType(Material.ICE);
        }
    }

    public void setBlocks(String color) { //todo serialize location
        for (String key : this.getConfig().getKeys(false)) {
            double minX = this.getConfig().getDouble(key + ".minX");
            double minY = this.getConfig().getDouble(key + ".minY");
            double minZ = this.getConfig().getDouble(key + ".minZ");
            double maxX = this.getConfig().getDouble(key + ".maxX");
            double maxY = this.getConfig().getDouble(key + ".maxY");
            double maxZ = this.getConfig().getDouble(key + ".maxZ");
            Location min = new Location(getServer().getWorld("hektor_city"), minX, minY, minZ);
            Location max = new Location(getServer().getWorld("hektor_city"), maxX, maxY, maxZ);

            if (maxX - minX == 0) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                        getBlocks(color, (int)minX, y , z);
                    }
                }
            } else if (maxZ - minZ == 0) {
                for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                    for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                        getBlocks(color, x, y, (int)minZ);
                    }
                }
            }
        }
    }
}
