package de.wolfpackrudel.nameandchatutils;

import co.aikar.commands.PaperCommandManager;
import de.wolfpackrudel.nameandchatutils.api.def.INameAndChatUtilsApi;
import de.wolfpackrudel.nameandchatutils.api.impl.NameAndChatUtilsApi;
import de.wolfpackrudel.nameandchatutils.commands.NameCommand;
import de.wolfpackrudel.nameandchatutils.commands.PlayerDisplayCommand;
import de.wolfpackrudel.nameandchatutils.commands.SkinCommand;
import de.wolfpackrudel.nameandchatutils.lang.Language;
import de.wolfpackrudel.nameandchatutils.listener.DisconnectListener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class NameAndChatUtils extends JavaPlugin {

    public static String PREFIX;
    private static NameAndChatUtils instance;
    private static NameAndChatUtilsApi api;

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
    }

    public static NameAndChatUtils getInstance() {
        return instance;
    }

    public static INameAndChatUtilsApi getApi() {
        return api;
    }
}
