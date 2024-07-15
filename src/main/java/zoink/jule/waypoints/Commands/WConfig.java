package zoink.jule.waypoints.Commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static zoink.jule.waypoints.Waypoints.LOGGER;
import static zoink.jule.waypoints.Waypoints.sendMessage;

public class WConfig implements CommandExecutor {
    private final Waypoints plugin;
    public WConfig(Waypoints plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(!(cmdSender instanceof Player)) {
            return true;
        } else if(!cmdSender.isOp()) {
            sendMessage((Player)cmdSender, "<red>You are not a server operator!</red>");
            return true;
        }

        plugin.reloadConfig();
        Player player = (Player)cmdSender;
        File configFile = new File(plugin.getDataFolder() + "/config.yml");
        FileConfiguration config = plugin.getConfig();

        if(args.length < 1) {
            sendMessage(player, "<red>No arguments given!</red>");
            sendMessage(player, "<red>Example</red>: /wconfig max_waypoints=20");
            return true;
        }

        boolean updatedConfig = false;
        // Go through each argument passed with the command, so the sender can pass
        // multiple values using one command.
        for(String arg : args) {
            // Check if sender wants to modify a value
            if(arg.contains("=")) {
                updatedConfig = true;
                String[] properties = arg.split("=");

                // Check if sender is changing value to a boolean value
                if(Objects.equals(properties[1], "true") || Objects.equals(properties[1], "false")) {
                    config.set(properties[0], Boolean.parseBoolean(properties[1]));

                // Check if sender is changing to a string array/list
                } else if(properties[1].matches("\\[[^\\]]*\\]"))  {
                    // Save each item to an array by using split
                    List<String> items = Arrays.asList(
                            StringUtils.substringBetween(
                                            properties[1], "[", "]")
                                    .split(",")
                    );
                    config.set(properties[0], items);

                // Check if sender is changing value to a integer
                } else if(properties[1].matches("-?(0|[1-9]\\d*)")) {
                    config.set(properties[0], Integer.parseInt(properties[1]));

                // Else, change to string value.
                } else {
                    config.set(properties[0], properties[1]);
                }

            // If the sender is not changing the value, we send them a message showing the value of that variable
            } else {
                sendMessage(player, arg + ": <gold>" + config.get(arg) + "</gold>");
            }
        }
        if(!updatedConfig) return true;

        try {
            config.save(configFile);
        } catch (IOException e) {
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }

        sendMessage(player, "<green>Succesfully updated config!</green>");

        return true;
    }
}
