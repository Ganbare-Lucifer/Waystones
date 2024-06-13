package net.blay09.mods.waystones.network;

import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.blay09.mods.waystones.config.WaystonesConfigData;
import net.blay09.mods.waystones.network.message.*;

public class ModNetworking {

    public static void initialize(BalmNetworking networking) {
        networking.registerServerboundPacket(InventoryButtonMessage.TYPE, InventoryButtonMessage.class, InventoryButtonMessage::encode, InventoryButtonMessage::decode, InventoryButtonMessage::handle);
        networking.registerServerboundPacket(EditWaystoneMessage.TYPE, EditWaystoneMessage.class, EditWaystoneMessage::encode, EditWaystoneMessage::decode, EditWaystoneMessage::handle);
        networking.registerServerboundPacket(SelectWaystoneMessage.TYPE, SelectWaystoneMessage.class, SelectWaystoneMessage::encode, SelectWaystoneMessage::decode, SelectWaystoneMessage::handle);
        networking.registerServerboundPacket(SortWaystoneMessage.TYPE, SortWaystoneMessage.class, SortWaystoneMessage::encode, SortWaystoneMessage::decode, SortWaystoneMessage::handle);
        networking.registerServerboundPacket(RemoveWaystoneMessage.TYPE, RemoveWaystoneMessage.class, RemoveWaystoneMessage::encode, RemoveWaystoneMessage::decode, RemoveWaystoneMessage::handle);
        networking.registerServerboundPacket(RequestEditWaystoneMessage.TYPE, RequestEditWaystoneMessage.class, RequestEditWaystoneMessage::encode, RequestEditWaystoneMessage::decode, RequestEditWaystoneMessage::handle);
        networking.registerServerboundPacket(RequestManageWaystoneModifiersMessage.TYPE, RequestManageWaystoneModifiersMessage.class, RequestManageWaystoneModifiersMessage::encode, RequestManageWaystoneModifiersMessage::decode, RequestManageWaystoneModifiersMessage::handle);

        networking.registerClientboundPacket(UpdateWaystoneMessage.TYPE, UpdateWaystoneMessage.class, UpdateWaystoneMessage::encode, UpdateWaystoneMessage::decode, UpdateWaystoneMessage::handle);
        networking.registerClientboundPacket(WaystoneRemovedMessage.TYPE, WaystoneRemovedMessage.class, WaystoneRemovedMessage::encode, WaystoneRemovedMessage::decode, WaystoneRemovedMessage::handle);
        networking.registerClientboundPacket(KnownWaystonesMessage.TYPE, KnownWaystonesMessage.class, KnownWaystonesMessage::encode, KnownWaystonesMessage::decode, KnownWaystonesMessage::handle);
        networking.registerClientboundPacket(SortingIndexMessage.TYPE, SortingIndexMessage.class, SortingIndexMessage::encode, SortingIndexMessage::decode, SortingIndexMessage::handle);
        networking.registerClientboundPacket(TeleportEffectMessage.TYPE, TeleportEffectMessage.class, TeleportEffectMessage::encode, TeleportEffectMessage::decode, TeleportEffectMessage::handle);
        networking.registerClientboundPacket(PlayerWaystoneCooldownsMessage.TYPE, PlayerWaystoneCooldownsMessage.class, PlayerWaystoneCooldownsMessage::encode, PlayerWaystoneCooldownsMessage::decode, PlayerWaystoneCooldownsMessage::handle);

        SyncConfigMessage.register(SyncWaystonesConfigMessage.TYPE, SyncWaystonesConfigMessage.class, SyncWaystonesConfigMessage::new, WaystonesConfigData.class, WaystonesConfigData::new);
    }

}
