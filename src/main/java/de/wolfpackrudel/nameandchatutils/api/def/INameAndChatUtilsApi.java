package de.wolfpackrudel.nameandchatutils.api.def;

import de.wolfpackrudel.nameandchatutils.api.impl.SkinChangeError;
import de.wolfpackrudel.nameandchatutils.api.impl.data.TextureData;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.function.BiConsumer;

public interface INameAndChatUtilsApi {

    /**
     * Get the playerDisplayUtil functionality
     *
     * @return
     */
    INameAndChatUtilsUtil getPlayerDisplayUtil();

    /**
     * Change a player skin to specific texturedatas
     *
     * @param player The player whose skin should be changed
     * @param data   The data that should be used
     */
    void changePlayerSkin(Player player, TextureData data);

    /**
     * Change a player skin to the skin of another player
     *
     * @param player The player whose skin should be changed
     * @param name   The name of the player to get the skin from
     * @return The error that happened or NONE if operation was successful
     */
    SkinChangeError changePlayerSkin(Player player, String name);

    /**
     * Get the texture data of a skin file.
     *
     * @param url
     * @param onFinishCallback
     */
    void getDataFromUrl(URL url, BiConsumer<SkinChangeError, TextureData> onFinishCallback);

    /**
     * Hide the player from all other players on the server
     *
     * @param player The player to hide
     */
    void hidePlayer(Player player);

    /**
     * Show the player to all other players
     *
     * @param player The player to show
     */
    void showPlayer(Player player);

    /**
     * Hide all players for the specified player
     *
     * @param player The player that should see no other player
     */
    void hideAllPlayers(Player player);

    /**
     * Show all players to the specified player
     *
     * @param player The player that should see all other players
     */
    void showAllPlayers(Player player);

    /**
     * Set the name of the specified player
     *
     * @param player The player whose name should be changed
     * @param name   The name it should be changed to
     */
    void setName(Player player, String name);

    /**
     * Show the players name
     *
     * @param player The player whose name should be shown
     */
    void showName(Player player);

    /**
     * Hide the players name
     *
     * @param player The player whose name should be hidden
     */
    void hideName(Player player);

    /**
     * Resets the name of the player. This sets the name to its default and displays it
     *
     * @param player The player whose name should be reset
     */
    void resetName(Player player);

    /**
     * Reset the skin of a player
     *
     * @param player The player whose skin should be reset
     */
    void resetSkin(Player player);

    /**
     * Completely reset all effects applied to the player
     *
     * @param player The player that should be reset
     */
    void reset(Player player);

    String getBasePlayerName(Player player);
}

