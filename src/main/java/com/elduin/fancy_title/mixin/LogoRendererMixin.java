package com.elduin.fancy_title.mixin;

import com.elduin.fancy_title.client.PopIn;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if >=26 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///? } else {
import net.minecraft.client.gui.GuiGraphics;
//? }
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Scales the MINECRAFT logo around its middle while it pops in, and draws it without its E, so it
 * says MINCRAFT. The logo picture is 256 wide (in logo units); the E sits between {@link #E_START}
 * and {@link #E_END}. The two halves are drawn next to each other and nudged right so the shorter
 * word stays in the middle. The "Minceraft" easter-egg logo is left alone.
 */
@Mixin(LogoRenderer.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public abstract class LogoRendererMixin {

	/** Where the E starts and ends in the logo picture (the dark gaps on either side of it). */
	private static final int E_START = 89;
	private static final int E_END = 118;
	private static final int SHIFT = (E_END - E_START) / 2;

	// 26 renamed GuiGraphics to GuiGraphicsExtractor and renderLogo to extractRenderState.
	//? if >=26 {
	/*@Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V", at = @At("HEAD"))
	private void fancy_title$popStart(GuiGraphicsExtractor graphics, int screenWidth, float alpha, int top, CallbackInfo ci) {
		Matrix3x2fStack pose = graphics.pose();
	*///? } else {
	@Inject(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At("HEAD"))
	private void fancy_title$popStart(GuiGraphics graphics, int screenWidth, float alpha, int top, CallbackInfo ci) {
		Matrix3x2fStack pose = graphics.pose();
	//? }
		float scale = PopIn.scale();
		float centreX = screenWidth / 2.0f;
		float centreY = top + LogoRenderer.LOGO_HEIGHT / 2.0f;
		pose.pushMatrix();
		pose.translate(centreX, centreY);
		pose.scale(scale, scale);
		pose.translate(-centreX, -centreY);
	}

	//? if >=26 {
	/*@Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V", at = @At("RETURN"))
	private void fancy_title$popEnd(GuiGraphicsExtractor graphics, int screenWidth, float alpha, int top, CallbackInfo ci) {
	*///? } else {
	@Inject(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At("RETURN"))
	private void fancy_title$popEnd(GuiGraphics graphics, int screenWidth, float alpha, int top, CallbackInfo ci) {
	//? }
		graphics.pose().popMatrix();
	}

	//? if >=26 {
	/*@Redirect(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V"))
	private void fancy_title$noE(GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier texture, int x, int y,
	*///? } else {
	@Redirect(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V"))
	private void fancy_title$noE(GuiGraphics graphics, RenderPipeline pipeline, Identifier texture, int x, int y,
	//? }
			float u, float v, int width, int height, int textureWidth, int textureHeight, int colour) {
		if (!texture.equals(LogoRenderer.MINECRAFT_LOGO)) {
			graphics.blit(pipeline, texture, x, y, u, v, width, height, textureWidth, textureHeight, colour);
			return;
		}
		// MIN...
		graphics.blit(pipeline, texture, x + SHIFT, y, u, v, E_START, height, textureWidth, textureHeight, colour);
		// ...CRAFT, pulled left to close the gap where the E was.
		graphics.blit(pipeline, texture, x + SHIFT + E_START, y, u + E_END, v, width - E_END, height,
				textureWidth, textureHeight, colour);
	}
}
