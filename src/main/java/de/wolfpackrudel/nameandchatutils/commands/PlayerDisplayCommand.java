package de.wolfpackrudel.nameandchatutils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("playerdisplay|pd")
@CommandPermission("playerdisplay.commands.playerdisplay")
public class PlayerDisplayCommand extends BaseCommand {

    @HelpCommand
    @Syntax("")
    @Description("Shows this Information")
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("hide")
    @CommandPermission("playerdisplay.commands.playerdisplay.hide")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Hides the selected players from everyone on the server")
    public void onHidePlayer(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().hidePlayer(p),
                Language.PD_SUCCESS_HIDE_SELF,
                Language.PD_SUCCESS_HIDE_OTHER,
                Language.PD_SUCCESS_HIDE_SELECTOR);
    }

    @Subcommand("show")
    @CommandPermission("playerdisplay.commands.playerdisplay.show")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Shows the selected players for everyone on the server")
    public void onShowPlayer(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().showPlayer(p),
                Language.PD_SUCCESS_SHOW_SELF,
                Language.PD_SUCCESS_SHOW_OTHER,
                Language.PD_SUCCESS_SHOW_SELECTOR);
    }

    @Subcommand("reset")
    @CommandPermission("playerdisplay.commands.playerdisplay.reset")
    @CommandCompletion("@players @nothing")
    @Syntax("(player)")
    @Description("Resets the players name, visibility and skin")
    public void onResetPlayer(Player player, @Optional String inputString) {
        CommandExecutionUtil.runSelectorCommand(player,
                inputString,
                (p, args) -> NameAndChatUtils.getApi().reset(p),
                Language.PD_SUCCESS_RESET_SELF,
                Language.PD_SUCCESS_RESET_OTHER,
                Language.PD_SUCCESS_RESET_SELECTOR);
    }

}

