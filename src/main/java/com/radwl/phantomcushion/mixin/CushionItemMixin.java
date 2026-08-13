package com.radwl.phantomcushion.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.radwl.phantomcushion.entity.ModEntityTypes;
import com.radwl.phantomcushion.item.PhantomCushionItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CushionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CushionItem.class)
public abstract class CushionItemMixin {

    @ModifyExpressionValue(method = "useOn", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntityTypes;CUSHION:Lnet/minecraft/world/entity/EntityType;"))
    private EntityType<?> phantomCushion$replaceEntityType(EntityType<?> original) {
        if ((Object) this instanceof PhantomCushionItem)
            return ModEntityTypes.PHANTOM_CUSHION;
        return original;
    }

}
