package zoink.jule.waypoints;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import zoink.jule.waypoints.Commands.WDelete;
import zoink.jule.waypoints.Commands.WList;
import zoink.jule.waypoints.Commands.WSave;
import zoink.jule.waypoints.Commands.WTp;
import zoink.jule.waypoints.Utils.Completer;

public final class Waypoints extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Waypoints Enabled!");

        createWaypointsDir();
        this.getCommand("wsave").setExecutor(new WSave());

        this.getCommand("wtp").setExecutor(new WTp());
        this.getCommand("wtp").setTabCompleter(new Completer());

        this.getCommand("wlist").setExecutor(new WList());

        this.getCommand("wdelete").setExecutor(new WDelete());
        this.getCommand("wdelete").setTabCompleter(new Completer());
    }

    private void createWaypointsDir() {
        File dir = new File("waypoints");

        if (!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Plugin Disabled!");
    }
}
