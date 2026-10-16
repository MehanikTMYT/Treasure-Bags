package net.silentchaos512.treasurebags;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.silentchaos512.treasurebags.setup.TbItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod(TreasureBags.MOD_ID)
public final class TreasureBags {
    public static final String MOD_ID = "treasurebags";
    public static final String MOD_NAME = "Treasure Bags";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final Random RANDOM = new Random();

    public static TreasureBags INSTANCE;
    private static SideProxy PROXY;

    public TreasureBags(IEventBus modEventBus, ModContainer modContainer) {
        INSTANCE = this;
        PROXY = FMLEnvironment.getDist() == Dist.CLIENT
                ? new SideProxy.Client(modEventBus)
                : new SideProxy.Server(modEventBus);

        modEventBus.addListener(TbItems::onBuildContentsOfCreativeTabs);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.Common.spec);
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "0.0.0";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion()) || !FMLLoader.getCurrent().isProduction();
    }

    public static Identifier getId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Nullable
    public static Identifier getIdWithDefaultNamespace(String name) {
        if (name.contains(":")) {
            return Identifier.tryParse(name);
        }
        return Identifier.tryParse(MOD_ID + ":" + name);
    }

    public static String shortenId(@Nullable Identifier id) {
        if (id == null) {
            return "null";
        }
        if (MOD_ID.equals(id.getNamespace())) {
            return id.getPath();
        }
        return id.toString();
    }
}
