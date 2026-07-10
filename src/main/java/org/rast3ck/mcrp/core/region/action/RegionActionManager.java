package org.rast3ck.mcrp.core.region.action;

import org.rast3ck.mcrp.core.region.action.storage.RegionActionStorage;

import java.util.*;

public class RegionActionManager {

    private final Map<UUID, List<RegionAction>> actions = new HashMap<>();

    private final RegionActionStorage storage;

    public RegionActionManager(RegionActionStorage storage) {
        this.storage = storage;
    }

    public void register(RegionAction action) {

        if (action == null) return;

        List<RegionAction> regionActions = actions.computeIfAbsent(action.getRegionId(), k -> new ArrayList<>());

        if (!regionActions.contains(action)) {
            regionActions.add(action);
            storage.save(action);
        }
    }

    public List<RegionAction> getActions(UUID regionId, RegionActionType type) {
        return actions.getOrDefault(regionId, Collections.emptyList()).stream().filter(action -> action.getType() == type).toList();
    }

    public void remove(RegionAction action) {

        if (action == null) return;

        List<RegionAction> regionActions = actions.get(action.getRegionId());

        if (regionActions == null) return;

        if (regionActions.remove(action)) {
            storage.delete(action.getId());
        }

    }

    public void load() {
        for (RegionAction action : storage.findAll()) {
            actions.computeIfAbsent(action.getRegionId(), k -> new ArrayList<>()).add(action);
        }

    }

    public Collection<RegionAction> getAll() {
        return actions.values().stream().flatMap(Collection::stream).toList();
    }

}