package de.wolfpackrudel.nameandchatutils;

import co.aikar.commands.PaperCommandManager;
import de.wolfpackrudel.nameandchatutils.api.def.INameAndChatUtilsApi;
import de.wolfpackrudel.nameandchatutils.api.impl.NameAndChatUtilsApi;
import de.wolfpackrudel.nameandchatutils.commands.NameCommand;
import de.wolfpackrudel.nameandchatutils.commands.PlayerDisplayCommand;
import de.wolfpackrudel.nameandchatutils.commands.SetNameCommand;
import de.wolfpackrudel.nameandchatutils.commands.SkinCommand;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import de.wolfpackrudel.nameandchatutils.listener.ChatMessageListener;
import de.wolfpackrudel.nameandchatutils.listener.DisconnectListener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class NameAndChatUtils extends JavaPlugin {

    public static String PREFIX;
    private static NameAndChatUtils instance;
    private static NameAndChatUtilsApi api;
    private HashMap<Player, TemplateHolder> playerTemplates;

    SetNameCommand setNameCommand;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        /*
        *   Config
        */
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveResource("config.yml", false);
            reloadConfig();
        }
        PREFIX = LegacyComponentSerializer.legacy('&').serialize(Language.getLang("prefix"));

        /*
        *   Create API
        */
        api = new NameAndChatUtilsApi();

        /*
        *   Register (Debug) Command
        */
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new NameCommand());
        commandManager.registerCommand(new SkinCommand());
        commandManager.registerCommand(new PlayerDisplayCommand());
        commandManager.enableUnstableAPI("help");

        setNameCommand = new SetNameCommand();
        this.getCommand("setname").setExecutor(new SetNameCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new ChatMessageListener(), this);

        /*
        *   Register Events
        */
        Bukkit.getPluginManager().registerEvents(new DisconnectListener(), this);

    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            api.reset(player);
        }
        setNameCommand.stopAllTasks();
    }

    public static NameAndChatUtils getInstance() {
        return instance;
    }

    public static INameAndChatUtilsApi getApi() {
        return api;
    }

    public HashMap<Player, TemplateHolder> getPlayerTemplates() {
        return playerTemplates;
    }
}
