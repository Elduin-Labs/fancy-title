package com.elduin.fancy_title.mixin;

import com.elduin.fancy_title.client.PopIn;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if >=26 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///? } else {
import net.minecraft.client.gui.GuiGraphics;
//? }
import net.minecraft.client.gui.components.LogoRenderer;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Scales the MINECRAFT logo around its middle while it pops in. */
@Mixin(LogoRenderer.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public abstract class LogoRendererMixin {

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
}
