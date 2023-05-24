package zoink.jule.waypoints.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import zoink.jule.waypoints.Utils.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class WSave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {
        if (cmdSender instanceof Player) {
            Player player = (Player)cmdSender;
            if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
                player.sendMessage(ChatColor.RED + "You do not have permissions to execute this command!");
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "No name given!");
                player.sendMessage(ChatColor.RED + "/wsave <name>");
                return true;
            }

            File waypointFile = new File("waypoints/"+player.getName()+".yml");
            FileConfiguration waypoints = new YamlConfiguration();

            try {
                loadFile(waypointFile, waypoints);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Save to File
            Location location = player.getLocation();
            waypoints.set(args[0] + ".coordinates.x", location.getX());
            waypoints.set(args[0] + ".coordinates.y", location.getY());
            waypoints.set(args[0] + ".coordinates.z", location.getZ());
            waypoints.set(args[0] + ".world", location.getWorld());

            player.sendMessage(ChatColor.GREEN + "Saved Waypoint!");
        }
        return true;
    }

    public static boolean loadFile(File waypointFile, FileConfiguration waypoints) throws IOException {
        if (!waypointFile.exists() || !waypointFile.isFile()) {
            waypointFile.createNewFile();
        }

        waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        return true;
    }
}
