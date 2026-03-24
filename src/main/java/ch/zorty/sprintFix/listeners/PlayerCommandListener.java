package ch.zorty.sprintFix.listeners;

import ch.zorty.sprintFix.SprintFix;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class PlayerCommandListener extends PacketAdapter {

    SprintFix plugin;

    public PlayerCommandListener(SprintFix plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.ENTITY_ACTION);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPacket().getPlayerActions().read(0).equals(EnumWrappers.PlayerAction.START_FALL_FLYING)) {
            plugin.getPlayerState(event.getPlayer()).set_client_elytra(true);
        }

    }
}
