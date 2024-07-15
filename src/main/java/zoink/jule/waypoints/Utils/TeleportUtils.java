package zoink.jule.waypoints.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportUtils {
    public static void teleportPlayer(Player player, Location location) {
        if (player.isInsideVehicle()) {
            Entity vehicle = player.getVehicle();
            assert vehicle != null;
            List<Entity> passengers = vehicle.getPassengers();

            // I've tested this a lot and it works *okay*
            vehicle.eject();
            vehicle.teleport(location);

            // Teleport all passengers of the vehicle and put them back in it
            for (Entity passenger : passengers) passenger.teleport(location);
            for (Entity passenger : passengers) vehicle.addPassenger(passenger);

        } else {
            player.teleport(location);
        }
    }
}
