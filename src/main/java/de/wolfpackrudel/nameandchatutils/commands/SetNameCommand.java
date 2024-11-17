package de.wolfpackrudel.nameandchatutils.commands;

import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.TemplateHolder;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import de.wolfpackrudel.nameandchatutils.listener.ChatMessageListener;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetNameCommand implements CommandExecutor {

    private final HashMap<Player, ScheduledTask> tasks;

    public SetNameCommand() {
        super();

        tasks = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.noPlayer")));
            return false;
        }

        Player player = ((Player) sender);

        //Reset Command: "/setname"
        if (args.length == 0) {
            //reset prefix
            NameAndChatUtils.getInstance().getPlayerTemplates().remove(player);
            sender.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.setname.reset")));
            return false;
        }


        String param = String.join(" ", args);
        //Only name is used. Command: "/setname name"
        if (!param.contains("\"")) {
            //Only name is used
            TemplateHolder holder = new TemplateHolder(param);
            NameAndChatUtils.getInstance().getPlayerTemplates().put(player, holder);
            player.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.setname.success")));
            startDisplayTask(player);
            return false;
        }

        param += " ";

        //" not closed properly
        if (!param.matches("(\"[(#-}) !]+\" )+")) {
            player.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.setname.invalidFormat")));
            return false;
        }

        //Parse/get list of text
        TemplateHolder holder = new TemplateHolder(player.getName());
        ArrayList<String> matches = new ArrayList<>();
        Matcher m = Pattern.compile("(\"[(#-}) !]+\" )").matcher(param);
        while (m.find()) {
            matches.add(m.group());
        }

        if (matches.size() >= 3) {
            player.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.setname.toManyArgs")));
            return false;
        }

        //Apply global and private templates
        if (matches.size() >= 1) holder.setGlobalMessageTemplate(matches.get(0).replace("\"", ""));
        if (matches.size() >= 2) holder.setPrivateMessageTemplate(matches.get(1).replace("\"", ""));

        NameAndChatUtils.getInstance().getPlayerTemplates().put(player, holder);
        startDisplayTask(player);

        //success message
        player.sendMessage(new TextComponent(NameAndChatUtils.PREFIX + Language.getLang("lang.commands.setname.success")));
        return true;
    }

    private void startDisplayTask(Player player){
        if(tasks.containsKey(player)) return;

        ScheduledTask task = (ScheduledTask) NameAndChatUtils.getInstance().getServer().getScheduler().runTask(
                NameAndChatUtils.getInstance(), () -> {
                    TemplateHolder holder = NameAndChatUtils.getInstance().getPlayerTemplates().getOrDefault(player, null);
                    if(holder == null || !player.isConnected()){
                        ScheduledTask t = tasks.get(player);
                        tasks.remove(player);
                        t.cancel();
                        return;
                    }

                    player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.valueOf(Language.getLang("lang.displayText",
                            "template", ChatMessageListener.applyPlaceholder(holder.getGlobalMessageTemplate(), holder.getName(), player)))
                    ));
                }
        );

        tasks.put(player, task);
    }

    public void stopAllTasks(){
        for(ScheduledTask task : tasks.values()){
            task.cancel();
        }
    }
}
