package net.silentchaos512.treasurebags.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.api.IEntityGroup;
import net.silentchaos512.treasurebags.lib.BagType;
import net.silentchaos512.treasurebags.lib.StandardEntityGroups;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class BagTypeBuilder {
    final ResourceLocation bagTypeId;
    private final String group;
    private final Rarity rarity;
    private final ResourceKey<LootTable> lootTable;
    private final Collection<IEntityGroup> dropsFromGroups = new LinkedHashSet<>();
    private boolean noMobDrops = false;
    private Component displayName;
    private int bagColor;
    private int bagOverlayColor;
    private int bagStringColor;

    public BagTypeBuilder(ResourceLocation bagTypeId, String group, Rarity rarity, ResourceKey<LootTable> lootTable) {
        this.bagTypeId = bagTypeId;
        this.group = group;
        this.rarity = rarity;
        this.lootTable = lootTable;
        this.displayName = Component.translatable(String.format("bag.%s.%s", this.bagTypeId.getNamespace(), this.bagTypeId.getPath()));
    }

    /**
     * Makes the bag type drop from no mob groups and suppresses the warning associated with that
     *
     * @return The builder
     */
    public BagTypeBuilder noMobDrops() {
        this.dropsFromGroups.clear();
        this.noMobDrops = true;
        return this;
    }

    /**
     * Makes the bag type drop from all mob groups
     *
     * @return The builder
     */
    public BagTypeBuilder dropsFromAllMobs() {
        return dropsFrom(StandardEntityGroups.values());
    }

    public BagTypeBuilder dropsFrom(IEntityGroup... groups) {
        this.dropsFromGroups.addAll(Arrays.asList(groups));
        return this;
    }

    public BagTypeBuilder displayName(Component text) {
        this.displayName = text;
        return this;
    }

    public BagTypeBuilder colors(int bag, int overlay, int string) {
        this.bagColor = bag;
        this.bagOverlayColor = overlay;
        this.bagStringColor = string;
        return this;
    }

    public BagTypeBuilder colors(Color bag, Color overlay, Color string) {
        return colors(bag.getColor(), overlay.getColor(), string.getColor());
    }

    public void validate() {
        if (dropsFromGroups.isEmpty() && !noMobDrops) {
            BagTypesProvider.LOGGER.warn("Bag type '{}' has no dropsFromGroups. This may be unintentional.", this.bagTypeId);
        }
    }

    public JsonObject serialize() {
        validate();

        BagType bagType = new BagType(
                this.group,
                this.rarity,
                List.copyOf(this.dropsFromGroups),
                this.bagColor,
                this.bagOverlayColor,
                this.bagStringColor,
                this.displayName,
                this.lootTable,
                true
        );

        var jsonElementDataResult = BagType.CODEC.encodeStart(JsonOps.INSTANCE, bagType);
        if (jsonElementDataResult.isError()) {
            TreasureBags.LOGGER.error("Something went wrong serializing bag type \"{}\"", this.bagTypeId);
        }
        return jsonElementDataResult.getOrThrow().getAsJsonObject();
    }
}
