All modifications compared with origin 2.1.0

The boxes on the left hand side are used to update the codes when the version is updated
* [ ] Not completed
* [-] Working
* [*] Paused because of a/some problem(s) (With comment(s))
* [=] Paused
* [X] Done

# New Classes: <!-- Just copying -->
* [X] minicraft.core.Crash
* [X] minicraft.core.Mod
* [X] minicraft.core.io.ClassLoader
* [X] minicraft.core.io.CommandWindow
* [*] minicraft.gfx.GraphicComp <!-- mentioned in line `minicraft.gfx.GraphicComp#L7` -->
* [X] minicraft.screen.ModsDisplay

# Small additions
* [X] TitleDisplay add ModsDisplay
* [X] GameDir "playminicraft/mods/Minicraft_Plus" -> "playminicraft/mods/Minicraft_Plus_Mods"

# Compatibilities
* [X] LevelGen Mod Compatibility
* [X] Tile.TileConnections
    * [X] connectsTo* -> TileConnections#get/set
* [X] ToolType Mod Compatibility
    * [X] ToolType enum to class
* [X] ToolItem Mod Compatibility
    * [X] LEVEL_NAMES (String[]) -> LEVELS (HashMap<\String, Integer>)
* [X] OreType Mod Compatibility
    * [X] OreType enum to class
* [ ] Level CommandWindow Compatibility
* [X] Items Mod Compatibility (Mods#registeredItems)
* [X] Change all visibilities of constructors of items and tiles to public
