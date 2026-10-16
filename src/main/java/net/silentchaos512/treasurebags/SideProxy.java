package net.silentchaos512.treasurebags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.silentchaos512.lib.event.InitialSpawnItems;
import net.silentchaos512.lib.util.LootUtils;
import net.silentchaos512.treasurebags.command.TreasureBagsCommand;
import net.silentchaos512.treasurebags.data.DataGenerators;
import net.silentchaos512.treasurebags.item.TreasureBagItem;
import net.silentchaos512.treasurebags.setup.*;

import java.util.Collections;

class SideProxy {
    private static final ResourceKey<LootTable> STARTING_INVENTORY = ResourceKey.create(Registries.LOOT_TABLE, TreasureBags.getId("starting_inventory"));

    SideProxy(IEventBus modEventBus) {
        TbDataComponents.REGISTRAR.register(modEventBus);
        TbIngredientTypes.REGISTRAR.register(modEventBus);
        TbItems.ITEMS.register(modEventBus);
        TbLoot.LOOT_FUNCTIONS.register(modEventBus);
        TbLoot.LOOT_MODIFIERS.register(modEventBus);

        EntityGroups.init();

        modEventBus.addListener(DataGenerators::gatherData);
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListener);
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        InitialSpawnItems.add(STARTING_INVENTORY.identifier(), p -> {
            if (p instanceof ServerPlayer) {
                return LootUtils.gift(STARTING_INVENTORY, (ServerPlayer) p);
            }
            return Collections.emptyList();
        });
    }

    private void onAddReloadListener(AddServerReloadListenersEvent event) {
        event.addListener(TreasureBags.getId("bag_types"), TbRegistries.BAG_TYPE);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        TreasureBagsCommand.register(event.getDispatcher());
    }

    static class Client extends SideProxy {
        Client(IEventBus modEventBus) {
            super(modEventBus);
            modEventBus.addListener(this::clientSetup);
            modEventBus.addListener(this::onItemColors);
        }

        private void clientSetup(FMLClientSetupEvent event) {
        }

        private void onItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
            event.register(TreasureBags.getId("bag"), net.silentchaos512.treasurebags.client.BagTintSource.CODEC);
        }
    }

    static class Server extends SideProxy {
        Server(IEventBus modEventBus) {
            super(modEventBus);
            modEventBus.addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }
}
