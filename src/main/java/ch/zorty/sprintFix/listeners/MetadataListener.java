package ch.zorty.sprintFix.listeners;

import ch.zorty.sprintFix.SprintFix;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MetadataListener extends PacketAdapter {

    SprintFix plugin;

    public MetadataListener(SprintFix plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.ENTITY_METADATA);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player p = event.getPlayer();

        int entity_id = event.getPacket().getIntegers().read(0);
        if(entity_id != p.getEntityId()) return;

        List<WrappedDataValue> metadata = event.getPacket().getDataValueCollectionModifier().read(0);
        List<WrappedDataValue> newMetadata = new ArrayList<>();

        for(WrappedDataValue value : metadata) {

            if (value.getIndex() == 0) {

                byte flags = (byte) value.getValue();

                // Check if player is exiting elytra
                if(plugin.getPlayerState(p).get_client_elytra() && (flags & 0x80) == 0) {

                    plugin.getPlayerState(p).set_last_metadata_packet(flags);
                    plugin.getPlayerState(p).set_client_elytra(false);
                    newMetadata.add(value);

                } else {

                    byte prev_flags = plugin.getPlayerState(p).get_last_metadata_packet();

                    // Check if content in relevant packets has changed (0x61 = 0x01 + 0x20 + 0x40)
                    if ((flags & 0x61) != (prev_flags & 0x61)) {
                        newMetadata.add(value);
                        plugin.getPlayerState(p).set_last_metadata_packet(flags);
                    }
                }

            // Client can do poses perfectly fine
            } else if(value.getIndex() != 6) {
                newMetadata.add(value);
            }
        }
        if(newMetadata.isEmpty()) {
            event.setCancelled(true);
            return;
        }
        event.getPacket().getDataValueCollectionModifier().write(0, newMetadata);
    }

}
