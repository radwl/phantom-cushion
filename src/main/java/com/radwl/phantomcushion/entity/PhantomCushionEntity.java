package com.radwl.phantomcushion.entity;

import com.radwl.phantomcushion.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PhantomCushionEntity extends Cushion {
	public PhantomCushionEntity(EntityType<Cushion> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public DyeColor getColor() {
		return DyeColor.WHITE;
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(ModItems.PHANTOM_CUSHION);
	}
}
