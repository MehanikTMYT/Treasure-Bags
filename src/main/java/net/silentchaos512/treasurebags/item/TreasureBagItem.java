package net.silentchaos512.treasurebags.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.silentchaos512.lib.item.LootContainerItem;
import net.silentchaos512.treasurebags.Config;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.lib.BagType;
import net.silentchaos512.treasurebags.setup.TbDataComponents;
import net.silentchaos512.treasurebags.setup.TbRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TreasureBagItem extends LootContainerItem {
    public TreasureBagItem(Properties properties) {
        super(TreasureBags.getId("default_bag"), true, properties);
    }

    public ItemStack stackOfType(BagType type) {
        return stackOfType(type, 1);
    }

    public ItemStack stackOfType(BagType type, int count) {
        ItemStack result = new ItemStack(this, count);
        return setBagProperties(result, type);
    }

    /**
     * Create a bag of a type that may exist. Useful for recipes/ingredients, since bag types may
     * not exist before they load.
     *
     * @param typeName The type ID
     * @return A stack of one treasure bag
     */
    public ItemStack stackOfType(ResourceLocation typeName) {
        ItemStack result = new ItemStack(this);
        return setBagType(result, typeName);
    }

    @Nullable
    public static BagType getBagType(ItemStack stack) {
        ResourceLocation id = stack.get(TbDataComponents.BAG_TYPE);
        return id != null ? TbRegistries.BAG_TYPE.get(id) : null;
    }

    /**
     * Set the BagType tag, nothing else. This is needed for loading from loot tables, because bag
     * types may not exist yet.
     *
     * @param stack     The bag
     * @param bagTypeId The bag type ID
     * @return The bag (modified original, not a copy)
     */
    public static ItemStack setBagType(ItemStack stack, ResourceLocation bagTypeId) {
        stack.set(TbDataComponents.BAG_TYPE, bagTypeId);
        return stack;
    }

    public static ItemStack setBagProperties(ItemStack stack, BagType type) {
        setBagType(stack, TbRegistries.BAG_TYPE.getKey(type));
        setLootTable(stack, type.lootTable());
        stack.set(DataComponents.RARITY, type.rarity());
        return stack;
    }

    @Nonnull
    @Override
    protected ResourceKey<LootTable> getLootTable(ItemStack stack) {
        BagType type = getBagType(stack);
        if (type == null) {
            return super.getLootTable(stack);
        }
        return type.lootTable();
    }

    @Nonnull
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        BagType type = getBagType(stack);
        if (type != null) {
            return type.customName();
        }
        return super.getName(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(ItemStack stack, int tintIndex) {
        BagType bagType = getBagType(stack);
        if (bagType != null) {
            if (tintIndex == 0) return bagType.bagColor();
            if (tintIndex == 1) return bagType.bagOverlayColor();
            if (tintIndex == 2) return bagType.bagStringColor();
        }
        return 0xFFFFFF;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, TooltipContext tooltipContext, @Nonnull List<Component> tooltip, TooltipFlag flag) {
        var type = getBagType(stack);
        var typeId = type != null ? TbRegistries.BAG_TYPE.getKey(type) : null;
        var typeName = typeId != null ? typeId.toString() : "null";

        appendInfoPair(tooltip, flag, true, "type", typeName);
        if (type != null) {
            appendInfoPair(tooltip, flag, true, "group", type.group());
            appendInfoPair(tooltip, flag, true, "lootTable", type.lootTable().location().toString());
        } else {
            tooltip.add(Component.translatable(this.getDescriptionId() + ".invalidType")
                    .withStyle(ChatFormatting.RED));
        }
        appendInfoPair(tooltip, flag, false, "packName", typeId != null ? typeId.getNamespace() : "null");
    }

    private void appendInfoPair(List<Component> tooltip, TooltipFlag flag, boolean advancedOnly, String key, String value) {
        if (flag.isAdvanced() || !advancedOnly) {
            var valueText = Component.literal(value).withStyle(ChatFormatting.WHITE);
            tooltip.add(Component.translatable(this.getDescriptionId() + "." + key, valueText).withStyle(ChatFormatting.AQUA));
        }
    }

    public List<ItemStack> getSubItems() {
        NonNullList<ItemStack> items = NonNullList.create();

        // Add for each type (sorted by ID)
        List<BagType> list = new ArrayList<>(TbRegistries.BAG_TYPE.stream().toList());
        list.sort(Comparator.comparing(bagType -> TbRegistries.BAG_TYPE.getKey(bagType).toString()));
        for (BagType type : list) {
            if (type.visible()) {
                items.add(stackOfType(type));
            }
        }

        return items;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);
        if (!(playerIn instanceof ServerPlayer)) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
        }
        ServerPlayer playerMP = (ServerPlayer) playerIn;

        // Generate items from loot table, give to player.
        boolean openWholeStack = playerMP.isCrouching();
        Collection<ItemStack> lootDrops = getDropsFromStack(heldItem, playerMP, openWholeStack);
        if (lootDrops.isEmpty()) {
            TreasureBags.LOGGER.warn("No drops from bag, is the loot table valid? {}, table={}", heldItem, getLootTable(heldItem));
            return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
        }
        lootDrops.forEach(stack -> {
            giveOrDropItem(playerIn, stack.copy());
            listItemReceivedInChat(playerMP, stack);
        });

        // Play item pickup sound...
        playerMP.level().playSound(null, playerMP.getX(), playerMP.getY(), playerMP.getZ(),
                SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F,
                ((playerMP.getRandom().nextFloat() - playerMP.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        heldItem.shrink(openWholeStack ? heldItem.getCount() : 1);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
    }

    private void giveOrDropItem(Player playerIn, ItemStack copy) {
        if (Config.Common.alwaysSpawnItems.get() || !playerIn.getInventory().add(copy)) {
            ItemEntity entityItem = new ItemEntity(playerIn.level(), playerIn.getX(), playerIn.getY(0.5), playerIn.getZ(), copy);
            entityItem.setNoPickUpDelay();
            entityItem.setThrower(playerIn);
            playerIn.level().addFreshEntity(entityItem);
        }
    }

    private Collection<ItemStack> getDropsFromStack(ItemStack stack, ServerPlayer player, boolean wholeStack) {
        Collection<ItemStack> list = new ArrayList<>();
        int openCount = wholeStack ? stack.getCount() : 1;
        for (int i = 0; i < openCount; ++i) {
            this.getLootDrops(stack, player).forEach(s -> mergeItem(list, s));
        }
        return list;
    }

    private static void mergeItem(Collection<ItemStack> list, ItemStack stack) {
        for (ItemStack itemStack : list) {
            if (ItemStack.isSameItemSameComponents(itemStack, stack)) {
                int space = itemStack.getMaxStackSize() - itemStack.getCount();
                int toMerge = Math.min(space, stack.getCount());
                itemStack.grow(toMerge);
                stack.shrink(toMerge);

                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        if (!stack.isEmpty()) {
            list.add(stack);
        }
    }

    private static void listItemReceivedInChat(ServerPlayer playerMP, ItemStack stack) {
        Component itemReceivedText = Component.translatable(
                "item.silentlib.lootContainer.itemReceived",
                stack.getCount(),
                stack.getHoverName());
        playerMP.sendSystemMessage(itemReceivedText);
    }
}
