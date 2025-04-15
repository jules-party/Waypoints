package zoink.jule.waypoints.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static zoink.jule.waypoints.Waypoints.*;

public class WDelete implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player)cmdSender;
        if(!checkPermissions(player)) return false;

        if (args.length < 1) {
            sendMessage(player, "<red>No name given!</red>");
            sendMessage(player, "<red>/wdelete <name></red>");
            return true;
        }

        // Get the players waypoint file
        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        // Check if the waypoint the player wants to delete even exist
        if (waypoints.get(args[0]) == null) {
            sendMessage(player, "<red>Waypoint doesn't exist!</red>");
            return true;
        }
        // Setting the waypoint to null removes it from the file
        waypoints.set(args[0], null);
        try {
            waypoints.save(waypointFile);
        } catch (IOException e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
        sendMessage(player, "<green>Removed Waypoint: </green>" + args[0]);

        return true;
    }
}