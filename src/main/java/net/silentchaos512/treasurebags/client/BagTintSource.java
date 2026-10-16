package net.silentchaos512.treasurebags.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.treasurebags.item.TreasureBagItem;

public record BagTintSource(int layer) implements ItemTintSource {
    public static final MapCodec<BagTintSource> CODEC = Codec.intRange(0, 2).fieldOf("layer").xmap(BagTintSource::new, BagTintSource::layer);
    @Override public int calculate(ItemStack stack, ClientLevel level, LivingEntity owner) {
        return 0xFF000000 | TreasureBagItem.getColor(stack, layer);
    }
    @Override public MapCodec<BagTintSource> type() { return CODEC; }
}
