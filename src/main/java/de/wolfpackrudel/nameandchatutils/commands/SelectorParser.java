package de.wolfpackrudel.nameandchatutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SelectorParser {

    public static final Pattern SELECTOR_PATTERN = Pattern.compile("@[aeprs]");

    public static String getSelectorWithArguments(String cmd) {
        String selector = cmd.substring(cmd.indexOf('@'));

        if (selector.length() > 3 && selector.charAt(2) == '[') {
            int startBrackets = 1;
            int endBrackets = 0;
            for (int i = 3; i < selector.length(); i++) {
                if (selector.charAt(i) == '[') {
                    startBrackets++;
                } else if (selector.charAt(i) == ']') {
                    endBrackets++;
                }

                if (startBrackets == endBrackets) {
                    selector = selector.substring(0, i + 1);
                    break;
                }
            }
        } else {
            selector = selector.substring(0, 2);
        }

        return selector;
    }

    public static String[] getSelectorMatchedCommand(String command) {
        if (SELECTOR_PATTERN.matcher(command).find()) {
            String selector = getSelectorWithArguments(command);
            String rest = command.replace(selector, "");
            ArrayList<String> elements = new ArrayList<>(List.of(selector));
            Collections.addAll(elements, rest.split(" "));
            return elements.toArray(new String[1]);
        }
        if (command.isEmpty()) {
            return new String[0];
        }

        return command.split(" ");
    }

    public static boolean playerWithNameOnline(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static List<Entity> getEntitiesFromSelector(Player base, String selector) {
        if (SELECTOR_PATTERN.matcher(selector).find()) {
            try {
                return base.getServer().selectEntities(base, selector);
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else {
            Player player = Bukkit.getPlayer(selector);
            if (player != null) return List.of(player);
            return Collections.emptyList();
        }
    }

}
