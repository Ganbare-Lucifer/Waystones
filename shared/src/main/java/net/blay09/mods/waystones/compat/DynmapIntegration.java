package net.blay09.mods.waystones.compat;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.*;
import net.blay09.mods.waystones.api.event.WaystoneInitializedEvent;
import net.blay09.mods.waystones.api.event.WaystoneRemovedEvent;
import net.blay09.mods.waystones.api.event.WaystoneUpdatedEvent;
import net.blay09.mods.waystones.api.event.WaystonesLoadedEvent;
import net.minecraft.resources.ResourceLocation;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynmapIntegration extends DynmapCommonAPIListener {

    private final List<Runnable> scheduledJobsWhenReady = new ArrayList<>();

    private DynmapCommonAPI api;
    private MarkerSet waystoneMarkers;
    private MarkerSet sharestoneMarkers;

    public void createFromWaystones(List<Waystone> waystones) {
        if (waystoneMarkers != null) {
            waystoneMarkers.deleteMarkerSet();
        }
        if (sharestoneMarkers != null) {
            sharestoneMarkers.deleteMarkerSet();
        }
        waystoneMarkers = api.getMarkerAPI().createMarkerSet("waystones:waystones", "Waystones", Collections.emptySet(), false);
        sharestoneMarkers = api.getMarkerAPI().createMarkerSet("waystones:sharestones", "Sharestones", Collections.emptySet(), false);

        for (final var waystone : waystones) {
            addWaystoneMarker(waystone);
        }
    }

    public void addWaystoneMarker(Waystone waystone) {
        final var markerSet = WaystoneTypes.isSharestone(waystone.getWaystoneType()) ? sharestoneMarkers : waystoneMarkers;
        createWaystoneMarker(markerSet, waystone);
    }

    public void removeWaystoneMarker(Waystone waystone) {
        final var markerId = getMarkerId(waystone);
        final var marker = waystoneMarkers.findMarker(markerId);
        if (marker != null) {
            marker.deleteMarker();
        }
    }

    public DynmapIntegration() {
        DynmapCommonAPIListener.register(this);

        Balm.getEvents().onEvent(WaystonesLoadedEvent.class, this::onWaystonesLoaded);
        Balm.getEvents().onEvent(WaystoneInitializedEvent.class, this::onWaystoneInitialized);
        Balm.getEvents().onEvent(WaystoneUpdatedEvent.class, this::onWaystoneUpdated);
        Balm.getEvents().onEvent(WaystoneRemovedEvent.class, this::onWaystoneRemoved);
    }

    @Override
    public void apiEnabled(DynmapCommonAPI api) {
        this.api = api;

        for (Runnable scheduledJob : scheduledJobsWhenReady) {
            scheduledJob.run();
        }
        scheduledJobsWhenReady.clear();
    }

    @Override
    public void apiDisabled(DynmapCommonAPI api) {
        this.api = null;
    }

    private void onWaystoneInitialized(WaystoneInitializedEvent event) {
        runWhenDynmapIsReady(() -> addWaystoneMarker(event.getWaystone()));
    }

    private void onWaystoneUpdated(WaystoneUpdatedEvent event) {
        runWhenDynmapIsReady(() -> addWaystoneMarker(event.getWaystone()));
    }

    private void onWaystoneRemoved(WaystoneRemovedEvent event) {
        runWhenDynmapIsReady(() -> removeWaystoneMarker(event.getWaystone()));
    }

    private void onWaystonesLoaded(WaystonesLoadedEvent event) {
        final var waystones = event.getWaystoneManager().getWaystones()
                .filter(DynmapIntegration::isSupportedWaystoneType)
                .toList();
        runWhenDynmapIsReady(() -> createFromWaystones(waystones));
    }

    public static String getMarkerId(Waystone waystone) {
        return waystone.getWaystoneUid().toString();
    }

    public static Marker createWaystoneMarker(MarkerSet markerSet, Waystone waystone) {
        return markerSet.createMarker(getMarkerId(waystone),
                waystone.getName(),
                true,
                waystone.getDimension().location().toString(),
                waystone.getPos().getX(),
                waystone.getPos().getY(),
                waystone.getPos().getZ(),
                markerSet.getDefaultMarkerIcon(),
                false);
    }

    private static boolean isSupportedWaystoneType(Waystone waystone) {
        return isSupportedWaystoneType(waystone.getWaystoneType());
    }

    private static boolean isSupportedWaystoneType(ResourceLocation waystoneType) {
        return waystoneType.equals(WaystoneTypes.WAYSTONE) || WaystoneTypes.isSharestone(waystoneType);
    }

    private void runWhenDynmapIsReady(Runnable runnable) {
        if (api != null) {
            runnable.run();
        } else {
            scheduledJobsWhenReady.add(runnable);
        }
    }
}
