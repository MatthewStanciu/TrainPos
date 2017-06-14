package com.hektropolis.TrainPos;

import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by mattbstanciu on 6/12/17.
 */
public class Commands implements CommandExecutor {

    private final TrainPos plugin;

    public Commands(TrainPos plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("trainpos." + cmd.getName()))) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("setboard")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Setting boards requires you to be a player!");
                return false;
            }
            Selection s =  plugin.we.getSelection((Player) sender);
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /setboard <number>");
                return false;
            } else {
                if (s == null) {
                    sender.sendMessage(ChatColor.RED + "You must define a selection first!");
                    return false;
                } else if (plugin.getConfig().contains(args[0])) {
                    sender.sendMessage(ChatColor.RED + "Board " + args[0] + " already exists!");
                    return false;
                } else {
                    plugin.getConfig().set(args[0] + ".minX", s.getMinimumPoint().getX());
                    plugin.getConfig().set(args[0] + ".minY", s.getMinimumPoint().getY());
                    plugin.getConfig().set(args[0] + ".minZ", s.getMinimumPoint().getZ());
                    plugin.getConfig().set(args[0] + ".maxX", s.getMaximumPoint().getX());
                    plugin.getConfig().set(args[0] + ".maxY", s.getMaximumPoint().getY());
                    plugin.getConfig().set(args[0] + ".maxZ", s.getMaximumPoint().getZ());
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.AQUA + "Board " + args[0] + " set.");
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("delboard")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /delboard <number>");
                return false;
            } else if (!(plugin.getConfig().contains(args[0]))) {
                sender.sendMessage(ChatColor.RED + "Board " + args[0] + " does not exist!");
                return false;
            } else {
                plugin.getConfig().set(args[0], null);
                plugin.saveConfig();
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "Board " + args[0] + " removed.");
            }
        }

        if (cmd.getName().equalsIgnoreCase("trainpos")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /trainpos <color>");
                return false;
            } else {
                plugin.setBlocks(args[0]);
            }
        }

        if (cmd.getName().equalsIgnoreCase("trainreload")) {
            plugin.saveConfig();
            plugin.reloadConfig();
        }

        return true;
    }
}
