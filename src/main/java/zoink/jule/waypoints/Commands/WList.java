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
import zoink.jule.waypoints.Waypoints;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

public class WList implements CommandExecutor {
    private final Waypoints plugin;

    public WList(Waypoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return false;

        Player player = (Player) cmdSender;
        FileConfiguration config = plugin.getConfig();

        if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "You do not have permissions to execute this command!");
            return true;
        }

        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        if (waypoints.getKeys(false).size() == 0) {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "No waypoints found!");
            return true;
        }

        player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~");
        for (String key : waypoints.getKeys(false)) {
            String world = waypoints.getString(key + ".world");
            ChatColor worldColor = ChatColor.AQUA;

            if (Objects.equals(world, config.getString("worlds.overworld"))) worldColor = ChatColor.GREEN;
            if (Objects.equals(world, config.getString("worlds.nether"))) worldColor = ChatColor.GOLD;
            if (Objects.equals(world, config.getString("worlds.end"))) worldColor = ChatColor.LIGHT_PURPLE;

            DecimalFormat decimalFormat = new DecimalFormat("#");
            double x, y, z;
            x = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.x")));
            y = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.y")));
            z = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.z")));
            player.sendMessage(worldColor + key + ChatColor.RESET + " : " + x + ", " + y + ", " + z);
        }
        player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~");

        return true;
    }
}
