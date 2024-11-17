package de.wolfpackrudel.nameandchatutils.listener;

import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        NameAndChatUtils.getApi().reset(event.getPlayer());
    }



}
