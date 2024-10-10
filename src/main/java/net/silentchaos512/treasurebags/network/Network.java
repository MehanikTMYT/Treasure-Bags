package net.silentchaos512.treasurebags.network;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.network.payload.SyncBagTypesPayload;
import net.silentchaos512.treasurebags.setup.TbRegistries;

@EventBusSubscriber(modid = TreasureBags.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class Network {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1.10");

        registrar.playToClient(
                SyncBagTypesPayload.TYPE,
                SyncBagTypesPayload.STREAM_CODEC,
                (data, ctx) -> ctx.enqueueWork(() -> TbRegistries.BAG_TYPE.handleSyncPacket(data, ctx))
                        .exceptionally(e -> {
                            ctx.disconnect(Component.literal("Treasure Bags network failure: " + e.getMessage()));
                            return null;
                        })
        );
    }
}
