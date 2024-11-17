package de.wolfpackrudel.nameandchatutils.api.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.wolfpackrudel.nameandchatutils.api.def.INameAndChatUtilsUtil;
import de.wolfpackrudel.nameandchatutils.api.impl.data.TextureData;
import org.bukkit.Bukkit;

import java.net.URL;
import java.util.Optional;


public class PlayerDisplayUtil implements INameAndChatUtilsUtil {

    @Override
    public boolean isValidMojangPlayer(String name) {
        PlayerProfile profile = Bukkit.createProfile(name);
        try {
            profile.complete();
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public Optional<URL> getTextureSkinURL(String name) {
        Optional<PlayerProfile> profile = getCompletedPlayerProfile(name);
        return profile.map(playerProfile -> playerProfile.getTextures().getSkin());
    }

    @Override
    public Optional<PlayerProfile> getCompletedPlayerProfile(String name) {
        if(!isValidMojangPlayer(name)) return Optional.empty();
        PlayerProfile profile = Bukkit.createProfile(name);
        profile.complete();
        return Optional.of(profile);
    }

    @Override
    public Optional<TextureData> getTextureDataFromProfile(PlayerProfile profile){
        ProfileProperty textureProperty = null;
        for(ProfileProperty property : profile.getProperties()){
            if(property.getName().equalsIgnoreCase("textures")){
                textureProperty = property;
                break;
            }
        }
        if(textureProperty != null) return Optional.of(new TextureData(textureProperty.getValue(), textureProperty.getSignature()));
        return Optional.empty();
    }

}
