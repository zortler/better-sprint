package ch.zorty.sprintFix.listeners;

import ch.zorty.sprintFix.MovementSpeedAttribute;
import ch.zorty.sprintFix.SprintFix;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import com.comphenix.protocol.wrappers.WrappedAttributeModifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AttributeListener extends PacketAdapter {

    SprintFix plugin;

    public AttributeListener(SprintFix plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.UPDATE_ATTRIBUTES);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        int entity_id = event.getPacket().getIntegers().read(0);
        if(entity_id != event.getPlayer().getEntityId()) return;

        List<WrappedAttribute> attributes = event.getPacket().getAttributeCollectionModifier().read(0);
        List<WrappedAttribute> attr_copy = new ArrayList<>();
        for(WrappedAttribute attr : attributes) {
            if(attr.getAttributeKey().equalsIgnoreCase("movement_speed")) {
                MovementSpeedAttribute msa = new MovementSpeedAttribute(attr.getBaseValue());
                Set<WrappedAttributeModifier> modifiers = attr.getModifiers();
                for(WrappedAttributeModifier mod : modifiers) {
                    msa.addModifier(mod.getKey().getFullKey(), mod.getAmount(), mod.getOperation().getId());
                }

                MovementSpeedAttribute last_msa = plugin.getPlayerState(event.getPlayer()).get_last_mvmt_speed_packet();
                if(last_msa == null) {
                    attr_copy.add(attr);
                    plugin.getPlayerState(event.getPlayer()).set_last_mvmt_speed_packet(msa);
                } else {
                    // Check if movement_speed attribute changed except for removing sprint
                    if(!msa.equals(last_msa) && !msa.equals(last_msa.copy_without_sprint())) {
                        attr_copy.add(attr);
                        plugin.getPlayerState(event.getPlayer()).set_last_mvmt_speed_packet(msa);
                    }
                }
            } else {
                attr_copy.add(attr);
            }
        }
        if(attr_copy.isEmpty()) {
            event.setCancelled(true);
        } else {
            event.getPacket().getAttributeCollectionModifier().write(0, attr_copy);
        }
    }
}
