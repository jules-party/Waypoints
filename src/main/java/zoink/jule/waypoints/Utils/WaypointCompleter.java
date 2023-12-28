package zoink.jule.waypoints.Utils;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zoink.jule.waypoints.Waypoints;

import java.io.File;
import java.util.*;

public class WaypointCompleter implements TabCompleter {
    private final Waypoints plugin;

    public WaypointCompleter(Waypoints plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender cmdSender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cmdSender instanceof Player))
            return null;

        plugin.reloadConfig();
        Player player = (Player)cmdSender;
        File waypointFile = new File("waypoints/" + player.getUniqueId() + ".yml");
        FileConfiguration waypoints;

        waypoints = YamlConfiguration.loadConfiguration(waypointFile);
        Iterator<String> iterator = waypoints.getKeys(false).iterator();
        List<String> waypointList = new ArrayList<>();

        while (iterator.hasNext())
            waypointList.add(iterator.next());

        if (!plugin.getConfig().getBoolean("multi_world_teleport")) {
            List<String> worldWaypointsList = new ArrayList<>();
            for (String waypoint : waypointList)
                if (Objects.equals(waypoints.getString(waypoint + ".world"), player.getWorld().getName()))
                    worldWaypointsList.add(waypoint);
            waypointList = worldWaypointsList;
        }

        List<String> filteredList = Lists.newArrayList(Collections2.filter(
                waypointList, Predicates.containsPattern(args[0])
        ));

        Collections.sort(filteredList);
        return filteredList;
    }
}
