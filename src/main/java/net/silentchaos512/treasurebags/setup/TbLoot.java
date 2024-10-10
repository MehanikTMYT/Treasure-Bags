package net.silentchaos512.treasurebags.setup;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.loot.BagDropLootModifier;
import net.silentchaos512.treasurebags.loot.SelectBagByRarity;
import net.silentchaos512.treasurebags.loot.SetBagTypeFunction;

import java.util.function.Supplier;

public final class TbLoot {
    public static DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, TreasureBags.MOD_ID);

    public static DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TreasureBags.MOD_ID);

    public static final Supplier<LootItemFunctionType<SelectBagByRarity>> SELECT_BAG_BY_RARITY = LOOT_FUNCTIONS.register(
            "select_bag_by_rarity",
            () -> new LootItemFunctionType<>(SelectBagByRarity.CODEC)
    );
    public static final Supplier<LootItemFunctionType<SetBagTypeFunction>> SET_BAG_TYPE = LOOT_FUNCTIONS.register(
            "set_bag_type",
            () -> new LootItemFunctionType<>(SetBagTypeFunction.CODEC)
    );

    public static final Supplier<MapCodec<BagDropLootModifier>> BAG_DROPS = LOOT_MODIFIERS.register(
            "bag_drops",
            BagDropLootModifier.CODEC
    );

    private TbLoot() {}
}
