package zoink.jule.waypoints.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static zoink.jule.waypoints.Waypoints.*;

public class WSave implements CommandExecutor {
    private final Waypoints plugin;

    public WSave(Waypoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player) cmdSender;
        if(!checkPermissions(player)) return false;

        if (args.length < 1) {
            sendMessage(player, "<red>No name given!</red>");
            sendMessage(player, "<red>/wsave <name></red>");
            return true;
        }

        // Users are not allowed to use periods cause that's how we parse through the yaml tree
        // Yes, in theory it would work, but it's a personal choice of mine.
        // If you don't like it, fork and patch or start an issue.
        if (args[0].contains(".")) {
            sendMessage(player, "<red>Waypoint can not contain a period!</red>");
            sendMessage(player, "<red>Try changing it to: </red>" + args[0].replace('.', '_'));
            return true;
        }

        // See if the sender is out of allowed saved waypoints.
        if (plugin.getConfig().getBoolean("limited_waypoints")) {
            File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
            FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);

            int waypointCount = waypoints.getKeys(false).size();
            if (waypointCount >= plugin.getConfig().getInt("max_waypoints") ) {
                sendMessage(player, "<red>You have the max amount of waypoints already!</red>");
                sendMessage(player, "<red>Max Waypoints</red>: " + plugin.getConfig().getInt("max_waypoints"));

                return true;
            }
        }

        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints;

        if (!waypointFile.exists() || !waypointFile.isFile()) {
            try {
                boolean fileCreated = waypointFile.createNewFile();
                if (!fileCreated) {
                    throw new IOException("Unable to create waypoints file!");
                }
            } catch (IOException e) {
                LOGGER.warning(Arrays.toString(e.getStackTrace()));
            }
        }

        waypoints = YamlConfiguration.loadConfiguration(waypointFile);

        try {
            waypoints.load(waypointFile);
        } catch (IOException | InvalidConfigurationException e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }

        List<String> allowedWorlds = plugin.getConfig().getStringList("allowed_worlds");
        if (!allowedWorlds.contains(player.getWorld().getName())) {
            sendMessage(player, "<red>You are not permitted to save waypoints in this world!</red>");
            return true;
        }

        // See if waypoint exists or not
        if (waypoints.get(args[0]) != null) {
            sendMessage(player, "<red>Waypoint with the name: </red>" + args[0] + " <red>already exists!</red>");
            return true;
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
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
        sendMessage(player, "Saved Waypoint!");

        return true;
    }
}
