package com.radwl.phantomcushion.client;

import com.radwl.phantomcushion.entity.ModEntityTypes;
import com.radwl.phantomcushion.mixin.client.EntityRenderersInvoker;

import net.fabricmc.api.ClientModInitializer;

public class PhantomCushionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRenderersInvoker.phantomcushion$register(ModEntityTypes.PHANTOM_CUSHION, PhantomCushionRenderer::new);
	}
}
