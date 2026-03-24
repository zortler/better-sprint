package ch.zorty.sprintFix.listeners;

import ch.zorty.sprintFix.SprintFix;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
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
        List<WrappedDataValue> metadata = event.getPacket().getDataValueCollectionModifier().read(0);
        List<WrappedDataValue> newMetadata = new ArrayList<>();
        for(WrappedDataValue value : metadata) {

            if (value.getIndex() == 0) {
                byte flags = (byte) value.getValue();

                // if paper sends the same index 0 metadata in a row, it's probably better to listen
                if(flags == plugin.getPlayerState(p).get_last_paper_packet()) {
                    plugin.getPlayerState(p).set_last_paper_packet(flags);
                    plugin.getPlayerState(p).set_last_metadata_packet(flags);
                    return;
                }
                plugin.getPlayerState(p).set_last_paper_packet(flags);
                byte prev_flags = plugin.getPlayerState(p).get_last_metadata_packet();

                // Check if content in relevant packets has changed (0xE1 = 0x01 + 0x20 + 0x40 + 0x80)
                if((flags & 0xE1) != (prev_flags & 0xE1)) {
                    newMetadata.add(value);
                    plugin.getPlayerState(p).set_last_metadata_packet(flags);
                }

            // Client can do poses fine mostly
            } else if(value.getIndex() == 6) {

                EnumWrappers.EntityPose pose = EnumWrappers.getEntityPoseConverter().getSpecific(value.getValue());
                if(plugin.getPlayerState(p).get_elytra_pose() && !pose.equals(EnumWrappers.EntityPose.FALL_FLYING)) {
                    plugin.getPlayerState(p).set_elytra_pose(false);
                    newMetadata.add(value);
                } else if(pose.equals(EnumWrappers.EntityPose.FALL_FLYING)) {
                    plugin.getPlayerState(p).set_elytra_pose(true);
                }

            } else {
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
