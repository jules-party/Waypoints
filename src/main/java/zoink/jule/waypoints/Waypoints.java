package zoink.jule.waypoints;

import java.io.File;
import java.util.logging.Logger;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import zoink.jule.waypoints.Commands.*;
import zoink.jule.waypoints.Utils.*;

public final class Waypoints extends JavaPlugin {
    public static final String CHAT_PREFIX = "<green>WP<blue>> <reset>";
    public static final Logger LOGGER = Bukkit.getLogger();
    public static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        super.onEnable();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Say hi to the server
        LOGGER.info("[Waypoints] hiiii :3");
        LOGGER.info("[Waypoints] Waypoints Enabled!");

        createWaypointsDir();

        // Register commands and listener
        this.getCommand("wsave").setExecutor(new WSave(this));

        this.getCommand("whome").setExecutor(new WHome());

        this.getCommand("wspawn").setExecutor(new WSpawn(this));

        this.getCommand("wlist").setExecutor(new WList(this));
        this.getCommand("wlist").setTabCompleter(new WorldCompleter(this));

        this.getCommand("wtp").setExecutor(new WTp(this));
        this.getCommand("wtp").setTabCompleter(new WaypointCompleter(this));

        this.getCommand("wdelete").setExecutor(new WDelete());
        this.getCommand("wdelete").setTabCompleter(new WaypointCompleter(this));

        this.getCommand("wconfig").setExecutor(new WConfig(this));
        this.getCommand("wconfig").setTabCompleter(new ConfigCompleter(this));

        this.getCommand("wsetup").setExecutor(new WSetup(this));
        this.getCommand("wsetup").setTabCompleter(new ConfigCompleter(this));

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

    public static void sendMessage(Player player, final String msg) {
        player.sendMessage(MM.deserialize(CHAT_PREFIX + msg));
    }

    public static void sendUrlMessage(Player player, final String msg, final String url) {
        player.sendMessage(Component
                .text(msg)
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .clickEvent(ClickEvent.openUrl(url)));
    }

    public static void checkPermissions(Player player) {
        if (!player.hasPermission(Permissions.WAYPOINTS.permission)) {
            sendMessage(player, "<red>You do not have permissions to execute this command!<red>");
        }
    }
}
