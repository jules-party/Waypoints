package zoink.jule.waypoints.Commands;

import net.md_5.bungee.api.chat.BaseComponent;
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
import java.text.DecimalFormat;

public class WList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {
        if (cmdSender instanceof Player) {
            Player player = (Player) cmdSender;

            if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
                player.sendMessage(ChatColor.RED + "You do not have permissions to execute this command!");
                return true;
            }

            File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
            FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);

            player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~");

            for (String key : waypoints.getKeys(false)) {
                String world = waypoints.getString(key + ".world");
                ChatColor worldColor;

                switch (world) {
                    case "world":
                        worldColor = ChatColor.GREEN;
                        break;
                    case "world_nether":
                        worldColor = ChatColor.GOLD;
                        break;
                    case "world_the_end":
                        worldColor = ChatColor.LIGHT_PURPLE;
                        break;
                    default:
                        worldColor = ChatColor.AQUA;
                }

                DecimalFormat decimalFormat = new DecimalFormat("#");

                double x, y, z;
                x = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.x")));
                y = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.y")));
                z = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.z")));

                player.sendMessage(worldColor + key + ChatColor.RESET + " : " + x + ", " + y + ", " + z);
            }
            player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~");
        }

        return true;
    }
}
