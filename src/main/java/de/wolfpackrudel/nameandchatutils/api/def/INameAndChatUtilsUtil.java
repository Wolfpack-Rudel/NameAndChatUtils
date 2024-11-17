package de.wolfpackrudel.nameandchatutils.api.def;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.wolfpackrudel.nameandchatutils.api.impl.data.TextureData;

import java.net.URL;
import java.util.Optional;

public interface INameAndChatUtilsUtil {

    /**
     * Check if the provided name is an actual existing player
     * @param name
     * @return true if the player exists
     */
    boolean isValidMojangPlayer(String name);

    /**
     * Get the Texture skin url from a player's name
     * @param name
     * @return The actual skin url of the player
     */
    Optional<URL> getTextureSkinURL(String name);

    /**
     * Get a completed player profile from a player's name or none if the player does not exist
     * @param name
     * @return
     */
    Optional<PlayerProfile> getCompletedPlayerProfile(String name);

    /**
     * Get the TextureData from a playerprofile or none if it does not exist
     * @param profile
     * @return
     */
    Optional<TextureData> getTextureDataFromProfile(PlayerProfile profile);

}
