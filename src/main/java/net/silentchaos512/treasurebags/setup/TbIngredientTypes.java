package net.silentchaos512.treasurebags.setup;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.crafting.ingredient.TreasureBagIngredient;

import java.util.function.Supplier;

public class TbIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, TreasureBags.MOD_ID);

    public static final Supplier<IngredientType<TreasureBagIngredient>> TREASURE_BAG = REGISTRAR.register(
            "bag",
            () -> new IngredientType<>(
                    TreasureBagIngredient.CODEC,
                    TreasureBagIngredient.STREAM_CODEC
            )
    );
}
