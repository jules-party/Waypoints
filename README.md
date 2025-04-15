# Waypoints
Waypoints Plugin for Minecraft (Paper)


## In-game commands

- **/wsave \<waypoint-name\>** | saves a new waypoint (permission required: **zoink.waypoints**).
- **/wtp \<waypoint-name\>** | teleports to a waypoint (permission required: **zoink.waypoints**).
- **/wlist \<world-name\>** | shows the of list all waypoints (permission required: **zoink.waypoints**).
- **/wdelete \<waypoint-name\>** | delete a waypoint (permission required: **zoink.waypoints**).
- **/whome** | teleports player to their spawnpoint (permission required: **zoink.waypoints**).
- **/wspawn** | teleports player to spawn defined in config.yml (permission required: **zoink.waypoints**).
- **/wconfig \<config-var(s)\>** | used to check or redefine variables in config.yml (permission required: **zoink.waypoints**, **server operator**)
- **/wsetup \<optional-config-var(s)\>** | used to setup config.yml if needed, arguments are optional (permission required: **zoink.waypoints**, **server operator**)

## Permissions:

- **zoink.waypoints**: Allow the user to use the personal waypoints.
- **zoink.waypoints.admin**: Allow admins to configure/setup plugin through `wconfig` or `wsetup`

## Config.yml
### For Lazy People
- You can simply run `/wsetup` if you are a server operator to automatically setup `config.yml`
- Note this is not guaranteed to 100% work
- If you want to set up but change a variable like `limited_waypoints` to true, you can simply do the following:
```bash
# One argument
/wsetup limited_waypoints=true
# Multiple Arguments
/wsetup limited_waypoints=true max_waypoints=20
```

### The 'wconfig' Command
- Arguments work the exact same as the `/wsetup` command, but just changes the variables you define
- But another feature is checking the current value of a variable like this:
```bash
/wconfig limited_waypoints
# Output:
# WP> limited_waypoints: false

# Argument Example with array; NOTICE THERE ARE NO SPACES
/wconfig allowed_worlds_colors=[light_purple,green,gold]
```

### Defining Spawn
- If you do not want a spawn in the world you can simply set `enabled` to false under the spawn key in config.yml
- Simply change the x, y, and z coordinates to wherever spawn is.
- If your spawn is in a different world than the default overworld, or your overworld folder name is different, you can simply change the name under `spawn.world`.

### Booleans
- `use_real_world_names` refers to when users can list waypoints they have in a specific world.
- This setting when set to true uses the names of the worlds folder so like `world_nether`.
- For a prettier looking result I would just keep this at false.
<br><br>
- `multi_world_teleport` when set to true allows players to teleport to waypoints that are in a different world than the current one they are in.
- For example a player can teleport they have in The End while they are in the Overworld.
<br><br>
- `limited_waypoints` is for server owners who want to limit the amount of waypoints that can be saved by a player
- Note, if a player already has more than your defined max waypoints, it will not remove any.
- This boolean combos with the `max_waypoints` variable, which defines the max waypoints a player can save.

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
