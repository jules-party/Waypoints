package zoink.jule.waypoints.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.Permissions;

import java.io.File;

public class WDelete implements CommandExecutor {
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
                player.sendMessage(ChatColor.RED + "/wdelete <name>");
                return true;
            }

            File waypointFile = new File("waypoints/"+player.getName()+".yml");
            FileConfiguration waypoints = new YamlConfiguration();

            if (waypoints.get(args[0]) == null) {
                player.sendMessage(ChatColor.RED + "Waypoint doesn't exist!");
                return true;
            }

            waypoints.set(args[0], null);
            player.sendMessage(ChatColor.GREEN + "Removed Waypoint: " + ChatColor.RESET + args[0]);
        }
        return true;
    }
}
