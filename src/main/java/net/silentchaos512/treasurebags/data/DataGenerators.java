package net.silentchaos512.treasurebags.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.loot.BagDropLootModifier;

public final class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();

        gen.addProvider(true, new BagTypesProvider(gen));
        gen.addProvider(true, new ModLootTables(packOutput, lookupProvider));

        gen.addProvider(true, new GlobalLootModifierProvider(packOutput, lookupProvider, TreasureBags.MOD_ID) {
            @Override
            protected void start() {
                add("bag_drops", new BagDropLootModifier(new LootItemCondition[0]));
            }
        });
    }
}
