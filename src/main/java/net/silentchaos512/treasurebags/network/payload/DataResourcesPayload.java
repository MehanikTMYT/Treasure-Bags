package net.silentchaos512.treasurebags.network.payload;

import net.minecraft.resources.Identifier;

import java.util.Map;

public interface DataResourcesPayload<T> {
    Map<Identifier, T> values();
}
