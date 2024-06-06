package net.blay09.mods.waystones.network.message;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.api.Waystone;
import net.blay09.mods.waystones.api.event.WaystoneUpdateReceivedEvent;
import net.blay09.mods.waystones.core.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class UpdateWaystoneMessage implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateWaystoneMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Waystones.MOD_ID,
            "update_waystone"));

    private final Waystone waystone;

    public UpdateWaystoneMessage(Waystone waystone) {
        this.waystone = waystone;
    }

    public static void encode(RegistryFriendlyByteBuf buf, UpdateWaystoneMessage message) {
        WaystoneImpl.write(buf, message.waystone);
    }

    public static UpdateWaystoneMessage decode(RegistryFriendlyByteBuf buf) {
        return new UpdateWaystoneMessage(WaystoneImpl.read(buf));
    }

    public static void handle(Player player, UpdateWaystoneMessage message) {
        WaystoneManagerImpl.get(player.getServer()).updateWaystone(message.waystone);
        Balm.getEvents().fireEvent(new WaystoneUpdateReceivedEvent(message.waystone));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
