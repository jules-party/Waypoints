package zoink.jule.waypoints.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.*;

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
        if(!checkPermissions(player)) return false;

        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        if (waypoints.getKeys(false).isEmpty()) {
            sendMessage(player, "<red>No waypoints found!</red>");
            return true;
        }

        // Allowed worlds is the worlds that waypoints are allowed to be saved in.
        List<String> allowedWorlds = config.getStringList("allowed_worlds");
        List<String> allowedWorldsColors = config.getStringList("allowed_worlds_colors");

        // If the sender did NOT specify worlds they want waypoints listed from
        // we go through EACH world and list them to the user
        if (args.length == 0) {
            player.sendMessage(MM.deserialize("<gray>~~~~~~</gray><green>Waypoints</green><gray>~~~~~~</gray>"));
            for (String allowedWorld : allowedWorlds) {
                List<String> messages = new ArrayList<>();
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

                        // Replace the string ".REPLACE." with the specified world color defined in the config
                        msg = "<.REPLACE.>" + key + "</.REPLACE.>: " + x + ", " + y + ", " + z;
                        msg = msg.replaceAll(".REPLACE.",  worldColor);

                        messages.add(msg);
                    }
                }
                // Sort alphabetically, this is for each world and helps give a nice looking sorted output.
                Collections.sort(messages);

                for (String msg : messages) player.sendMessage(MM.deserialize(msg));
            }
            player.sendMessage(MM.deserialize("<gray>~~~~~~~~~~~~~~~~~~~~~</gray>"));

        // Allows the sender to get the waypoints from a specific world, for example
        // only getting the waypoints saved in The End world.
        // Also, a lot of this code repeats the above  code, just shorter as we are only aiming at a specific world
        // TODO: Add the ability for the sender to add multiple worlds
        } else if (args.length < 2) {

            String world = args[0];
            List<String> messages = new ArrayList<>();

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

                    msg = "<.REPLACE.>" + key + "</.REPLACE.>: " + x + ", " + y + ", " + z;
                    msg = msg.replaceAll(".REPLACE.",  worldColor);

                    messages.add(msg);
                }
            }

            if (messages.isEmpty()) {
                sendMessage(player, "<red>No waypoints found!</red>");
                return true;
            }

            player.sendMessage(MM.deserialize("<gray>~~~~~~</gray><green>Waypoints</green><gray>~~~~~~</gray>"));
            for (String msg : messages) player.sendMessage(MM.deserialize(msg));
            player.sendMessage(MM.deserialize("<gray>~~~~~~~~~~~~~~~~~~~~~</gray>"));
        } else {
            sendMessage(player, "<red>/wlist only takes up to 1 optional argument!</red>");
            sendMessage(player, "<red>/wlist <world-name></red>");
        }

        return true;
    }
}
