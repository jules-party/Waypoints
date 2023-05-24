package zoink.jule.waypoints;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;

public final class Waypoints extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    private void createWaypointsDir() {
        File dir = new File("waypoints");

        if (!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
    }
}
