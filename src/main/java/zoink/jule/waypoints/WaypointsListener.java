package zoink.jule.waypoints;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static zoink.jule.waypoints.Waypoints.*;

public class WaypointsListener implements Listener {
    private final Waypoints plugin;
    public WaypointsListener(Waypoints plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        Player player = event.getPlayer();
        PluginMeta pm = plugin.getPluginMeta();
        String url = "https://raw.githubusercontent.com/jules-party/Waypoints/main/pom.xml";

        // This whole thingamabob basically see's if we are running on the latest version of the plugin.
        DocumentBuilderFactory dBf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dB = dBf.newDocumentBuilder();
        Document document = dB.parse(new URL(url).openStream());
        final String currentVersion = document.getElementsByTagName("version").item(0).getTextContent();
        final String runningVersion = pm.getVersion();

        if (!runningVersion.equals(currentVersion)) {
            if (player.isOp()) {
                player.sendMessage(MM.deserialize(CHAT_PREFIX + "<red>Current running version: </red>" + runningVersion));
                player.sendMessage(MM.deserialize(CHAT_PREFIX + "<red>Plugin is outdated! Update to version: </red>" + currentVersion));
                sendUrlMessage(player, "Click me to get latest release of Waypoints!", "https://github.com/jules-party/Waypoints/releases/latest");
            }
        }
    }
}
