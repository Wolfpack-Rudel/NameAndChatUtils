package de.wolfpackrudel.nameandchatutils.listener;

import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.TemplateHolder;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import io.papermc.paper.event.player.ChatEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;

public class ChatMessageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onChat(ChatEvent event) {

        //Is a name template set, and the message no command except /msg?
        Player sender = ((Player) event.getPlayer());
        HashMap<Player, TemplateHolder> templates = NameAndChatUtils.getInstance().getPlayerTemplates();
        if (!templates.containsKey(sender)) return;

        String message = event.message().toString();
        //Handle Msg
        if (message.startsWith("/msg")) {
            String[] parts = message.split(" ");
            String recvPlayer = parts[1];
            message = String.join(" ", Arrays.asList(parts).subList(2, parts.length));
            Player recv = NameAndChatUtils.getInstance().getServer().getPlayer(recvPlayer);
            if (recv == null) {
                sender.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.playerNotFound", "name", recvPlayer)));
            } else {
                //Apply placeholder
                message = applyPlaceholder(templates.get(sender).getPrivateMessageTemplate() + message, templates.get(sender).getName(), recv);

                //Send message
                recv.sendMessage(new TextComponent(message));
                sender.sendMessage(new TextComponent(message));
            }

        } else {
            //handle global command
            for (Player p : sender.getServer().getOnlinePlayers()) {
                //Apply placeholder
                String playerMessage = applyPlaceholder(templates.get(sender).getGlobalMessageTemplate() + message, templates.get(sender).getName(), p);

                //Send message
                p.sendMessage(new TextComponent(playerMessage));
            }
        }
        event.setCancelled(true);

    }

    public static String applyPlaceholder(String message, String name, Player recv) {
        message = message.replace("%receiver%", recv.getName()).replace("%sender%", name);
        return ChatColor.translateAlternateColorCodes('&', message);
    }


}
