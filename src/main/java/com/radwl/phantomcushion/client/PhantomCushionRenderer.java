package com.radwl.phantomcushion.client;

import java.util.Map;
import java.util.WeakHashMap;

import com.radwl.phantomcushion.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.cushion.CushionModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CushionRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.phys.EntityHitResult;

public class PhantomCushionRenderer extends EntityRenderer<Cushion, PhantomCushionRenderer.PhantomCushionRenderState> {
	private static final float MAX_GHOST_ALPHA = 0.69F;
	private static final int REVEAL_DELAY_TICKS = 2;
	private static final int FADE_TICKS = 4;
	private static final int GHOST_RGB = 0x00FFFFFF;
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("phantomcushion", "textures/entity/phantom_cushion.png");

	private final CushionModel model;
	private final Map<Cushion, RevealState> revealStates = new WeakHashMap<>();

	public PhantomCushionRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CushionModel(context.bakeLayer(ModelLayers.CUSHION));
	}

	@Override
	public PhantomCushionRenderState createRenderState() {
		return new PhantomCushionRenderState();
	}

	@Override
	public void extractRenderState(Cushion cushion, PhantomCushionRenderState renderState, float tickDelta) {
		super.extractRenderState(cushion, renderState, tickDelta);
		float revealAlpha = updateRevealAlpha(cushion, tickDelta);
		renderState.direction = Direction.fromYRot(cushion.getYRot());
		renderState.texture = TEXTURE;
		renderState.visible = revealAlpha > 0.0F;
		renderState.color = ghostColor(revealAlpha);
	}

	@Override
	public void submit(PhantomCushionRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (renderState.visible) {
			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotationDegrees(renderState.direction.toYRot()));
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
			poseStack.translate(0.0, -0.25, 0.0);
			submitNodeCollector.submitModel(this.model, renderState, poseStack, RenderTypes.entityTranslucentCull(renderState.texture), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.color, null, renderState.outlineColor);
			poseStack.popPose();
		}

		super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
	}

	@Override
	public boolean shouldRender(Cushion cushion, Frustum frustum, double x, double y, double z) {
		if (isHoldingPhantomCushion()) {
			return frustum.isVisible(cushion.getBoundingBox());
		}
		return super.shouldRender(cushion, frustum, x, y, z);
	}

	private float updateRevealAlpha(Cushion cushion, float tickDelta) {
		RevealState state = this.revealStates.computeIfAbsent(cushion, unused -> new RevealState());
		boolean forcedVisible = isHoldingPhantomCushion();
		if (cushion.isVehicle() && !forcedVisible) {
			state.reset();
			return 0.0F;
		}

		boolean targetVisible = forcedVisible || isSelected(cushion);
		float elapsedTicks = state.tickElapsed(cushion.level().getGameTime() + tickDelta);

		if (forcedVisible) {
			state.visible = true;
			state.pendingTicks = 0;
			state.pendingVisible = true;
			state.alpha = 1.0F;
		} else if (targetVisible == state.visible) {
			state.pendingTicks = 0;
			state.pendingVisible = targetVisible;
		} else {
			if (state.pendingVisible != targetVisible) {
				state.pendingVisible = targetVisible;
				state.pendingTicks = 0;
			}
			state.pendingTicks += elapsedTicks;
			if (state.pendingTicks >= REVEAL_DELAY_TICKS) {
				state.visible = targetVisible;
				state.pendingTicks = 0;
			}
		}

		if (elapsedTicks > 0) {
			float fadeStep = (float) elapsedTicks / FADE_TICKS;
			state.alpha = approach(state.alpha, state.visible ? 1.0F : 0.0F, fadeStep);
		}

		return state.alpha;
	}

	private static boolean isSelected(Cushion cushion) {
		if (cushion.isVehicle()) {
			return false;
		}

		return Minecraft.getInstance().hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() == cushion;
	}

	private static float approach(float value, float target, float amount) {
		if (value < target) {
			return Math.min(target, value + amount);
		}
		if (value > target) {
			return Math.max(target, value - amount);
		}
		return value;
	}

	private static int ghostColor(float alpha) {
		int alphaValue = Math.max(0, Math.min(255, Math.round(255.0F * MAX_GHOST_ALPHA * alpha)));
		return alphaValue << 24 | GHOST_RGB;
	}

	private static boolean isHoldingPhantomCushion() {
		return Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(ModItems.PHANTOM_CUSHION);
	}

	public static class PhantomCushionRenderState extends CushionRenderState {
		public boolean visible;
		public int color;
	}

	private static class RevealState {
		private boolean visible;
		private boolean pendingVisible;
		private float pendingTicks;
		private float alpha;
		private double lastTime = Double.NaN;

		private float tickElapsed(double time) {
			if (Double.isNaN(this.lastTime)) {
				this.lastTime = time;
				return 0.0F;
			}

			double elapsed = time - this.lastTime;
			this.lastTime = time;
			if (elapsed <= 0.0) {
				return 0.0F;
			}
			return (float) Math.min(elapsed, REVEAL_DELAY_TICKS + FADE_TICKS);
		}

		private void reset() {
			this.visible = false;
			this.pendingVisible = false;
			this.pendingTicks = 0;
			this.alpha = 0.0F;
			this.lastTime = Double.NaN;
		}
	}
}
