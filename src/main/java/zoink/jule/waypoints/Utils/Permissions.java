package zoink.jule.waypoints.Utils;

public enum Permissions {
    WAYPOINTS("zoink.waypoints"),
    ADMIN("zoink.waypoints.admin");

    public final String permission;
    Permissions(String permission) {
        this.permission = permission;
    }
}
