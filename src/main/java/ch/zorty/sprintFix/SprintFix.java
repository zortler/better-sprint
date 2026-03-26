package ch.zorty.sprintFix;

import ch.zorty.sprintFix.listeners.AttributeListener;
import ch.zorty.sprintFix.listeners.MetadataListener;
import ch.zorty.sprintFix.listeners.PlayerCommandListener;
import ch.zorty.sprintFix.listeners.QuitListener;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SprintFix extends JavaPlugin {

    private ProtocolManager protocolManager;
    private final Map<UUID, PlayerState> player_state = new HashMap<>();

    @Override
    public void onEnable() {

        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("ProtocolLib not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new AttributeListener(this));
        protocolManager.addPacketListener(new MetadataListener(this));
        protocolManager.addPacketListener(new PlayerCommandListener(this));
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        this.getLogger().info("SprintFix enabled");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        if(protocolManager != null) {
            protocolManager.removePacketListeners(this);
        }
        player_state.clear();
        this.getLogger().info("SprintFix disabled");
    }

    public void remove_player(Player p) {
        this.player_state.remove(p.getUniqueId());
    }

    public PlayerState getPlayerState(Player p) {
        PlayerState state = this.player_state.get(p.getUniqueId());
        if(state == null) {
            PlayerState new_state = new PlayerState();
            this.player_state.put(p.getUniqueId(), new_state);
            return new_state;
        }
        return state;
    }
}
