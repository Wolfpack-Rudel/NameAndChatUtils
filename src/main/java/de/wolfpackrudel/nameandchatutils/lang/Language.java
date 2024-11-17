package de.wolfpackrudel.nameandchatutils.lang;

import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Language {

    public static String INVALID_PLAYER = "commands.invalidPlayer";
    public static String NO_PLAYERS_MODIFIED = "commands.noPlayersModified";
    public static String INVALID_SELECTOR = "commands.invalidSelector";

    public static String SKIN_LOADING = "commands.skin.loading";
    public static String SKIN_SUCCESS_CHANGE_OTHER = "commands.skin.success.change.other";
    public static String SKIN_SUCCESS_CHANGE_SELF = "commands.skin.success.change.self";
    public static String SKIN_SUCCESS_CHANGE_SELECTOR = "commands.skin.success.change.selector";
    public static String SKIN_SUCCESS_RESET_OTHER = "commands.skin.success.reset.other";
    public static String SKIN_SUCCESS_RESET_SELF = "commands.skin.success.reset.self";
    public static String SKIN_SUCCESS_RESET_SELECTOR = "commands.skin.success.reset.selector";
    public static String SKIN_ERROR_INVALID_URL = "commands.skin.error.invalidURL";
    public static String SKIN_ERROR_INVALID_PLAYER = "commands.skin.error.invalidPlayer";
    public static String SKIN_ERROR_INVALID_SKIN = "commands.skin.error.invalidSkin";

    public static String PD_SUCCESS_HIDE_OTHER = "commands.pd.success.hide.other";
    public static String PD_SUCCESS_HIDE_SELECTOR = "commands.pd.success.hide.selector";
    public static String PD_SUCCESS_HIDE_SELF = "commands.pd.success.hide.self";
    public static String PD_SUCCESS_SHOW_OTHER = "commands.pd.success.show.other";
    public static String PD_SUCCESS_SHOW_SELECTOR = "commands.pd.success.show.selector";
    public static String PD_SUCCESS_SHOW_SELF = "commands.pd.success.show.self";
    public static String PD_SUCCESS_RESET_OTHER = "commands.pd.success.reset.other";
    public static String PD_SUCCESS_RESET_SELF = "commands.pd.success.reset.self";
    public static String PD_SUCCESS_RESET_SELECTOR = "commands.pd.success.reset.selector";

    public static String NAME_SUCCESS_CHANGE_OTHER = "commands.name.success.change.other";
    public static String NAME_SUCCESS_CHANGE_SELF = "commands.name.success.change.self";
    public static String NAME_SUCCESS_HIDE_OTHER = "commands.name.success.hide.other";
    public static String NAME_SUCCESS_HIDE_SELF = "commands.name.success.hide.self";
    public static String NAME_SUCCESS_HIDE_SELECTOR = "commands.name.success.hide.selector";
    public static String NAME_SUCCESS_SHOW_OTHER = "commands.name.success.show.other";
    public static String NAME_SUCCESS_SHOW_SELF = "commands.name.success.show.self";
    public static String NAME_SUCCESS_SHOW_SELECTOR = "commands.name.success.show.selector";
    public static String NAME_SUCCESS_RESET_OTHER = "commands.name.success.reset.other";
    public static String NAME_SUCCESS_RESET_SELF = "commands.name.success.reset.self";
    public static String NAME_SUCCESS_RESET_SELECTOR = "commands.name.success.reset.selector";

    public static String NAME_ERROR_PLAYER_EXISTS = "commands.name.error.playerWithNameExists";


    public static Component getLang(String path, String... replacements) {
        String lang = NameAndChatUtils.getInstance().getConfig().getString(path, "&cUnknown language key \"&6" + path + "&c\"");
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            lang = lang.replace("%" + replacements[i] + "%", replacements[i + 1]);
        }
        return LegacyComponentSerializer.legacy('&').deserialize(lang);
    }

    public static Component getPrefixedLang(String path, String... replacements) {
        String lang = NameAndChatUtils.PREFIX + NameAndChatUtils.getInstance().getConfig().getString(path, "&cUnknown language key \"&6" + path + "&c\"");
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            lang = lang.replace("%" + replacements[i] + "%", replacements[i + 1]);
        }
        return LegacyComponentSerializer.legacy('&').deserialize(lang);
    }

}
