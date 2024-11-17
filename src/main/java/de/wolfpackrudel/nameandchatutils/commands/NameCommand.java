package de.wolfpackrudel.nameandchatutils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("name")
@CommandPermission("playerdisplay.commands.name")
public class NameCommand extends BaseCommand {

    @HelpCommand
    @Syntax("")
    @Description("Shows this Information")
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("change")
    @CommandPermission("playerdisplay.commands.name.change")
    @CommandCompletion("@players @nothing")
    @Syntax("(player) <name>")
    @Description("Changes the name of the selected players")
    public void change(Player player, String inputString) {
        String[] input = inputString.split(" ");
        boolean force = false;
        List<String> args = new ArrayList<>();

        //Parse Force flag
        for (String ele : input) {
            if (ele.equalsIgnoreCase("-force") || ele.equalsIgnoreCase("-f")) {
                force = true;
                continue;
            }
            args.add(ele);
        }
        if (args.size() == 1) {
            if (force || !SelectorParser.playerWithNameOnline(args.get(0))) {
                NameAndChatUtils.getApi().setName(player, args.get(0));
                player.sendMessage(Language.getPrefixedLang(Language.NAME_SUCCESS_CHANGE_SELF, "to", args.get(0)));
                return;
            }
            player.sendMessage(Language.getPrefixedLang(Language.NAME_ERROR_PLAYER_EXISTS, "name", args.get(0)));
            return;
        }
        Player toChange = Bukkit.getPlayer(args.get(0));
        if (toChange == null) {
            player.sendMessage(Language.getPrefixedLang(Language.INVALID_PLAYER, "player", args.get(0)));
            return;
        }
        if (force || !SelectorParser.playerWithNameOnline(args.get(1))) {
            String basePlayerName = NameAndChatUtils.getApi().getBasePlayerName(toChange);
            NameAndChatUtils.getApi().setName(toChange, args.get(1));
            player.sendMessage(Language.getPrefixedLang(Language.NAME_SUCCESS_CHANGE_OTHER, "player", basePlayerName, "to", toChange.getName()));
            return;
        }
        player.sendMessage(Language.getPrefixedLang(Language.NAME_ERROR_PLAYER_EXISTS, "name", args.get(1)));
    }

    @Subcommand("show")
    @CommandPermission("playerdisplay.commands.name.show")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Shows the name of the selected players")
    public void show(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().showName(p),
                Language.NAME_SUCCESS_SHOW_SELF,
                Language.NAME_SUCCESS_SHOW_OTHER,
                Language.NAME_SUCCESS_SHOW_SELECTOR);
    }

    @Subcommand("hide")
    @CommandPermission("playerdisplay.commands.name.hide")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Hides the name of the selected players")
    public void hide(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().hideName(p),
                Language.NAME_SUCCESS_HIDE_SELF,
                Language.NAME_SUCCESS_HIDE_OTHER,
                Language.NAME_SUCCESS_HIDE_SELECTOR);
    }

    @Subcommand("reset")
    @CommandPermission("playerdisplay.commands.name.reset")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Resets the name of the selected players")
    public void reset(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().resetName(p),
                Language.NAME_SUCCESS_RESET_SELF,
                Language.NAME_SUCCESS_RESET_OTHER,
                Language.NAME_SUCCESS_RESET_SELECTOR);
    }
}