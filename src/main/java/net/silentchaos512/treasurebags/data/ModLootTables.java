package net.silentchaos512.treasurebags.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.treasurebags.lib.Const;
import net.silentchaos512.treasurebags.lib.StandardEntityGroups;
import net.silentchaos512.treasurebags.loot.SelectBagByRarity;
import net.silentchaos512.treasurebags.loot.SetBagTypeFunction;
import net.silentchaos512.treasurebags.setup.TbItems;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModLootTables extends LootTableProvider {
    public ModLootTables(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(
                packOutput,
                Collections.emptySet(),
                VanillaLootTableProvider.create(packOutput, lookupProvider).getTables(),
                lookupProvider
        );
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return ImmutableList.of(
                new SubProviderEntry(Entities::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(Gifts::new, LootContextParamSets.GIFT)
        );
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        // Don't validate against built-in tables
    }

    private static LootPoolSingletonContainer.Builder<?> treasureBag(ResourceLocation bagType) {
        return LootItem.lootTableItem(TbItems.TREASURE_BAG)
                .apply(SetBagTypeFunction.builder(bagType));
    }

    private static LootPoolSingletonContainer.Builder<?> bagOfRarity(Rarity rarity) {
        return LootItem.lootTableItem(TbItems.TREASURE_BAG)
                .apply(SelectBagByRarity.builder(rarity));
    }

    @Nonnull
    private static LootItemConditionalFunction.Builder<?> setName(Component text) {
        return SetNameFunction.setName(text, SetNameFunction.Target.CUSTOM_NAME);
    }

    private static LootItemConditionalFunction.Builder<?> setCount(int count) {
        return SetItemCountFunction.setCount(ConstantValue.exactly(count));
    }

    private static LootItemConditionalFunction.Builder<?> setCount(int min, int max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    private static class Gifts implements LootTableSubProvider {
        private final HolderLookup.Provider registries;

        public Gifts(HolderLookup.Provider registries) {
            this.registries = registries;
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.STARTING_INVENTORY, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(treasureBag(Const.Bags.SPAWN))
                    )
            );

            generateSpawnBag(consumer);

            generateDungeonBag(consumer);
            generateEnderBag(consumer);
            generateFoodBag(consumer);
            generateGagBag(consumer);
            generateIngotsBag(consumer);
            generateLiteracyBag(consumer);
            generateNatureBag(consumer);
            generateNetherBag(consumer);
            generateOceanBag(consumer);
            generatePlayerBag(consumer);
            generateSticksAndStonesBag(consumer);

            generateDefaultBag(consumer);
            generateTestBag(consumer);
        }

        private static void generateSpawnBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_SPAWN, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.STONE_AXE)
                                    .apply(setName(Component.literal("Crude Stone Axe")))
                                    .apply(SetComponentsFunction.setComponent(DataComponents.MAX_DAMAGE, 72))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.STONE_PICKAXE)
                                    .apply(setName(Component.literal("Crude Stone Pickaxe")))
                                    .apply(SetComponentsFunction.setComponent(DataComponents.MAX_DAMAGE, 72))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.STONE_SHOVEL)
                                    .apply(setName(Component.literal("Crude Stone Shovel")))
                                    .apply(SetComponentsFunction.setComponent(DataComponents.MAX_DAMAGE, 72))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.OAK_LOG)
                                    .apply(setCount(16))
                            )
                            .add(lootItem(Items.SPRUCE_LOG)
                                    .apply(setCount(16))
                            )
                            .add(lootItem(Items.BIRCH_LOG)
                                    .apply(setCount(16))
                            )
                            .add(lootItem(Items.JUNGLE_LOG)
                                    .apply(setCount(16))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(TagEntry.expandTag(ItemTags.BEDS))
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.APPLE)
                                    .apply(setCount(10))
                            )
                            .add(lootItem(Items.BREAD)
                                    .apply(setCount(8))
                            )
                            .add(lootItem(Items.CHICKEN)
                                    .apply(setCount(8))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.TORCH)
                                    .apply(setCount(16, 32))
                            )
                    )
            );
        }

        private static void generateDungeonBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_DUNGEON, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(NestedLootTable.lootTableReference(BuiltInLootTables.SIMPLE_DUNGEON))
                    )
            );
        }

        private static void generateEnderBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_ENDER, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .setBonusRolls(UniformGenerator.between(0, 1))
                            .add(lootItem(Items.ENDER_PEARL)
                                    .setWeight(20)
                            )
                            .add(lootItem(Items.ENDER_EYE)
                                    .setWeight(5)
                            )
                            .add(lootItem(Items.ENDER_CHEST)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.GRASS_BLOCK)
                                    .setWeight(8)
                            )
                            .add(lootItem(Items.END_STONE)
                                    .setWeight(5)
                            )
                            .add(lootItem(Items.PURPUR_BLOCK)
                                    .setWeight(4)
                            )
                            .add(lootItem(Items.END_ROD)
                                    .setWeight(2)
                            )
                            .add(lootItem(Items.CHORUS_FLOWER)
                                    .setWeight(1)
                            )
                    )
            );
        }

        private static void generateFoodBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_FOOD, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(2, 3))
                            .add(lootItem(Items.COOKED_COD)
                                    .setWeight(10)
                                    .apply(setCount(2, 4))
                            )
                            .add(lootItem(Items.COOKED_SALMON)
                                    .setWeight(10)
                                    .apply(setCount(2, 4))
                            )
                            .add(lootItem(Items.COOKED_PORKCHOP)
                                    .setWeight(7)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.COOKED_BEEF)
                                    .setWeight(7)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.COOKED_CHICKEN)
                                    .setWeight(8)
                                    .apply(setCount(2, 4))
                            )
                            .add(lootItem(Items.BREAD)
                                    .setWeight(12)
                                    .apply(setCount(2, 6))
                            )
                            .add(lootItem(Items.GOLDEN_CARROT)
                                    .setWeight(3)
                                    .apply(setCount(1, 2))
                            )
                            .add(lootItem(Items.GOLDEN_APPLE)
                                    .setWeight(1)
                            )
                    )
            );
        }

        private void generateGagBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
            consumer.accept(Const.LootTables.BAGS_GAG, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.HOPPER)
                                    .apply(setName(Component.literal("Franz")))
                            )
                            .add(lootItem(Items.LIGHT_BLUE_GLAZED_TERRACOTTA)
                                    .apply(setName(Component.literal("Loginar Egg?")))
                            )
                            .add(lootItem(Items.PINK_WOOL)
                                    .apply(setName(Component.literal("Larry's Wool")))
                            )
                            .add(lootItem(Items.STICK)
                                    .apply(setCount(2))
                                    .apply(setName(Component.literal("Drumsticks")))
                            )
                            .add(lootItem(Items.RECOVERY_COMPASS)
                                    .apply(SetLoreFunction.setLore().addLine(Component.literal("You're too good to need this, right?")))
                            )
                            .add(lootItem(Items.FURNACE_MINECART)
                                    .apply(SetLoreFunction.setLore().addLine(Component.literal("Choo Choo!")))
                            )
                            .add(lootItem(Items.GOLDEN_PICKAXE)
                                    .apply(SetLoreFunction.setLore().addLine(Component.literal("Don't blink")))
                                    .apply(SetComponentsFunction.setComponent(DataComponents.MAX_DAMAGE, 1))
                                    .apply(new SetEnchantmentsFunction.Builder()
                                            .withEnchantment(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), ConstantValue.exactly(1))
                                            .withEnchantment(registrylookup.getOrThrow(Enchantments.UNBREAKING), ConstantValue.exactly(3))
                                            .withEnchantment(registrylookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(5))
                                            .withEnchantment(registrylookup.getOrThrow(Enchantments.MENDING), ConstantValue.exactly(1))
                                    )
                            )
                    )
            );
        }

        private static void generateIngotsBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_INGOTS, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .setBonusRolls(UniformGenerator.between(0, 1))
                            .add(lootItem(Items.IRON_INGOT)
                                    .setWeight(50)
                                    .apply(setCount(2, 5))
                            )
                            .add(lootItem(Items.COPPER_INGOT)
                                    .setWeight(35)
                                    .apply(setCount(3, 9))
                            )
                            .add(lootItem(Items.GOLD_INGOT).setWeight(20)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.NETHERITE_INGOT)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(lootItem(Items.IRON_INGOT)
                                    .setWeight(5)
                                    .apply(setCount(1, 3))
                            )
                            .add(TagEntry.expandTag(Tags.Items.INGOTS)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.COAL)
                                    .setWeight(20)
                                    .apply(setCount(5, 10))
                            )
                            .add(lootItem(Items.COAL_BLOCK)
                                    .setWeight(5)
                                    .apply(setCount(1, 2))
                            )
                            .add(lootItem(Items.LAVA_BUCKET)
                                    .setWeight(1)
                            )
                    )
            );
        }

        private static void generateLiteracyBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_LITERACY, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(lootItem(Items.BOOK)
                                    .setWeight(12)
                                    .apply(setCount(2, 4))
                            )
                            .add(lootItem(Items.PAPER)
                                    .setWeight(15)
                                    .apply(setCount(4, 10))
                            )
                            .add(lootItem(Items.BOOKSHELF)
                                    .setWeight(3)
                                    .apply(setCount(1, 2))
                            )
                            .add(lootItem(Items.WRITABLE_BOOK)
                                    .setWeight(1)
                            )
                    )
                    // TODO: enchanted books
                    .withPool(LootPool.lootPool()
                            .when(LootItemRandomChanceCondition.randomChance(0.5f))
                            .add(lootItem(Items.BOOK)
                                    .apply(setName(Component.literal("That Time I Was Slapped With a Salmon, Vol. 4")))
                            )
                            .add(lootItem(Items.BOOK)
                                    .apply(setName(Component.literal("The Adventures of Tardigrade Man")))
                            )
                            .add(lootItem(Items.BOOK)
                                    .apply(setName(Component.literal("Reading For Dummies")))
                            )
                            .add(lootItem(Items.BOOK)
                                    .apply(setName(Component.literal("The Story of Larry the Sheep")))
                            )
                    )
            );
        }

        private static void generateNatureBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_NATURE, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.SUGAR_CANE)
                                    .setWeight(10)
                                    .apply(setCount(1, 4))
                            )
                            .add(lootItem(Items.SHORT_GRASS)
                                    .setWeight(12)
                                    .apply(setCount(4, 12))
                            )
                            .add(lootItem(Items.FERN)
                                    .setWeight(8)
                                    .apply(setCount(2, 5))
                            )
                            .add(lootItem(Items.VINE)
                                    .setWeight(7)
                                    .apply(setCount(1, 4))
                            )
                            .add(lootItem(Items.LILY_PAD)
                                    .setWeight(6)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.CACTUS)
                                    .setWeight(5)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.PUMPKIN)
                                    .setWeight(3)
                                    .apply(setCount(1, 2))
                            )
                            .add(lootItem(Items.MELON)
                                    .setWeight(3)
                                    .apply(setCount(1, 2))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(lootItem(Items.CARROT)
                                    .setWeight(5)
                                    .apply(setCount(1, 4))
                            )
                            .add(lootItem(Items.POTATO)
                                    .setWeight(5)
                                    .apply(setCount(1, 4))
                            )
                            .add(lootItem(Items.COCOA_BEANS)
                                    .setWeight(2)
                                    .apply(setCount(1, 2))
                            )
                            .add(
                                    TagEntry.expandTag(Tags.Items.SEEDS)
                                            .setWeight(10)
                                            .apply(setCount(1, 4))
                            )
                            .add(
                                    TagEntry.expandTag(Tags.Items.CROPS)
                                            .setWeight(5)
                                            .apply(setCount(1, 4))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(
                                    TagEntry.expandTag(ItemTags.SAPLINGS)
                                            .apply(setCount(2, 4))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(
                                    TagEntry.expandTag(ItemTags.FLOWERS)
                                            .apply(setCount(2, 4))
                            )
                    )
            );
        }

        private void generateNetherBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_NETHER, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(lootItem(Items.NETHERRACK)
                                    .setWeight(10)
                                    .apply(setCount(3, 12))
                            )
                            .add(lootItem(Items.SOUL_SAND)
                                    .setWeight(7)
                                    .apply(setCount(2, 6))
                            )
                            .add(lootItem(Items.BASALT)
                                    .setWeight(5)
                                    .apply(setCount(3, 9))
                            )
                            .add(lootItem(Items.QUARTZ)
                                    .setWeight(3)
                                    .apply(setCount(2, 7))
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.GOLD_NUGGET)
                                    .setWeight(5)
                                    .apply(setCount(8, 28))
                            )
                            .add(lootItem(Items.BLAZE_ROD)
                                    .setWeight(10)
                                    .apply(setCount(1, 2))
                            )
                            .add(lootItem(Items.MAGMA_CREAM)
                                    .setWeight(6)
                                    .apply(setCount(2, 3))
                            )
                            .add(lootItem(Items.GHAST_TEAR)
                                    .setWeight(3)
                            )
                            .add(lootItem(Items.WITHER_SKELETON_SKULL)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(
                                    EmptyLootItem.emptyItem()
                                            .setWeight(99)
                            )
                            .add(lootItem(Items.ANCIENT_DEBRIS)
                                    .setWeight(1)
                            )
                    )
            );
        }

        private void generateOceanBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_OCEAN, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.KELP)
                                    .setWeight(6)
                                    .apply(setCount(6, 18))
                            )
                            .add(lootItem(Items.SEAGRASS)
                                    .setWeight(3)
                                    .apply(setCount(4, 10))
                            )
                            .add(lootItem(Items.SEA_PICKLE)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.COPPER_INGOT)
                                    .setWeight(5)
                                    .apply(setCount(1, 3))
                            )
                            .add(lootItem(Items.WATER_BUCKET)
                                    .setWeight(1)
                            )
                            .add(lootItem(Items.COD_BUCKET)
                                    .setWeight(1)
                            )
                            .add(lootItem(Items.TROPICAL_FISH_BUCKET)
                                    .setWeight(1)
                            )
                            .add(lootItem(Items.NAUTILUS_SHELL)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(299)
                            )
                            .add(lootItem(Items.TRIDENT)
                                    .setWeight(1)
                            )
                    )
            );
        }

        private static void generatePlayerBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_PLAYER, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(lootItem(Items.GOLDEN_APPLE))
                    )
            );
        }

        private static void generateSticksAndStonesBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_STICKS_AND_STONES, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.STICK)
                                    .setWeight(24)
                                    .apply(setCount(4, 8))
                            )
                            .add(TagEntry.expandTag(Tags.Items.RODS)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .setBonusRolls(UniformGenerator.between(0, 1))
                            .add(lootItem(Items.COBBLESTONE)
                                    .setWeight(36)
                                    .apply(setCount(6, 12))
                            )
                            .add(lootItem(Items.STONE)
                                    .setWeight(24)
                                    .apply(setCount(3, 7))
                            )
                            .add(lootItem(Items.ANDESITE)
                                    .setWeight(15)
                                    .apply(setCount(4, 8))
                            )
                            .add(lootItem(Items.DIORITE)
                                    .setWeight(15)
                                    .apply(setCount(4, 8))
                            )
                            .add(lootItem(Items.GRANITE)
                                    .setWeight(15)
                                    .apply(setCount(4, 8))
                            )
                            .add(lootItem(Items.DIAMOND)
                                    .setWeight(1)
                            )
                            .add(lootItem(Items.EMERALD)
                                    .setWeight(1)
                            )
                    )
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.BONE)
                                    .setWeight(10)
                                    .apply(setCount(2, 4))
                            )
                            .add(lootItem(Items.BONE_MEAL)
                                    .setWeight(4)
                                    .apply(setCount(3, 8))
                            )
                            .add(lootItem(Items.BONE_BLOCK)
                                    .setWeight(1)
                                    .apply(setCount(1, 2))
                            )
                    )
            );
        }

        private static void generateDefaultBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_DEFAULT, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.STICK)
                                    .apply(setCount(4))
                            )
                    )
            );
        }

        private static void generateTestBag(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(Const.LootTables.BAGS_TEST, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootItem(Items.IRON_INGOT)
                                    .apply(setCount(5))
                            )
                            .add(lootItem(Items.GOLD_INGOT)
                                    .apply(setCount(2))
                            )
                            .add(lootItem(Items.DIAMOND))
                    )
            );
        }
    }

    @NotNull
    private static LootPoolSingletonContainer.Builder<?> lootItem(Item item) {
        return LootItem.lootTableItem(item);
    }

    private static class Entities extends EntityLootSubProvider {
        protected Entities(HolderLookup.Provider registries) {
            super(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        public void generate() {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(StandardEntityGroups.BOSS.getLootTable(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .setRolls(ConstantValue.exactly(2))
                            .add(bagOfRarity(Rarity.COMMON)
                                    .setWeight(3)
                            )
                            .add(bagOfRarity(Rarity.UNCOMMON)
                                    .setWeight(6)
                            )
                            .add(bagOfRarity(Rarity.RARE)
                                    .setWeight(10)
                            )
                            .add(bagOfRarity(Rarity.EPIC)
                                    .setWeight(7)
                            )
                    )
            );
            consumer.accept(StandardEntityGroups.HOSTILE.getLootTable(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(200)
                            )
                            .add(bagOfRarity(Rarity.COMMON)
                                    .setWeight(10)
                            )
                            .add(bagOfRarity(Rarity.UNCOMMON)
                                    .setWeight(5)
                            )
                            .add(bagOfRarity(Rarity.RARE)
                                    .setWeight(2)
                            )
                            .add(bagOfRarity(Rarity.EPIC)
                                    .setWeight(1)
                            )
                    )
            );
            consumer.accept(StandardEntityGroups.PEACEFUL.getLootTable(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(500)
                            )
                            .add(bagOfRarity(Rarity.COMMON)
                                    .setWeight(12)
                            )
                            .add(bagOfRarity(Rarity.UNCOMMON)
                                    .setWeight(6)
                            )
                    )
            );
            consumer.accept(StandardEntityGroups.PLAYER.getLootTable(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .add(treasureBag(Const.Bags.PLAYER))
                    )
            );
        }
    }
}
