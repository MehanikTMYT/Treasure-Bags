package net.silentchaos512.treasurebags.lib;

import net.minecraft.resources.ResourceLocation;

public class BagTypeJsonException extends RuntimeException {
    public BagTypeJsonException(ResourceLocation name, String packName, Throwable cause) {
        super("Error loading \"" + name + "\" from pack \"" + packName + "\": " + cause.getMessage(), cause);
    }
}
