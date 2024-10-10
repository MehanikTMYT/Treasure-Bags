package net.silentchaos512.treasurebags.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.item.TreasureBagItem;
import net.silentchaos512.treasurebags.setup.TbLoot;
import net.silentchaos512.treasurebags.setup.TbRegistries;

import java.util.List;

public final class SetBagTypeFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetBagTypeFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(
                            ResourceLocation.CODEC.fieldOf("bag_type").forGetter(f -> f.typeId)
                    )
                    .apply(instance, SetBagTypeFunction::new)
    );

    private final ResourceLocation typeId;

    private SetBagTypeFunction(List<LootItemCondition> conditions, ResourceLocation typeId) {
        super(conditions);
        this.typeId = typeId;
    }

    public static LootItemConditionalFunction.Builder<?> builder(ResourceLocation bagTypeId) {
        return simpleBuilder(conditions -> new SetBagTypeFunction(conditions, bagTypeId));
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        var bagType = TbRegistries.BAG_TYPE.get(this.typeId);
        if (bagType != null) {
            return TreasureBagItem.setBagProperties(stack, bagType);
        }
        TreasureBags.LOGGER.error("Unknown bag type: {}", this.typeId);
        return stack;
    }

    @Override
    public LootItemFunctionType<SetBagTypeFunction> getType() {
        return TbLoot.SET_BAG_TYPE.get();
    }
}
