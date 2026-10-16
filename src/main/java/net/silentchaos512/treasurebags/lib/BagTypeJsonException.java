package net.silentchaos512.treasurebags.lib;

import net.minecraft.resources.Identifier;

public class BagTypeJsonException extends RuntimeException {
    public BagTypeJsonException(Identifier name, String packName, Throwable cause) {
        super("Error loading \"" + name + "\" from pack \"" + packName + "\": " + cause.getMessage(), cause);
    }
}
