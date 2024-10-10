package net.silentchaos512.treasurebags.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.network.payload.SyncBagTypesPayload;
import net.silentchaos512.treasurebags.setup.TbRegistries;

@EventBusSubscriber(modid = TreasureBags.MOD_ID)
public final class ServerEvents {
    private ServerEvents() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        sendBagTypesToClient(serverPlayer);
        TbRegistries.BAG_TYPE.getErrorMessages(serverPlayer).forEach(player::sendSystemMessage);
    }

    private static void sendBagTypesToClient(ServerPlayer playerMP) {
        var payload = new SyncBagTypesPayload();
        TreasureBags.LOGGER.info("Sending {} bag types to {}", payload.bagTypes().size(), playerMP.getScoreboardName());
        PacketDistributor.sendToPlayer(playerMP, payload);
    }
}
