package de.wolfpackrudel.nameandchatutils.commands;

import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class CommandExecutionUtil {

    public static void runSelectorCommand(Player player, String input, BiConsumer<Player, String[]> function, String self, String other, String selector) {
        //Check and parse input
        if (input == null) input = "";
        String[] args = SelectorParser.getSelectorMatchedCommand(input);

        //Apply for yourself
        if (args.length == 0) {
            function.accept(player, args);
            player.sendMessage(Language.getPrefixedLang(self));
            return;
        }

        // In case of a selector, use the bukkit provided selectorMatcher
        List<Entity> entities = SelectorParser.getEntitiesFromSelector(player, args[0]);
        if (entities == null) {
            player.sendMessage(Language.getPrefixedLang(Language.INVALID_SELECTOR, "selector", args[0]));
            return;
        }

        List<Player> players = entities.stream().filter(p -> p instanceof Player).map(p -> (Player) p).toList();

        //Handle case, no players
        if (players.isEmpty() && args[0].contains("@")) {
            player.sendMessage(Language.getPrefixedLang(Language.NO_PLAYERS_MODIFIED));
            return;
        } else if (players.isEmpty()) {
            player.sendMessage(Language.getPrefixedLang(Language.INVALID_PLAYER, "player", args[0]));
            return;
        }

        //Execute for selected players
        String[] strippedArgs = Arrays.copyOfRange(args, 1, args.length);
        for (Player p : players) {
            function.accept(p, strippedArgs);
        }

        //Success message
        if (players.size() == 1) {
            if (players.get(0).getUniqueId().equals(player.getUniqueId()))
                player.sendMessage(Language.getPrefixedLang(self));
            else
                player.sendMessage(Language.getPrefixedLang(other, "player", NameAndChatUtils.getApi().getBasePlayerName(players.get(0))));
        } else
            player.sendMessage(Language.getPrefixedLang(selector, "amount", String.valueOf(players.size())));
    }

}
