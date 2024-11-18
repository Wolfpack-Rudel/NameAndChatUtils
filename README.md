# NameAndChatUtils

NameAndChatUtils is a plugin as well as an api connector to change the players skin and name.
It also allows the user to show and hide other players.
The Plugin gives you the Option to write as other people.

## Commands
### The _name_ command:

- `/name change (player) [name]` Change the nametag of a player.
    - There will be an error if you try to change the name to the name of a player already on the server. To circumvent
      this error use the `-f` flag.
- `/name show (selector)` Show the nametag of the selected players.
- `/name hide (selector)` Show the nametag of the selected players.
- `/name reset (selector)` Resets the nametag, changing the name to the original players name and showing the nametag

### The _skin_ command:

- `/skin change (selector) [skin/skinurl]` Change the skin of the selected players.
    - You can either provide the name of the player to get the skin from or a valid texture url.
- `/skin reset (selector)` Reset the skin of the selected players.

### The _playerdisplay/pd_ command:

- `/pd show (selector)` Show the selected players.
- `/pd hide (selector)` Hide the selected players.
- `/pd reset (selector)` Reset the skin/name/visibility of the selected players.

## Permissions

```properties
# Commands (Skin)
playerdisplay.commands.skin
playerdisplay.commands.skin.change
playerdisplay.commands.skin.reset
# Commands (PlayerDisplay)
playerdisplay.commands.playerdisplay
playerdisplay.commands.playerdisplay.hide
playerdisplay.commands.playerdisplay.show
playerdisplay.commands.playerdisplay.reset
# Commands (Name)
playerdisplay.commands.name
playerdisplay.commands.name.change
playerdisplay.commands.name.show
playerdisplay.commands.name.hide
playerdisplay.commands.name.reset
```

# PlayerDisplay (Api)

To use this plugin as an api in your plugin, the plugin has to be installed on the server.
After that you can get the api instance by using `NameAndChatUtils#getApi`.
This provides you with all the necessary methods to change the players skin/name. 