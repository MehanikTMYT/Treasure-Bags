package net.silentchaos512.treasurebags.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.api.IEntityGroup;

public class TagEntityGroup implements IEntityGroup {
    private final ResourceLocation groupId;
    private final TagKey<EntityType<?>> entityTypeTag;
    private final ResourceKey<LootTable> lootTable;

    public TagEntityGroup(ResourceLocation groupId, TagKey<EntityType<?>> entityTypeTag) {
        this.groupId = groupId;
        this.entityTypeTag = entityTypeTag;
        var lootTablePathSuffix = this.groupId.toString().replace(':', '.');
        var lootTableId = TreasureBags.getId("entity_group/" + lootTablePathSuffix);
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, lootTableId);
    }

    public static TagEntityGroup withDefaultName(ResourceLocation tagKey) {
        return new TagEntityGroup(tagKey, TagKey.create(Registries.ENTITY_TYPE, tagKey));
    }

    @Override
    public ResourceLocation getId() {
        return this.groupId;
    }

    @Override
    public boolean matches(Entity entity) {
        return entity.getType().is(this.entityTypeTag);
    }

    @Override
    public ResourceKey<LootTable> getLootTable() {
        return this.lootTable;
    }
}
