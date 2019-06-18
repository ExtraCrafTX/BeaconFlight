# BeaconFlight

![BeaconFlight icon](src/main/resources/assets/beaconflight/icon-medium.png?raw=true)

This is a server-side only mod (works with unmodded clients!) which allows beacons to grant creative flight to players when certain configurable conditions are met. 

***Note:*** *Although this is a server-side mod, it can still be used in single player on clients*

## Behaviour
By default, the mod requires the beacon to be an activated level 4 beacon (i.e. a max level beacon with an effect active) and requires the player to have the "Sky's the Limit" advancement unlocked. If both these conditions are met, the player will be able to fly when within range of the beacon.\
If the player goes out of range of the beacon, the flight will last the same amount of time as the potion effects. Once the flight expires, the player will be given the Slow Falling effect for 10s, allowing them to glide back down safely.

## Configuration
This is all configurable, along with requiring the player to have certain items equipped or held. Here are all the config options:

* `minBeaconLevel` specifies the minimum level the beacon must be to grant this effect
* `mainHandItem` specifies the item, if any, which needs to be held in the player's main hand
* `offHandItem` specifies the item, if any, which needs to be held in the player's off hand
* `anyHandItem` specifies an item, if any, which needs to be held in one of the player's hands
* `headItem` specifies the item, if any, which must be worn on the player's head
* `chestItem` specifies the item, if any, which must be worn on the player's chest
* `legsItem` specifies the item, if any, which must be worn on the player's legs
* `feetItem` specifies the item, if any, which must be worn on the player's feet
* `advancementsRequired` specifies the list of advancements that the player must have unlocked*
* `flightLingerTime` specifies how long the flight lasts when not in range**
* `slowFallingTime` specifies how long the Slow Falling effect is given for after flight is lost
* `logLevel` specifies the level of logging in the console, can be `INFO`, `WARN`, `ERROR` or `OFF`

\* The id of the advancements is required. This can be found in the in-game autocomplete for the advancement command, or a full list with descriptions may be found on the [Minecraft wiki](https://minecraft.gamepedia.com/Advancements#List_of_advancements).

** A flightLingerTime of 0 gives the same time as the effects for that beacon level. The time should also be more than 4 seconds if custom, as beacons only apply their effects every 4 seconds.

### Example 
The following is an example config that requires a max level beacon, requires the player to hold feathers in both hands and have equipped full diamond armour and have the "Sky's the Limit" and "Return to Sender" advancements. It will grant flight for the same amount of time as the potion effects and gives 10 seconds of Slow Falling upon losing flight:
```json
{
  "minBeaconLevel": 4,
  "mainHandItem": "minecraft:feather",
  "offHandItem": "minecraft:feather",
  "anyHandItem": null,
  "headItem": "minecraft:diamond_helmet",
  "chestItem": "minecraft:diamond_chestplate",
  "legsItem": "minecraft:diamond_leggings",
  "feetItem": "minecraft:diamond_boots",
  "advancementsRequired": [
    "minecraft:end/elytra",
    "minecraft:nether/return_to_sender"
  ],
  "flightLingerTime": 0,
  "slowFallingTime": 10,
  "logLevel": "INFO"
}
```
The config file is present in a folder called "config" in the same directory as Minecraft, which is the server directory in case of a dedicated server, or .minecraft in case of the client (it will be in the same place as the mods folder). It is called `beaconflight_config.json`.

If the file doesn't exist, the mod will create a default file upon startup.

 

## Credits
Thanks to [@xAlicatt](https://minecraft.curseforge.com/members/xalicatt) for the ideas, commissioning the mod, and for testing!
