package net.silentchaos512.treasurebags.setup;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.treasurebags.TreasureBags;

import java.util.function.Supplier;

public class TbDataComponents {
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE, TreasureBags.MOD_ID);

    public static final Supplier<DataComponentType<Identifier>> BAG_TYPE = REGISTRAR.registerComponentType(
            "bag_type",
            builder -> builder
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC)
    );
}
