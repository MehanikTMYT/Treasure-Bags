package net.silentchaos512.treasurebags.lib;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.item.TreasureBagItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class BagTypeManager extends DataResourceManager<BagType> {
    public BagTypeManager() {
        super(
                BagType.CODEC,
                BagTypeJsonException::new,
                "bag type",
                "treasurebags_types",
                "BagTypeManager",
                TreasureBags.LOGGER
        );
    }

    @Nullable
    public BagType typeFromBag(ItemStack stack) {
        if (!(stack.getItem() instanceof TreasureBagItem)) return null;
        return TreasureBagItem.getBagType(stack);
    }

    @Override
    public Collection<Component> getErrorMessages(ServerPlayer player) {
        List<Component> messages = new ArrayList<>(super.getErrorMessages(player));

        // Loot tables that failed to load or are missing
        int missingLootTables = countMissingLootTables(player);
        if (missingLootTables > 0) {
            String counter = missingLootTables == 1
                    ? "1 bag type has a missing or invalid loot table"
                    : missingLootTables + " bag types have missing or invalid loot tables";
            messages.add(errorMessage(counter));
        }

        return messages;
    }

    private int countMissingLootTables(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        if (server == null) return 0;

        var lootTables = server.reloadableRegistries().lookup().lookup(Registries.LOOT_TABLE).orElseThrow();
        return (int) this.stream()
                .filter(bagType -> lootTables.get(bagType.lootTable()).isEmpty())
                .count();
    }

    private static Component errorMessage(String str) {
        return Component.literal("[Treasure Bags] ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(str).withStyle(ChatFormatting.WHITE));
    }
}
