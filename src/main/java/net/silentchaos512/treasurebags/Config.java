package net.silentchaos512.treasurebags;

import com.google.common.collect.ImmutableList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

@EventBusSubscriber(modid = TreasureBags.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class Config {
    public static final class Common {
        static final ModConfigSpec spec;

        public static final ModConfigSpec.BooleanValue alwaysSpawnItems;
        public static final ModConfigSpec.ConfigValue<List<? extends String>> disabledBagGroups;

        static {
            ModConfigSpec.Builder builder =new ModConfigSpec.Builder();

            {
                builder.push("item");

                {
                    builder.push("treasurebag");

                    alwaysSpawnItems = builder
                            .comment("If true, treasure bags will always spawn items on top of the player.",
                                    "Otherwise this only happens if the player's inventory is full",
                                    "Useful for bag-type items; Dank Storage, SGems Gem Bag, backpacks")
                            .define("alwaysSpawnItems", false);

                    disabledBagGroups = builder
                            .comment("Disables all treasure bags in these groups. The built-in bags are in the \"default\" and \"example\" groups")
                            .define("disabledGroups", ImmutableList.of("example", "another_example"), Config::isStringList);

                    builder.pop();
                }

                builder.pop();
            }

            spec = builder.build();
        }

        private Common() {}
    }

    private static boolean isStringList(Object obj) {
        if (obj instanceof List) {
            for (Object entry : (List) obj) {
                if (!(entry instanceof String)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Config() {}

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
    }
}
