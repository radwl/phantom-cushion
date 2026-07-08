package com.radwl.phantomcushion;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

public class PhantomCushionEntity extends Cushion {
	public PhantomCushionEntity(EntityType<Cushion> entityType, Level level) {
		super(entityType, level);
		setInvisible(false);
	}

	@Override
	public void dropItem(ServerLevel level, Entity breaker) {
		playSound(SoundEvents.CUSHION_BREAK, 1.0F, 1.0F);
		if (!level.getGameRules().get(GameRules.ENTITY_DROPS)) {
			return;
		}
		if (breaker instanceof Player player && player.hasInfiniteMaterials()) {
			return;
		}
		spawnAtLocation(level, ModItems.PHANTOM_CUSHION);
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(ModItems.PHANTOM_CUSHION);
	}
}
