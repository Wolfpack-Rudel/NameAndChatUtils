package de.wolfpackrudel.nameandchatutils.api.impl;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.wolfpackrudel.nameandchatutils.NameAndChatUtils;
import de.wolfpackrudel.nameandchatutils.api.impl.data.TextureData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mineskin.MineskinClient;
import de.wolfpackrudel.nameandchatutils.api.def.INameAndChatUtilsUtil;
import de.wolfpackrudel.nameandchatutils.api.def.INameAndChatUtilsApi;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class NameAndChatUtilsApi implements INameAndChatUtilsApi {

    private static final String INVIS_NAMETAG_TEAM_NAME = "_invisNametag";

    private final INameAndChatUtilsUtil playerDisplayUtil;
    private final HashMap<UUID, TextureData> previousSkins;
    private final HashMap<String, TextureData> cachedData;
    private final HashMap<UUID, String> previousNames;
    private final HashMap<UUID, Team> previousTeams;
    private final MineskinClient client;
    private Team invisibleNametagTeam;


    public NameAndChatUtilsApi() {
        playerDisplayUtil = new PlayerDisplayUtil();

        previousSkins = new HashMap<>();
        cachedData = new HashMap<>();
        previousNames = new HashMap<>();
        previousTeams = new HashMap<>();

        client = new MineskinClient("SkinChangeApi", "fbef67012ba99fcaf49ce582ec5148643fd383c2d0d35b34d517b043196b9356");
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        invisibleNametagTeam = scoreboard.getTeam(INVIS_NAMETAG_TEAM_NAME);
        if (invisibleNametagTeam == null) invisibleNametagTeam = scoreboard.registerNewTeam(INVIS_NAMETAG_TEAM_NAME);
        invisibleNametagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    @Override
    public INameAndChatUtilsUtil getPlayerDisplayUtil() {
        return playerDisplayUtil;
    }

    @Override
    public void changePlayerSkin(Player player, TextureData data) {
        if (data == null) return;
        Optional<TextureData> oldData = data.applyToPlayer(player);
        if (!previousSkins.containsKey(player.getUniqueId()) && oldData.isPresent())
            previousSkins.put(player.getUniqueId(), oldData.get());

        World world = player.getWorld();
        Location save = player.getLocation().clone();
        for (World w : Bukkit.getWorlds()) {
            if (w.equals(world)) continue;
            player.teleport(w.getSpawnLocation());
            break;
        }

        player.teleport(save);
    }

    @Override
    public SkinChangeError changePlayerSkin(Player player, String name) {
        Optional<PlayerProfile> profile = playerDisplayUtil.getCompletedPlayerProfile(name);
        if (profile.isEmpty()) return SkinChangeError.NO_VALID_PLAYER;

        //Gather data only if not cached
        Optional<TextureData> textureData;
        if (cachedData.containsKey(name.toLowerCase(Locale.ROOT)))
            textureData = Optional.ofNullable(cachedData.get(name.toLowerCase(Locale.ROOT)));
        else textureData = playerDisplayUtil.getTextureDataFromProfile(profile.get());

        if (textureData.isEmpty()) return SkinChangeError.NO_VALID_SKIN;

        //Cache for later usage
        cachedData.put(name.toLowerCase(Locale.ROOT), textureData.get());

        //Change skin
        changePlayerSkin(player, textureData.get());
        return SkinChangeError.NONE;
    }

    @Override
    public void getDataFromUrl(URL url, BiConsumer<SkinChangeError, TextureData> onFinishCallback) {
        if (cachedData.containsKey(url.toString())) {
            onFinishCallback.accept(SkinChangeError.NONE, cachedData.get(url.toString()));
            return;
        }

        client.generateUrl(url.toString())
                .exceptionally(throwable -> null)
                .thenAcceptAsync(skin -> {
                    if (skin == null) {
                        onFinishCallback.accept(SkinChangeError.NO_VALID_URL, null);
                        return;
                    }

                    TextureData data = new TextureData(skin.data.texture.value, skin.data.texture.signature);
                    cachedData.put(url.toString(), data); // Save for later usage
                    onFinishCallback.accept(SkinChangeError.NONE, data);
                }, runnable -> Bukkit.getScheduler().runTask(NameAndChatUtils.getInstance(), runnable));
    }

    @Override
    public void hidePlayer(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(NameAndChatUtils.getInstance(), player);
        }
    }

    @Override
    public void showPlayer(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(NameAndChatUtils.getInstance(), player);
        }
    }

    @Override
    public void hideAllPlayers(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(NameAndChatUtils.getInstance(), p);
        }
    }

    @Override
    public void showAllPlayers(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(NameAndChatUtils.getInstance(), p);
        }
    }

    @Override
    public void setName(Player player, String name) {
        name = name.substring(0, Math.min(name.length(), 16));
        //Save for reset
        if (!previousNames.containsKey(player.getUniqueId())) previousNames.put(player.getUniqueId(), player.getName());

        //Change name
        PlayerProfile profile = player.getPlayerProfile();
        profile.setName(name);
        player.setPlayerProfile(profile);
    }

    @Override
    public void showName(Player player) {
        invisibleNametagTeam.removePlayer(player);
        if (previousTeams.containsKey(player.getUniqueId())) {
            previousTeams.get(player.getUniqueId()).addPlayer(player);
            previousTeams.remove(player.getUniqueId());
        }
    }

    @Override
    public void hideName(Player player) {
        Team lastTeam = player.getScoreboard().getPlayerTeam(player);
        if (lastTeam != null && !previousTeams.containsKey(player.getUniqueId()) && !lastTeam.getName().equalsIgnoreCase(INVIS_NAMETAG_TEAM_NAME))
            previousTeams.put(player.getUniqueId(), lastTeam);
        invisibleNametagTeam.addPlayer(player);
    }

    @Override
    public void resetName(Player player) {
        if (!previousNames.containsKey(player.getUniqueId())) return;
        showName(player);
        setName(player, previousNames.get(player.getUniqueId()));
    }

    @Override
    public void resetSkin(Player player) {
        if (!previousSkins.containsKey(player.getUniqueId())) return;
        changePlayerSkin(player, previousSkins.get(player.getUniqueId()));
        previousSkins.remove(player.getUniqueId());
    }

    @Override
    public void reset(Player player) {
        showName(player);
        resetName(player);
        resetSkin(player);
    }

    @Override
    public String getBasePlayerName(Player player) {

        if (previousNames.containsKey(player.getUniqueId())) return previousNames.get(player.getUniqueId());
        return player.getName();
    }

}
