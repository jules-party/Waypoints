package zoink.jule.waypoints.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import zoink.jule.waypoints.Commands.WSave;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import zoink.jule.waypoints.Utils.Permissions;
import zoink.jule.waypoints.Utils.TeleportUtils;

import java.io.File;

public class WTp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (cmdSender instanceof Player) {
            Player player = (Player)cmdSender;
            World world;

            if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
                player.sendMessage(ChatColor.RED + "You do not have permissions to execute this command!");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "No name given!");
                player.sendMessage(ChatColor.RED + "/wsave <name>");
                return true;
            }

            File waypointsFile = new File("waypoints/" + player.getName() + ".yml");
            FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointsFile);

            if (waypoints.get(args[0]) == null) {
                player.sendMessage(ChatColor.RED + "Waypoint doesn't exist!");
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
            player.sendMessage(ChatColor.GREEN + "Teleported to waypoint: " + ChatColor.RESET + args[0]);
        }

        return true;
    }
}
