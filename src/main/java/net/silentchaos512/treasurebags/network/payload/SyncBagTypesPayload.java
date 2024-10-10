package net.silentchaos512.treasurebags.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.lib.BagType;
import net.silentchaos512.treasurebags.setup.TbRegistries;

import java.util.HashMap;
import java.util.Map;

public record SyncBagTypesPayload(
        Map<ResourceLocation, BagType> bagTypes
) implements CustomPacketPayload, DataResourcesPayload<BagType> {
    public static final Type<SyncBagTypesPayload> TYPE = new Type<>(TreasureBags.getId("sync_bag_types"));

    public SyncBagTypesPayload() {
        this(TbRegistries.BAG_TYPE.copyOfMap());
    }

    private static final StreamCodec<RegistryFriendlyByteBuf, HashMap<ResourceLocation, BagType>> MAP_STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            ResourceLocation.STREAM_CODEC,
            BagType.STREAM_CODEC
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBagTypesPayload> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> MAP_STREAM_CODEC.encode(buf, new HashMap<>(data.bagTypes)),
            buf -> new SyncBagTypesPayload(MAP_STREAM_CODEC.decode(buf))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public Map<ResourceLocation, BagType> values() {
        return this.bagTypes;
    }
}
