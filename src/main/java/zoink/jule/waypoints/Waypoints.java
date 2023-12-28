package zoink.jule.waypoints;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import zoink.jule.waypoints.Commands.*;
import zoink.jule.waypoints.Utils.Permissions;
import zoink.jule.waypoints.Utils.WaypointCompleter;
import zoink.jule.waypoints.Utils.TeleportUtils;
import zoink.jule.waypoints.Utils.WorldCompleter;

public final class Waypoints extends JavaPlugin {
    public static final String CHAT_PREFIX = "<green>WP<blue>> <reset>";
    public static final Logger LOGGER = Bukkit.getLogger();
    public static final MiniMessage MM = MiniMessage.miniMessage();
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
