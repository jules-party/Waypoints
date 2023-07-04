package zoink.jule.waypoints.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.Permissions;
import zoink.jule.waypoints.Utils.TeleportUtils;

import java.io.File;

import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

public class WTp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (cmdSender instanceof Player) {
            Player player = (Player)cmdSender;
            World world;

            if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "You do not have permissions to execute this command!");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "No name given!");
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/wsave <name>");
                return true;
            }

            File waypointsFile = new File("waypoints/" + player.getUniqueId() + ".yml");
            FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointsFile);

            if (waypoints.get(args[0]) == null) {
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Waypoint doesn't exist!");
                return true;
            }

            double x, y, z;

            x = waypoints.getDouble(args[0] + ".coordinates.x");
            y = waypoints.getDouble(args[0] + ".coordinates.y");
            z = waypoints.getDouble(args[0] + ".coordinates.z");
            world = Bukkit.getWorld(waypoints.getString(args[0] + ".world"));

            Location location;
            location = new Location(world, x, y, z);
            TeleportUtils.teleportPlayer(player, location);
            player.sendMessage(CHAT_PREFIX + ChatColor.GREEN + "Teleported to waypoint: " + ChatColor.RESET + args[0]);
        }

        return true;
    }
}
