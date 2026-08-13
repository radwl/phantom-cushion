package com.radwl.phantomcushion.item;

import java.util.function.Function;

import com.radwl.phantomcushion.PhantomCushion;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModItems {
	public static final Item PHANTOM_CUSHION = register(
			"phantom_cushion",
			PhantomCushionItem::new,
			new Item.Properties().stacksTo(16)
	);

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS)
				.register(output -> output.insertAfter(Items.CUSHION.pink(), PHANTOM_CUSHION));
	}

	private static <T extends Item> T register(String id, Function<Item.Properties, T> factory, Item.Properties properties) {
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, PhantomCushion.createId(id));
		T item = factory.apply(properties.setId(itemKey));
		return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
	}
}
