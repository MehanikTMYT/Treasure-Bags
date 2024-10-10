package net.silentchaos512.treasurebags.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.treasurebags.TreasureBags;

public class Const {
    public static final class Bags {
        // TODO: brewing bag? template/trim bag? egg bag?
        public static final ResourceLocation DEFAULT = TreasureBags.getId("default");
        public static final ResourceLocation DUNGEON = TreasureBags.getId("dungeon");
        public static final ResourceLocation ENDER = TreasureBags.getId("ender");
        public static final ResourceLocation FOOD = TreasureBags.getId("food");
        public static final ResourceLocation GAG = TreasureBags.getId("gag");
        public static final ResourceLocation INGOTS = TreasureBags.getId("ingots");
        public static final ResourceLocation LITERACY = TreasureBags.getId("literacy");
        public static final ResourceLocation NATURE = TreasureBags.getId("nature");
        public static final ResourceLocation NETHER = TreasureBags.getId("nether");
        public static final ResourceLocation OCEAN = TreasureBags.getId("ocean");
        public static final ResourceLocation PLAYER = TreasureBags.getId("player");
        public static final ResourceLocation SPAWN = TreasureBags.getId("spawn");
        public static final ResourceLocation STICKS_AND_STONES = TreasureBags.getId("sticks_and_stones");
        public static final ResourceLocation TEST = TreasureBags.getId("test");

        private Bags() {}
    }

    public static final class LootTables {
        public static final ResourceKey<LootTable> STARTING_INVENTORY = getLootTable("starting_inventory");

        public static final ResourceKey<LootTable> BAGS_DEFAULT = getLootTable("bags/default");
        public static final ResourceKey<LootTable> BAGS_DUNGEON = getLootTable("bags/dungeon");
        public static final ResourceKey<LootTable> BAGS_ENDER = getLootTable("bags/ender");
        public static final ResourceKey<LootTable> BAGS_FOOD = getLootTable("bags/food");
        public static final ResourceKey<LootTable> BAGS_GAG = getLootTable("bags/gag");
        public static final ResourceKey<LootTable> BAGS_INGOTS = getLootTable("bags/ingots");
        public static final ResourceKey<LootTable> BAGS_LITERACY = getLootTable("bags/literacy");
        public static final ResourceKey<LootTable> BAGS_NATURE = getLootTable("bags/nature");
        public static final ResourceKey<LootTable> BAGS_NETHER = getLootTable("bags/nether");
        public static final ResourceKey<LootTable> BAGS_OCEAN = getLootTable("bags/ocean");
        public static final ResourceKey<LootTable> BAGS_PLAYER = getLootTable("bags/player");
        public static final ResourceKey<LootTable> BAGS_SPAWN = getLootTable("bags/spawn");
        public static final ResourceKey<LootTable> BAGS_STICKS_AND_STONES = getLootTable("bags/sticks_and_stones");
        public static final ResourceKey<LootTable> BAGS_TEST = getLootTable("bags/test");

        private LootTables() {}
    }

    private static ResourceKey<LootTable> getLootTable(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, TreasureBags.getId(path));
    }
}
