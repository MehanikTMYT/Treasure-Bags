package net.silentchaos512.treasurebags.setup;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.item.TreasureBagItem;

import java.util.function.Supplier;

public final class TbItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(TreasureBags.MOD_ID);

    public static final DeferredItem<TreasureBagItem> TREASURE_BAG = ITEMS.registerItem(
            "treasure_bag",
            TreasureBagItem::new
    );

    private TbItems() {}

    public static void onBuildContentsOfCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.acceptAll(TREASURE_BAG.get().getSubItems());
        }
    }

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
