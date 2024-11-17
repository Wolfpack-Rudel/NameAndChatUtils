package de.wolfpackrudel.nameandchatutils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.api.impl.SkinChangeError;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@CommandAlias("skin")
@CommandPermission("playerdisplay.commands.skin")
public class SkinCommand extends BaseCommand {

    @HelpCommand
    @Syntax("")
    @Description("Shows this Information")
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("change")
    @CommandPermission("playerdisplay.commands.skin.change")
    @CommandCompletion("@players @nothing")
    @Syntax("(player) <name/url>")
    @Description("Changes the selected players skin")
    public void change(Player player, String inputString) throws MalformedURLException {
        // Check and parse input
        if (inputString == null) inputString = "";
        String[] args = SelectorParser.getSelectorMatchedCommand(inputString);

        if (args.length == 0) {
            player.sendMessage(Language.getPrefixedLang(Language.SKIN_ERROR_INVALID_PLAYER));
            return;
        }

        // Apply for yourself
        if (args.length == 1) {
            String changeTo = String.join(" ", args);
            // Handle url
            if (isUrl(changeTo)) {
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_LOADING));
                NameAndChatUtils.getApi().getDataFromUrl(new URL(changeTo), (error, data) -> {
                    if (error != SkinChangeError.NONE) {
                        displayError(player, error);
                        return;
                    }

                    // Change the skin
                    NameAndChatUtils.getApi().changePlayerSkin(player, data);
                    player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELF));
                });
                return;
            }

            // Change skin based on Name
            SkinChangeError error = NameAndChatUtils.getApi().changePlayerSkin(player, changeTo);
            if (error != SkinChangeError.NONE) {
                displayError(player, error);
                return;
            }
            player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELF));
            return;
        }

        //Gather entities from selector
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

        // Execute for selected players
        String changeTo = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).strip();
        if (isUrl(changeTo)) {
            // Load skin data and then apply to all players
            player.sendMessage(Language.getPrefixedLang(Language.SKIN_LOADING));
            NameAndChatUtils.getApi().getDataFromUrl(new URL(changeTo), (error, data) -> {
                if (error != SkinChangeError.NONE) {
                    displayError(player, error);
                    return;
                }

                for (Player p : players) {
                    NameAndChatUtils.getApi().changePlayerSkin(p, data);
                }

                //Success message
                if (players.size() == 1) {
                    if (players.get(0).getUniqueId().equals(player.getUniqueId()))
                        player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELF));
                    else
                        player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_OTHER, "player", players.get(0).getName()));
                } else
                    player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELECTOR, "amount", String.valueOf(players.size())));

            });
            return;
        }

        for (Player p : players) {
            SkinChangeError error = NameAndChatUtils.getApi().changePlayerSkin(p, changeTo);
            if (error != SkinChangeError.NONE) {
                displayError(player, error);
                return;
            }
        }

        //Success message
        if (players.size() == 1) {
            if (players.get(0).getUniqueId().equals(player.getUniqueId()))
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELF));
            else
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_OTHER, "player", players.get(0).getName()));
        } else
            player.sendMessage(Language.getPrefixedLang(Language.SKIN_SUCCESS_CHANGE_SELECTOR, "amount", String.valueOf(players.size())));

    }

    @Subcommand("reset")
    @CommandPermission("playerdisplay.commands.skin.reset")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Resets the selected players skin to their original")
    public void reset(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().resetSkin(p),
                Language.SKIN_SUCCESS_RESET_SELF,
                Language.SKIN_SUCCESS_RESET_OTHER,
                Language.SKIN_SUCCESS_RESET_SELECTOR);
    }

    public boolean isUrl(String s) {
        try {
            new URL(s);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public void displayError(Player player, SkinChangeError error) {
        switch (error) {
            case NO_VALID_SKIN -> {
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_ERROR_INVALID_SKIN));
            }
            case NO_VALID_PLAYER -> {
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_ERROR_INVALID_PLAYER));
            }
            case NO_VALID_URL -> {
                player.sendMessage(Language.getPrefixedLang(Language.SKIN_ERROR_INVALID_URL));
            }
        }
    }

}

