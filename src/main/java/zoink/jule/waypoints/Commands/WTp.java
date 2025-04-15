package zoink.jule.waypoints.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.TeleportUtils;
import zoink.jule.waypoints.Waypoints;

import java.io.File;
import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.*;

public class WTp implements CommandExecutor {
    private final Waypoints plugin;

    public WTp(Waypoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player)cmdSender;
        World world;

        if(!checkPermissions(player)) return false;

        if (args.length < 1) {
            sendMessage(player, "<red>No name given!</red>");
            sendMessage(player, "<red>/wtp <name></red>");
            return true;
        }

        // Load players waypoints
        File waypointsFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointsFile);

        // Check if waypoint exist
        if (waypoints.get(args[0]) == null) {
            sendMessage(player, "<red>Waypoint doesn't exist!</red>");
            return true;
        }

        // Get the coordinates of the waypoint
        double x, y, z;
        x = waypoints.getDouble(args[0] + ".coordinates.x");
        y = waypoints.getDouble(args[0] + ".coordinates.y");
        z = waypoints.getDouble(args[0] + ".coordinates.z");

        world = Bukkit.getWorld(Objects.requireNonNull(waypoints.getString(args[0] + ".world")));
        Location location;
        location = new Location(world, x, y, z);

        if (!plugin.getConfig().getBoolean("multi_world_teleport")
            && !Objects.equals(waypoints.getString(args[0] + ".world"), player.getWorld().getName())) {
            sendMessage(player, "<red>Multi World Teleport is not enabled on this server!</red>");
            return true;
        }

        TeleportUtils.teleportPlayer(player, location);
        sendMessage(player, "<green>Teleported to waypoint: </green>" + args[0]);

        return true;
    }
}
