package zoink.jule.waypoints.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.Permissions;
import zoink.jule.waypoints.Utils.TeleportUtils;
import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

public class WHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return true;

        Player player = (Player)cmdSender;
        Location playerSpawn;

        if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "You do not have permissions to execute this command!");
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "This Command Doesn't Accept Arguments!");
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/whome");
            return true;
        }

        if (player.getBedSpawnLocation() == null) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED +"You do not have a spawnpoint set!");
            return true;
        } else {
            playerSpawn = player.getBedSpawnLocation();
        }

        TeleportUtils.teleportPlayer(player, playerSpawn);
        player.sendMessage(CHAT_PREFIX + ChatColor.WHITE + "Teleported to Spawnpoint");

        return true;
    }
}
