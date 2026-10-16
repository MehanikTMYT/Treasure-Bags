package net.silentchaos512.treasurebags.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.lib.TagEntityGroup;
import net.silentchaos512.treasurebags.setup.EntityGroups;

public interface IEntityGroup {
    Identifier getId();

    boolean matches(Entity entity);

    ResourceKey<LootTable> getLootTable();

    Codec<IEntityGroup> CODEC = Codec.STRING
            .comapFlatMap(
                    str -> {
                        var id = TreasureBags.getIdWithDefaultNamespace(str);
                        if (id == null) {
                            return DataResult.error(() -> "Invalid ID for entity group: " + str);
                        }
                        return DataResult.success(EntityGroups.getOrCreate(id, TagEntityGroup::withDefaultName));
                    },
                    entityGroup -> TreasureBags.shortenId(entityGroup.getId())
            );

    StreamCodec<RegistryFriendlyByteBuf, IEntityGroup> STREAM_CODEC = StreamCodec.of(
            (buf, val) -> buf.writeIdentifier(val.getId()),
            buf -> EntityGroups.getOrCreate(buf.readIdentifier(), TagEntityGroup::withDefaultName)
    );
}
