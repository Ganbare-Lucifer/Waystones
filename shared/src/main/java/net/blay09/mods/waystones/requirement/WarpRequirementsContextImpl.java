package net.blay09.mods.waystones.requirement;

import net.blay09.mods.waystones.api.WaystoneTeleportContext;
import net.blay09.mods.waystones.api.requirement.WarpRequirement;
import net.blay09.mods.waystones.api.requirement.WarpRequirementsContext;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WarpRequirementsContextImpl implements WarpRequirementsContext {

    private final Map<ResourceLocation, WarpRequirement> requirements = new HashMap<>();
    private final WaystoneTeleportContext context;

    public WarpRequirementsContextImpl(WaystoneTeleportContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T extends WarpRequirement, P> void apply(ConfiguredRequirementModifier<T, P> configuredModifier) {
        for (final var condition : configuredModifier.conditions()) {
            if (!matchesCondition(condition)) {
                return;
            }
        }

        final var requirement = configuredModifier.requirement();
        final var modifier = requirement.modifier();
        final var parameters = requirement.parameters();
        var existing = (T) requirements.get(modifier.getRequirementType());
        if (existing == null) {
            existing = WarpModifierRegistry.<T>getRequirementType(modifier.getRequirementType()).createInstance();
        }
        requirements.put(modifier.getRequirementType(), modifier.apply(existing, this, parameters));
    }

    public float getContextValue(ResourceLocation id) {
        final var resolver = WarpModifierRegistry.getVariableResolver(id);
        if (resolver != null) {
            return resolver.resolve(context);
        }

        return 0f;
    }

    public <P> boolean matchesCondition(ConfiguredCondition<P> configuredCondition) {
        return configuredCondition.resolver().matches(context, configuredCondition.parameters());
    }

    public WarpRequirement resolve() {
        if (requirements.isEmpty()) {
            return NoRequirement.INSTANCE;
        } else if (requirements.size() == 1) {
            return requirements.values().iterator().next();
        }
        return new CombinedRequirement(requirements.values());
    }
}
