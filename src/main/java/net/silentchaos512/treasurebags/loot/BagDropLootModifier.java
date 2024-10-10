package net.silentchaos512.treasurebags.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.setup.EntityGroups;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BagDropLootModifier extends LootModifier {
    public static final Supplier<MapCodec<BagDropLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(
                    inst -> codecStart(inst).apply(inst, BagDropLootModifier::new)
            )
    );

    public BagDropLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        TreasureBags.LOGGER.debug("{}: {}", context.getQueriedLootTableId(), context.getParamOrNull(LootContextParams.THIS_ENTITY));

        Entity thisEntity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (thisEntity == null) {
            return generatedLoot;
        }

        ObjectArrayList<ItemStack> ret = new ObjectArrayList<>(generatedLoot);

        var server = context.getLevel().getServer();
        var lootTableLookup = server.reloadableRegistries().lookup().lookup(Registries.LOOT_TABLE).orElseThrow();
        EntityGroups.forEachGroup(thisEntity, (group, entity) -> {
            lootTableLookup.get(group.getLootTable())
                    .ifPresent(lootTable -> {
                        // Must use raw method to prevent stack overflow
                        //noinspection deprecation
                        lootTable.value().getRandomItemsRaw(context, ret::add);
                    });
        });

        return ret;
    }
}
