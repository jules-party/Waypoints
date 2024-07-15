package zoink.jule.waypoints.Commands;

import org.bukkit.Bukkit;
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
import static zoink.jule.waypoints.Waypoints.sendMessage;

public class WSpawn implements CommandExecutor {
    private final Waypoints plugin;

    public WSpawn(Waypoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player)cmdSender;
        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn.world")));

        if (!config.getBoolean("spawn.enabled")) {
            sendMessage(player, "<red>This command is not enabled on this server!</red>");
            return true;
        }

        if (args.length > 0) {
            sendMessage(player, "<red>This command doesn't accept arguments!</red>");
            sendMessage(player, "<red>/wspawn</red>");
            return true;
        }

        // I don't feel the need to really comment on the file, as it's pretty self-explanatory,
        // but basically get the spawn coords defined in the config, then teleport the player to those coords.
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
        sendMessage(player, "Teleported to Spawn!");

        return true;
    }
}
