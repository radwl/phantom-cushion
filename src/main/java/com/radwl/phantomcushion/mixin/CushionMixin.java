package com.radwl.phantomcushion.mixin;

import com.radwl.phantomcushion.entity.PhantomCushionEntity;
import com.radwl.phantomcushion.item.ModItems;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Cushion.class)
public class CushionMixin {
	@Redirect(method = "getCushionItemStackWithData", at = @At(value = "NEW", target = "Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack phantomCushion$replaceItemStack(ItemLike item) {
		if ((Object) this instanceof PhantomCushionEntity) {
			return new ItemStack(ModItems.PHANTOM_CUSHION);
		}
		return new ItemStack(item);
	}
}
