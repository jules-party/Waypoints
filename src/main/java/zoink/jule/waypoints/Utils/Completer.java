package zoink.jule.waypoints.Utils;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import zoink.jule.waypoints.Commands.WSave;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class Completer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender cmdSender, Command cmd, String label, String[] args) {
        if (cmdSender instanceof Player) {
            Player player = (Player)cmdSender;
            File waypointFile = new File("waypoints/" + player.getName() + ".yml");
            FileConfiguration waypoints;
            waypoints = YamlConfiguration.loadConfiguration(waypointFile);

            Iterator iterator = waypoints.getKeys(false).iterator();
            List<String> waypointList = new ArrayList<String>();
            while (iterator.hasNext())
                waypointList.add(iterator.next().toString());

            List<String> filteredList = Lists.newArrayList(Collections2.filter(
                    waypointList, Predicates.containsPattern(args[0])
            ));
            Collections.sort(filteredList);

            return filteredList;
        }

        return null;
    }
}
