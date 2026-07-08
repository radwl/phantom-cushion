package com.radwl.phantomcushion;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModItems {
	public static final Item PHANTOM_CUSHION = register("phantom_cushion", new PhantomCushionItem(new Item.Properties().stacksTo(16)));

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS)
				.register(output -> output.insertAfter(Items.CUSHION.black(), PHANTOM_CUSHION));
	}

	private static Item register(String path, Item item) {
		return Registry.register(BuiltInRegistries.ITEM, key(path), item);
	}

	static ResourceKey<Item> key(String path) {
		return ResourceKey.create(Registries.ITEM, PhantomCushion.id(path));
	}
}
