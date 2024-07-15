package zoink.jule.waypoints.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;

import java.util.ArrayList;
import java.util.List;

public class ConfigCompleter implements TabCompleter {
    private final Waypoints plugin;

    public ConfigCompleter(Waypoints plugin) {
        this.plugin = plugin;
    }
    // Used for WConfig.java and WSetup.java for tab completion for variables found in config.yaml

    @Override
    public List<String> onTabComplete(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        return new ArrayList<>(plugin.getConfig().getKeys(true));
    }
}
