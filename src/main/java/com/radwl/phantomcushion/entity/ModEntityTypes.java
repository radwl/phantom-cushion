package com.radwl.phantomcushion.entity;

import com.radwl.phantomcushion.PhantomCushion;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Cushion;

public class ModEntityTypes {
	public static final ResourceKey<EntityType<?>> PHANTOM_CUSHION_KEY = ResourceKey.create(Registries.ENTITY_TYPE, PhantomCushion.createId("phantom_cushion"));

	public static final EntityType<Cushion> PHANTOM_CUSHION = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			PHANTOM_CUSHION_KEY,
			EntityType.Builder.of(PhantomCushionEntity::new, MobCategory.MISC)
					.noLootTable()
					.sized(1.0F, 0.25F)
					.clientTrackingRange(10)
					.updateInterval(Integer.MAX_VALUE)
					.dontTrackDeltas()
					.build(PHANTOM_CUSHION_KEY)
	);

	public static void initialize() {
	}
}
