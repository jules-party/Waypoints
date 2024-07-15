package zoink.jule.waypoints.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.TeleportUtils;

import static zoink.jule.waypoints.Waypoints.*;

public class WHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player)cmdSender;
        Location playerSpawn;

        checkPermissions(player);

        if (args.length > 1) {
            sendMessage(player, "<red>This command does not accept arguments!</red>");
            sendMessage(player, "<red>/whome</red>");
            return true;
        }

        // Check if player has a spawn point set
        if (player.getBedSpawnLocation() == null) {
            sendMessage(player, "<red>You do no have a spawnpoint set!</red>");
            return true;
        } else {
            playerSpawn = player.getBedSpawnLocation();
        }

        TeleportUtils.teleportPlayer(player, playerSpawn);
        sendMessage(player, "Teleported to Spawnpoint");

        return true;
    }
}
