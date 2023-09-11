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
import java.io.IOException;
import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

public class WDelete implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player)cmdSender;
        if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED +"You do not have permissions to execute this command!");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "No name given!");
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/wdelete <name>");
            return true;
        }
        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        System.out.println(waypoints.getKeys(false));
        if (waypoints.get(args[0]) == null) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED +"Waypoint doesn't exist!");
            return true;
        }
        waypoints.set(args[0], null);
        try {
            waypoints.save(waypointFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(CHAT_PREFIX + ChatColor.GREEN + "Removed Waypoint: " + ChatColor.RESET + args[0]);

        return true;
    }
}