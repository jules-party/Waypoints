package zoink.jule.waypoints.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TeleportUtils {
    public static void teleportPlayer(Player player, Location location) {
        if (player.isInsideVehicle()) {
            double[] cords = {
                    location.getX(),
                    location.getY(),
                    location.getZ()
            };

            Entity vehicle = player.getVehicle();
            location = new Location(player.getWorld(), cords[0], cords[1], cords[2]);
            vehicle.eject();

            // Teleport both the vehicle and Player to the waypoint/location
            vehicle.teleport(location);
            player.teleport(location);

            // Put the player back in the vehicle
            vehicle.addPassenger(player);
        } else {
            player.teleport(location);
        }
    }
}
