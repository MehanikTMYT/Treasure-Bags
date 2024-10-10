package net.silentchaos512.treasurebags.lib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.treasurebags.api.IEntityGroup;

import java.util.List;

public record BagType (
        String group,
        Rarity rarity,
        List<IEntityGroup> dropsFromGroups,
        int bagColor,
        int bagOverlayColor,
        int bagStringColor,
        Component customName,
        ResourceKey<LootTable> lootTable,
        boolean visible
){
    public static final Codec<BagType> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("group").forGetter(bt -> bt.group),
                    Rarity.CODEC.fieldOf("rarity").forGetter(bt -> bt.rarity),
                    Codec.list(IEntityGroup.CODEC).fieldOf("drops_from_groups").forGetter(bt -> bt.dropsFromGroups),
                    Color.CODEC.fieldOf("bag_color").forGetter(bt -> new Color(bt.bagColor)),
                    Color.CODEC.fieldOf("bag_overlay_color").forGetter(bt -> new Color(bt.bagOverlayColor)),
                    Color.CODEC.fieldOf("bag_string_color").forGetter(bt -> new Color(bt.bagStringColor)),
                    ComponentSerialization.CODEC.fieldOf("display_name").forGetter(bt -> bt.customName),
                    ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(bt -> bt.lootTable),
                    Codec.BOOL.fieldOf("visible").forGetter(bt -> bt.visible)
            ).apply(instance, BagType::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BagType> STREAM_CODEC = StreamCodec.of(
            BagType::encode,
            BagType::decode
    );

    private static void encode(RegistryFriendlyByteBuf buf, BagType bagType) {
        buf.writeUtf(bagType.group);
        Rarity.STREAM_CODEC.encode(buf, bagType.rarity);
        IEntityGroup.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, bagType.dropsFromGroups);
        buf.writeInt(bagType.bagColor);
        buf.writeInt(bagType.bagOverlayColor);
        buf.writeInt(bagType.bagStringColor);
        ComponentSerialization.STREAM_CODEC.encode(buf, bagType.customName);
        ResourceKey.streamCodec(Registries.LOOT_TABLE).encode(buf, bagType.lootTable);
        buf.writeBoolean(bagType.visible);
    }

    private static BagType decode(RegistryFriendlyByteBuf buf) {
        return new BagType(
                buf.readUtf(),
                Rarity.STREAM_CODEC.decode(buf),
                IEntityGroup.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                buf.readInt() | 0xFF000000,
                buf.readInt() | 0xFF000000,
                buf.readInt() | 0xFF000000,
                ComponentSerialization.STREAM_CODEC.decode(buf),
                ResourceKey.streamCodec(Registries.LOOT_TABLE).decode(buf),
                buf.readBoolean()
        );
    }

    public BagType(
            String group,
            Rarity rarity,
            List<IEntityGroup> dropsFromGroups,
            Color bagColor,
            Color bagOverlayColor,
            Color bagStringColor,
            Component customName,
            ResourceKey<LootTable> lootTable,
            boolean visible
    ) {
        this(
                group,
                rarity,
                dropsFromGroups,
                bagColor.getColor() | 0xFF000000,
                bagOverlayColor.getColor() | 0xFF000000,
                bagStringColor.getColor() | 0xFF000000,
                customName,
                lootTable,
                visible
        );
    }

    public boolean canDropFromMob(Entity entity) {
        for (IEntityGroup group : this.dropsFromGroups) {
            if (group.matches(entity)) {
                return true;
            }
        }
        return false;
    }
}
