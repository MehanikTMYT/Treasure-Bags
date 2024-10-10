package net.silentchaos512.treasurebags.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.setup.TbItems;
import net.silentchaos512.treasurebags.setup.TbRegistries;

@JeiPlugin
public class TreasureBagsJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = TreasureBags.getId("plugin/main");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        reg.registerSubtypeInterpreter(
                TbItems.TREASURE_BAG.get(),
                new ISubtypeInterpreter<>() {
                    @Override
                    public Object getSubtypeData(ItemStack ingredient, UidContext context) {
                        var bagType = TbRegistries.BAG_TYPE.typeFromBag(ingredient);
                        return bagType != null ? TbRegistries.BAG_TYPE.getKey(bagType).toString() : "null";
                    }

                    @Override
                    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
                        var bagType = TbRegistries.BAG_TYPE.typeFromBag(ingredient);
                        return bagType != null ? TbRegistries.BAG_TYPE.getKey(bagType).toString() : "null";
                    }
                }
        );
    }
}
