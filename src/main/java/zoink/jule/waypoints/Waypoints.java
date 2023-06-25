package zoink.jule.waypoints;

import java.io.File;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import zoink.jule.waypoints.Commands.*;
import zoink.jule.waypoints.Utils.Completer;

public final class Waypoints extends JavaPlugin {
    public static final String CHAT_PREFIX = ChatColor.GREEN + "WP" + ChatColor.BLUE + "> " + ChatColor.RESET;
    public static final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void onEnable() {
        super.onEnable();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        LOGGER.info("hiiii :3");
        LOGGER.info("Waypoints Enabled!");

        createWaypointsDir();
        this.getCommand("wsave").setExecutor(new WSave());

        this.getCommand("wlist").setExecutor(new WList());

        this.getCommand("whome").setExecutor(new WHome());

        this.getCommand("wspawn").setExecutor(new WSpawn(this));

        this.getCommand("wtp").setExecutor(new WTp());
        this.getCommand("wtp").setTabCompleter(new Completer());


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
        LOGGER.info("Plugin Disabled!");
    }
}
