package zoink.jule.waypoints.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.TeleportUtils;
import zoink.jule.waypoints.Waypoints;

import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

public class WSpawn implements CommandExecutor {
    private final Waypoints plugin;

    public WSpawn(Waypoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return false;

        FileConfiguration config = plugin.getConfig();
        Player player = (Player)cmdSender;
        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn.world")));

        if (!config.getBoolean("spawn.enabled")) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "This command is not enabled on this server!");
            return true;
        }

        if (args.length > 0) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "This Command Doesn't Accept Arguments!");
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/wspawn");
            return true;
        }

        double[] spawnCoords = {
                plugin.getConfig().getDouble("spawn.x"),
                plugin.getConfig().getDouble("spawn.y"),
                plugin.getConfig().getDouble("spawn.z")
        };

        Location spawnLocation = new Location(world,
                spawnCoords[0],
                spawnCoords[1],
                spawnCoords[2]
        );

        TeleportUtils.teleportPlayer(player, spawnLocation);
        player.sendMessage(CHAT_PREFIX + "Teleported to Spawn!");

        return true;
    }
}
