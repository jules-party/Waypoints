package zoink.jule.waypoints.Commands;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Utils.Permissions;
import zoink.jule.waypoints.Waypoints;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.LOGGER;
import static zoink.jule.waypoints.Waypoints.sendMessage;

public class WSetup implements CommandExecutor {
    private final Waypoints plugin;
    public WSetup(Waypoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(!(cmdSender instanceof Player)) {
            return true;
        } else if(!cmdSender.hasPermission(Permissions.ADMIN.permission)) {
            sendMessage((Player)cmdSender, "<red>You are not a server operator!</red>");
            return true;
        }

        plugin.reloadConfig();
        Player player = (Player)cmdSender;
        List<World> worldObjects = Bukkit.getWorlds();
        List<String> worlds = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();
        File configFile = new File(plugin.getDataFolder() + "/config.yml");

        // Get the names of all the worlds (ex. Overworld, Nether, and The End)
        for(World w : worldObjects) {
            worlds.add(w.getName());
        }

        // Automatically let every world have the ability to have waypoints saved in it
        config.set("allowed_worlds", worlds);

        // Usually the default world name is just 'world', but for example
        // if a server owner is hosting on something like Apex Hosting. The
        // world name may be different from the standard 'world'.
        String defaultWorldName = worlds.get(0);
        List<String> colors = config.getStringList("allowed_worlds_colors");
        List<String> worldNames = config.getStringList("custom_world_names");
        List<String> cutWorlds = worlds.subList(3, worlds.size());

        // If a server has more than the standard amount of worlds (3),
        // then we add them to the world names and give them the default color yellow.
        for (String worldName : cutWorlds) {
            worldNames.add(
                    WordUtils.capitalize(worldName
                            .replace(defaultWorldName + "_", "")
                            .replace("_", " ")
                    )
            );

            colors.add("yellow");
        }

        config.set("allowed_worlds_colors", colors);
        config.set("custom_world_names", worldNames);

        // Basically the same thing you see in WConfig.java
        for(String arg : args) {
            if(arg.contains("=")) {
                String[] properties = arg.split("=");
                if(Objects.equals(properties[1], "true") || Objects.equals(properties[1], "false")) {
                    config.set(properties[0], Boolean.parseBoolean(properties[1]));
                } else if(properties[1].matches("\\[[^\\]]*\\]"))  {
                    List<String> items = Arrays.asList(
                            StringUtils.substringBetween(
                                    properties[1], "[", "]")
                                    .split(",")
                    );
                    config.set(properties[0], items);
                } else if(properties[1].matches("-?(0|[1-9]\\d*)")) {
                    config.set(properties[0], Integer.parseInt(properties[1]));
                } else {
                    config.set(properties[0], properties[1]);
                }
            } else {
                sendMessage(player, arg + ": <gold>" + config.get(arg) + "</gold>");
            }
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }

        plugin.reloadConfig();
        sendMessage(player, "<green>Config is now done being setup!</green>");

        return true;
    }
}
