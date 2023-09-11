package zoink.jule.waypoints;

import static zoink.jule.waypoints.Waypoints.CHAT_PREFIX;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class WaypointsListener implements Listener {
    private final Waypoints plugin;
    public WaypointsListener(Waypoints plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        Player player = event.getPlayer();
        PluginDescriptionFile pdf = plugin.getDescription();
        String url = "https://raw.githubusercontent.com/jules-party/Waypoints/main/pom.xml";

        DocumentBuilderFactory dBf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dB = dBf.newDocumentBuilder();
        Document document = dB.parse(new URL(url).openStream());
        final String currentVersion = document.getElementsByTagName("version").item(0).getTextContent();
        final String runningVersion = pdf.getVersion();

        if (!runningVersion.equals(currentVersion)) {
            if (player.isOp()) {
                TextComponent message = new TextComponent(CHAT_PREFIX + "Click this to get the latest release on GitHub");

                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/jules-party/Waypoints/releases/latest"));

                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Current running version: " + ChatColor.RESET + pdf.getVersion());
                player.sendMessage(CHAT_PREFIX + ChatColor.RED + "Plugin is outdated! Update to version: " + ChatColor.RESET + currentVersion);
                player.sendMessage(message);
            }
        }
    }
}
