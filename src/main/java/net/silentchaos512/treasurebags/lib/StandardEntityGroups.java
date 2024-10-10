package net.silentchaos512.treasurebags.lib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.treasurebags.TreasureBags;
import net.silentchaos512.treasurebags.api.IEntityGroup;

import java.util.Locale;
import java.util.function.Predicate;

/**
 * A rough group for mobs. Very similar to what Scaling Health uses, but no blights and every type
 * has a loot table.
 */
public enum StandardEntityGroups implements IEntityGroup {
    PLAYER(StandardEntityGroups::isPlayer),
    BOSS(StandardEntityGroups::isBoss),
    HOSTILE(StandardEntityGroups::isHostile),
    PEACEFUL(StandardEntityGroups::isPeacefulMob);

    private final ResourceKey<LootTable> lootTable;
    private final Predicate<Entity> predicate;

    StandardEntityGroups(Predicate<Entity> predicate) {
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, TreasureBags.getId("entity_group/" + this.getName()));
        this.predicate = predicate;
    }

    @Override
    public ResourceLocation getId() {
        return TreasureBags.getId(this.getName());
    }

    @Override
    public boolean matches(Entity entity) {
        return this.predicate.test(entity);
    }

    @Override
    public ResourceKey<LootTable> getLootTable() {
        return lootTable;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    private static boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    private static boolean isBoss(Entity entity) {
        return entity.isAlive() && !entity.canUsePortal(true);
    }

    private static boolean isHostile(Entity entity) {
        return entity instanceof Enemy;
    }

    private static boolean isPeacefulMob(Entity e) {
        return !(isPlayer(e) || isBoss(e) || isHostile(e));
    }
}
