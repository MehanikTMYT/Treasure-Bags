package net.silentchaos512.treasurebags.crafting.ingredient;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.silentchaos512.treasurebags.setup.TbDataComponents;
import net.silentchaos512.treasurebags.setup.TbItems;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public final class TreasureBagIngredient implements ICustomIngredient {
    public static final MapCodec<TreasureBagIngredient> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("bag_type").forGetter(ingredient -> ingredient.typeName)
            ).apply(instance, TreasureBagIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TreasureBagIngredient> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ingredient -> ingredient.typeName,
            TreasureBagIngredient::new
    );

    // Store bag type ID because the bag types likely don't exist yet
    private final ResourceLocation typeName;

    private TreasureBagIngredient(ResourceLocation typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        var stackBagId = stack.get(TbDataComponents.BAG_TYPE);
        return this.typeName.equals(stackBagId);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Stream.of(TbItems.TREASURE_BAG.get().stackOfType(this.typeName));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return null;
    }
}
