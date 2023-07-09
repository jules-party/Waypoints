package zoink.jule.waypoints.Utils;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;
import java.util.*;

public class WorldCompleter implements TabCompleter {
    private final Waypoints plugin;

    public WorldCompleter(Waypoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        plugin.reloadConfig();
        if (!(cmdSender instanceof Player))
            return null;

        List<String> worldNames = plugin.getConfig().getBoolean("use_real_world_names") ?
                plugin.getConfig().getStringList("allowed_worlds") :
                plugin.getConfig().getStringList("custom_world_names");

        List<String> filteredList = Lists.newArrayList(Collections2.filter(
                worldNames, Predicates.containsPattern(args[0])
        ));
        Collections.sort(filteredList);

        return filteredList;
    }
}
