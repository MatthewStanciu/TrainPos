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

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (!(p.hasPermission("trainpos." + cmd.getName()))) {
            p.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("setboard")) {
            Selection s =  TrainPos.plugin.we.getSelection(p);
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Usage: /setboard <number>");
                return false;
            } else {
                if (s == null) {
                    p.sendMessage(ChatColor.RED + "You must define a selection first!");
                    return false;
                } else if (TrainPos.plugin.getConfig().contains("boards." + args[0])) {
                    p.sendMessage(ChatColor.RED + "Board " + args[0] + " already exists!");
                    return false;
                } else {
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".minX", s.getMinimumPoint().getX());
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".minY", s.getMinimumPoint().getY());
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".minZ", s.getMinimumPoint().getZ());
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".maxX", s.getMaximumPoint().getX());
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".maxY", s.getMaximumPoint().getY());
                    TrainPos.config.set("boards." + Integer.parseInt(args[0]) + ".maxZ", s.getMaximumPoint().getZ());
                    TrainPos.plugin.saveConfig();
                    TrainPos.plugin.reloadConfig();
                    p.sendMessage(ChatColor.AQUA + "Board " + args[0] + " set.");
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("delboard")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Usage: /delboard <number>");
                return false;
            } else if (!(TrainPos.plugin.getConfig().contains("boards." + args[0]))) {
                p.sendMessage(ChatColor.RED + "Board " + args[0] + " does not exist!");
                return false;
            } else {
                TrainPos.plugin.getConfig().set("boards." + Integer.parseInt(args[0]), null);
                TrainPos.plugin.saveConfig();
                TrainPos.plugin.reloadConfig();
                p.sendMessage(ChatColor.AQUA + "Board " + args[0] + " removed.");
            }
        }

        if (cmd.getName().equalsIgnoreCase("trainpos")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /trainpos <color>");
                return false;
            } else {
                TrainPos.plugin.setBlocks(args[0]);
            }
        }

        return true;
    }
}
