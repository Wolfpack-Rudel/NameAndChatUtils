package de.wolfpackrudel.nameandchatutils.api.impl.data;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.entity.Player;

import java.util.Optional;


public record TextureData(String value, String signature) {

    /**
     * Applies this Texturedata to the player and returns the old textures of this provided player
     *
     * @param player The player to apply the value to
     * @return The Texturedata the player had on previously
     */
    public Optional<TextureData> applyToPlayer(Player player) {
        PlayerProfile profile = player.getPlayerProfile();

        ProfileProperty textureProperty = null;
        for (ProfileProperty property : profile.getProperties()) {
            if (property.getName().equalsIgnoreCase("textures")) {
                textureProperty = property;
                break;
            }
        }

        profile.getProperties().clear();
        profile.getProperties().add(new ProfileProperty("textures", value, signature));
        profile.complete();
        player.setPlayerProfile(profile);
        if (textureProperty != null)
            return Optional.of(new TextureData(textureProperty.getValue(), textureProperty.getSignature()));
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "TextureData{" +
                "value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

}
