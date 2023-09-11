package zoink.jule.waypoints;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import zoink.jule.waypoints.Commands.*;
import zoink.jule.waypoints.Utils.WaypointCompleter;
import zoink.jule.waypoints.Utils.TeleportUtils;
import zoink.jule.waypoints.Utils.WorldCompleter;

public final class Waypoints extends JavaPlugin {
    public static final String CHAT_PREFIX = ChatColor.GREEN + "WP" + ChatColor.BLUE + "> " + ChatColor.RESET;
    public static final Logger LOGGER = Bukkit.getLogger();
    private TeleportUtils teleportUtils;

    @Override
    public void onEnable() {
        super.onEnable();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        LOGGER.info("[Waypoints] hiiii :3");
        LOGGER.info("[Waypoints] Waypoints Enabled!");

        createWaypointsDir();
        this.getCommand("wsave").setExecutor(new WSave(this));

        this.getCommand("whome").setExecutor(new WHome());

        this.getCommand("wspawn").setExecutor(new WSpawn(this));

        this.getCommand("wlist").setExecutor(new WList(this));
        this.getCommand("wlist").setTabCompleter(new WorldCompleter(this));

        this.getCommand("wtp").setExecutor(new WTp(this));
        this.getCommand("wtp").setTabCompleter(new WaypointCompleter(this));

        this.getCommand("wdelete").setExecutor(new WDelete());
        Objects.requireNonNull(this.getCommand("wdelete")).setTabCompleter(new WaypointCompleter(this));

        this.getServer().getPluginManager().registerEvents(new WaypointsListener(this), this);
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
