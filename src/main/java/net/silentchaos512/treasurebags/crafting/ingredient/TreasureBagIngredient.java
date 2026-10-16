package net.silentchaos512.treasurebags.crafting.ingredient;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
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
                    Identifier.CODEC.fieldOf("bag_type").forGetter(ingredient -> ingredient.typeName)
            ).apply(instance, TreasureBagIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TreasureBagIngredient> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ingredient -> ingredient.typeName,
            TreasureBagIngredient::new
    );

    // Store bag type ID because the bag types likely don't exist yet
    private final Identifier typeName;

    private TreasureBagIngredient(Identifier typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        var stackBagId = stack.get(TbDataComponents.BAG_TYPE);
        return this.typeName.equals(stackBagId);
    }

    @Override
    public Stream<net.minecraft.core.Holder<net.minecraft.world.item.Item>> items() {
        return Stream.of(TbItems.TREASURE_BAG);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return net.silentchaos512.treasurebags.setup.TbIngredientTypes.TREASURE_BAG.get();
    }
    @Override
    public net.minecraft.world.item.crafting.display.SlotDisplay display() {
        return new net.minecraft.world.item.crafting.display.SlotDisplay.ItemStackSlotDisplay(net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(TbItems.TREASURE_BAG.get().stackOfType(typeName)));
    }

    @Override public boolean equals(Object other) {
        return other instanceof TreasureBagIngredient ingredient && typeName.equals(ingredient.typeName);
    }
    @Override public int hashCode() { return typeName.hashCode(); }
}
