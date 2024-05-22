package net.blay09.mods.waystones.network.message;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.api.Waystone;
import net.blay09.mods.waystones.api.event.WaystonesListReceivedEvent;
import net.blay09.mods.waystones.api.WaystoneTypes;
import net.blay09.mods.waystones.core.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnownWaystonesMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<KnownWaystonesMessage> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(Waystones.MOD_ID,
            "known_waystones"));

    private final ResourceLocation type;
    private final Collection<Waystone> waystones;

    public KnownWaystonesMessage(ResourceLocation type, Collection<Waystone> waystones) {
        this.type = type;
        this.waystones = waystones;
    }

    public static void encode(RegistryFriendlyByteBuf buf, KnownWaystonesMessage message) {
        buf.writeResourceLocation(message.type);
        buf.writeShort(message.waystones.size());
        for (Waystone waystone : message.waystones) {
            WaystoneImpl.write(buf, waystone);
        }
    }

    public static KnownWaystonesMessage decode(RegistryFriendlyByteBuf buf) {
        ResourceLocation type = buf.readResourceLocation();
        int waystoneCount = buf.readShort();
        List<Waystone> waystones = new ArrayList<>();
        for (int i = 0; i < waystoneCount; i++) {
            waystones.add(WaystoneImpl.read(buf));
        }
        return new KnownWaystonesMessage(type, waystones);
    }

    public static void handle(Player player, KnownWaystonesMessage message) {
        final var waystones = message.waystones.stream().toList(); // backwards compat for event expecting a List
        if (message.type.equals(WaystoneTypes.WAYSTONE)) {
            InMemoryPlayerWaystoneData playerWaystoneData = (InMemoryPlayerWaystoneData) PlayerWaystoneManager.getPlayerWaystoneData(BalmEnvironment.CLIENT);
            playerWaystoneData.setWaystones(message.waystones);
        }

        Balm.getEvents().fireEvent(new WaystonesListReceivedEvent(message.type, waystones));

        for (Waystone waystone : message.waystones) {
            WaystoneManagerImpl.get(player.getServer()).updateWaystone(waystone);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
