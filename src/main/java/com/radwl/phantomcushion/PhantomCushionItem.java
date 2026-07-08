package com.radwl.phantomcushion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.CushionItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PhantomCushionItem extends CushionItem {
	public PhantomCushionItem(Properties properties) {
		super(properties.setId(ModItems.key("phantom_cushion")), DyeColor.WHITE);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getClickedFace() != Direction.UP) {
			return InteractionResult.FAIL;
		}

		Level level = context.getLevel();
		BlockPlaceContext placeContext = new BlockPlaceContext(context);
		BlockPos blockPos = placeContext.getClickedPos();
		Vec3 position = Vec3.atCenterOfWithY(blockPos, context.getClickLocation().y);
		AABB bounds = ModEntityTypes.PHANTOM_CUSHION.getSpawnAABB(position);

		if (!Cushion.wouldSuriveAt(level, bounds)) {
			return InteractionResult.FAIL;
		}

		ItemStack stack = context.getItemInHand();

		if (level instanceof ServerLevel serverLevel) {
			if (!serverLevel.getEntitiesOfClass(Cushion.class, bounds).isEmpty()) {
				return InteractionResult.FAIL;
			}

			PostSpawnProcessor<Cushion> spawnProcessor = EntityType.createDefaultStackConfig(serverLevel, stack, context.getPlayer());
			Cushion cushion = ModEntityTypes.PHANTOM_CUSHION.create(serverLevel, spawnProcessor, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);

			if (cushion == null) {
				return InteractionResult.FAIL;
			}

			cushion.snapTo(position, Direction.fromYRot(placeContext.getRotation()).toYRot(), 0.0F);
			cushion.setColor(DyeColor.WHITE);
			serverLevel.addFreshEntity(cushion);
			level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), SoundEvents.CUSHION_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
			cushion.gameEvent(GameEvent.ENTITY_PLACE);
			stack.consume(1, placeContext.getPlayer());
		}

		return InteractionResult.SUCCESS;
	}
}
