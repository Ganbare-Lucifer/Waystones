package net.blay09.mods.waystones.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.waystones.Waystones;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Waystones.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeWaystonesClient {

    public NeoForgeWaystonesClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initialize(Waystones.MOD_ID, context, WaystonesClient::initialize);
    }
}
