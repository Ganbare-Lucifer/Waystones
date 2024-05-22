package net.blay09.mods.waystones.api.trait;

import net.blay09.mods.waystones.api.Waystone;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IAttunementItem {
    Optional<Waystone> getWaystoneAttunedTo(@Nullable MinecraftServer server, @Nullable Player player, ItemStack itemStack);

    void setWaystoneAttunedTo(ItemStack itemStack, @Nullable Waystone waystone);
}
