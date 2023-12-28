# Waypoints
Waypoints Plugin for Minecraft (Paper)


## In-game commands

- /wsave \<waypoint-name\> saves a new waypoint (permission required: **zoink.waypoints**).
- /wtp \<waypoint-name\> teleports to a waypoint (permission required: **zoink.waypoints**).
- /wlist \<world-name\> shows the of list all waypoints (permission required: **zoink.waypoints**).
- /wdelete \<waypoint-name\> delete a waypoint (permission required: **zoink.waypoints**).
- /whome teleports player to their spawnpoint (permission required: **zoink.waypoints**).
- /wspawn teleports player to spawn defined in config.yml (permission required: **zoink.waypoints**).

## Permissions:

- **zoink.waypoints**: Allow the user to use the personal waypoints.

## Config.yml
### Defining Spawn
- If you do not want a spawn in the world you can simply set `enabled` to false under the spawn key in config.yml
- Simply change the x, y, and z coordinates to wherever spawn is.
- If your spawn is in a different world than the default overworld, or your overworld folder name is different, you can simply change the name under `spawn.world`.

### Booleans
- There are two booleans that you can mess around with in config.yml
- `use_real_world_names` refers to when users can list waypoints they have in a specific world.
- This setting when set to true uses the names of the worlds folder so like `world_nether`.
- For a prettier looking result I would just keep this at false.
- `multi_world_teleport` when set to true allows players to teleport to waypoints that are in a different world than the current one they are in.
- For example a player can teleport they have in The End while they are in the Overworld.

### Allowed Worlds
- You are able to set which worlds players are able to save waypoints in
- The order of the worlds is the order they will be printed out in when a player runs `/wlist`.

### Allowed Worlds Colors
- The colors are based on the [MiniMessage Colors](https://docs.advntr.dev/minimessage/format.html#color)
- The order of the colors is the same as the ones in the `allowed_worlds` list.

### Custom World Names
- This only matters when you have `use_real_world_names` set to false.
- They are in the same order as the `allowed_worlds` list.

## Build the plugin

To build the plugin, type the following commands:

```bash
cd Waypoints
mvn clean package
```
