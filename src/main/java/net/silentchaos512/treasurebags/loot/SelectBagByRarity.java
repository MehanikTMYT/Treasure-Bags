package net.silentchaos512.treasurebags.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.item.TreasureBagItem;
import net.silentchaos512.treasurebags.lib.BagType;
import net.silentchaos512.treasurebags.setup.TbLoot;
import net.silentchaos512.treasurebags.setup.TbRegistries;

import java.util.List;

public class SelectBagByRarity extends LootItemConditionalFunction {
    public static final MapCodec<SelectBagByRarity> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(
                            Rarity.CODEC.fieldOf("rarity").forGetter(f -> f.rarity)
                    )
                    .apply(instance, SelectBagByRarity::new)
    );

    private final Rarity rarity;

    public SelectBagByRarity(List<LootItemCondition> conditions, Rarity rarity) {
        super(conditions);
        this.rarity = rarity;
    }

    public static LootItemConditionalFunction.Builder<?> builder(Rarity rarity) {
        return simpleBuilder(conditions -> new SelectBagByRarity(conditions, rarity));
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null) return ItemStack.EMPTY;

        List<BagType> list = TbRegistries.BAG_TYPE.stream()
                .filter(type -> type.canDropFromMob(entity) && type.rarity() == this.rarity)
                .toList();
        if (list.isEmpty()) return ItemStack.EMPTY;

        BagType bagType = list.get(TreasureBags.RANDOM.nextInt(list.size()));
        return TreasureBagItem.setBagProperties(stack, bagType);
    }

    @Override
    public LootItemFunctionType<SelectBagByRarity> getType() {
        return TbLoot.SELECT_BAG_BY_RARITY.get();
    }
}
