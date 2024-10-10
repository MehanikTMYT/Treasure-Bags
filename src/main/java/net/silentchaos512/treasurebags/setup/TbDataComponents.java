package net.silentchaos512.treasurebags.setup;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.treasurebags.TreasureBags;

import java.util.function.Supplier;

public class TbDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(TreasureBags.MOD_ID);

    public static final Supplier<DataComponentType<ResourceLocation>> BAG_TYPE = REGISTRAR.registerComponentType(
            "bag_type",
            builder -> builder
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
    );
}
