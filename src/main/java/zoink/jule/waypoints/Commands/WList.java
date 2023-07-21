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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            return true;

        plugin.reloadConfig();
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

        List<String> allowedWorlds = plugin.getConfig().getStringList("allowed_worlds");
        List<String> allowedWorldsColors = plugin.getConfig().getStringList("allowed_worlds_colors");

        if (args.length == 0) {
            player.sendMessage(ChatColor.GRAY + "~~~~~~" + ChatColor.GREEN + "Waypoints" + ChatColor.GRAY + "~~~~~~");
            for (String allowedWorld : allowedWorlds) {
                List<String> messages = new ArrayList<String>();
                for (String key : waypoints.getKeys(false)) {
                    String msg;
                    String world = waypoints.getString(key + ".world");

                    if (Objects.equals(world, allowedWorld)) {
                        String worldColor = allowedWorldsColors.get(allowedWorlds.indexOf(allowedWorld));

                        DecimalFormat decimalFormat = new DecimalFormat("#");
                        double x, y, z;
                        x = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.x")));
                        y = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.y")));
                        z = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.z")));

                        msg = worldColor + key + ChatColor.RESET + ": " + x + ", " + y + ", " + z;
                        msg = ChatColor.translateAlternateColorCodes('&', msg);

                        messages.add(msg);
                    }
                }
                Collections.sort(messages);

                for (String msg : messages) player.sendMessage(msg);
            }
            player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~~");
        } else if (args.length < 2) {

            String world = args[0];
            List<String> messages = new ArrayList<String>();

            if (!plugin.getConfig().getBoolean("use_real_world_names")) {
                List<String> customNames = plugin.getConfig().getStringList("custom_world_names");
                world = allowedWorlds.get(customNames.indexOf(args[0]));
            }

            for (String key : waypoints.getKeys(false)) {
                String msg;
                String waypointWorld = waypoints.getString(key + ".world");

                if (Objects.equals(waypointWorld, world)) {
                    String worldColor = allowedWorldsColors.get(allowedWorlds.indexOf(world));

                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    double x, y, z;
                    x = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.x")));
                    y = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.y")));
                    z = Double.parseDouble(decimalFormat.format(waypoints.getDouble(key + ".coordinates.z")));

                    msg = worldColor + key + ChatColor.RESET + ": " + x + ", " + y + ", " + z;
                    msg = ChatColor.translateAlternateColorCodes('&', msg);

                    messages.add(msg);
                }
            }

            if (messages.isEmpty()) {
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "No waypoints found!");
                return true;
            }

            player.sendMessage(ChatColor.GRAY + "~~~~~~" + ChatColor.GREEN + "Waypoints" + ChatColor.GRAY + "~~~~~~");
            for (String msg : messages) player.sendMessage(msg);
            player.sendMessage(ChatColor.GRAY + "~~~~~~~~~~~~~~~~~~~~~");
        } else {
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/wlist only takes up to 1 optional argument!");
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "/wlist <world-name>");
        }

        return true;
    }
}
