package net.blay09.mods.waystones.core;

import net.blay09.mods.waystones.api.Waystone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPlayerWaystoneData {
    void activateWaystone(Player player, Waystone waystone);
    boolean isWaystoneActivated(Player player, Waystone waystone);
    void deactivateWaystone(Player player, Waystone waystone);
    long getCooldownUntil(Player player, ResourceLocation key);
    void setCooldownUntil(Player player, ResourceLocation key, long timeStamp);
    List<UUID> getSortingIndex(Player player);
    List<UUID> ensureSortingIndex(Player player, Collection<Waystone> waystones);
    void setSortingIndex(Player player, List<UUID> sortingIndex);
    Collection<Waystone> getWaystones(Player player);
    void sortWaystoneAsFirst(Player player, UUID waystoneUid);
    void sortWaystoneAsLast(Player player, UUID waystoneUid);
    void sortWaystoneSwap(Player player, UUID waystoneUid, UUID otherWaystoneUid);

    Map<ResourceLocation, Long> getCooldowns(Player player);

    void resetCooldowns(Player player);
}
