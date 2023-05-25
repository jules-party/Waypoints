package zoink.jule.waypoints.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.yaml.snakeyaml.Yaml;
import zoink.jule.waypoints.Utils.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class WSave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {
        if (cmdSender instanceof Player) {
            Player player = (Player) cmdSender;
            if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
                player.sendMessage(ChatColor.RED + "You do not have permissions to execute this command!");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "No name given!");
                player.sendMessage(ChatColor.RED + "/wsave <name>");
                return true;
            }

            if (args[0].contains(".")) {
                player.sendMessage(ChatColor.RED + "Waypoint can not contain a period!");
                player.sendMessage(ChatColor.RED + "Try changing it to: " + ChatColor.RESET + args[0].replace('.', '_'));
                return true;
            }

            File waypointFile = new File("waypoints/" + player.getName() + ".yml");
            FileConfiguration waypoints;

            if (!waypointFile.exists() || !waypointFile.isFile()) {
                try {
                    waypointFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            waypoints = YamlConfiguration.loadConfiguration(waypointFile);

            try {
                waypoints.load(waypointFile);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }

            // Save to File
            Location location = player.getLocation();

            waypoints.set(args[0] + ".coordinates.x", location.getX());
            waypoints.set(args[0] + ".coordinates.y", location.getY());
            waypoints.set(args[0] + ".coordinates.z", location.getZ());
            waypoints.set(args[0] + ".world", location.getWorld().getName());

            try {
                waypoints.save(waypointFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage(ChatColor.GREEN + "Saved Waypoint!");
        }
        return true;
    }
}
