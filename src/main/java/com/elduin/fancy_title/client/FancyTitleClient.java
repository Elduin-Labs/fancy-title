package com.elduin.fancy_title.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

public final class FancyTitleClient {

	/** The title screen we last set up, so resizing the window doesn't pop the logo again. */
	private static Screen lastTitle;

	private FancyTitleClient() {
	}

	public static void register() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof TitleScreen)) {
				return;
			}
			if (screen != lastTitle) {
				lastTitle = screen;
				PopIn.start();
			}
			Sparkles.clear();
			ScreenEvents.afterTick(screen).register(s -> {
				// Sparkles start once the logo has finished popping in.
				if (PopIn.done()) {
					Sparkles.tick(s.width);
				}
			});
			// Fabric renamed "render" to "extract" in 26, along with Minecraft's GUI drawing.
			//? if >=26 {
			/*ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, partialTick) ->
					Sparkles.draw(graphics, partialTick));
			*///? } else {
			ScreenEvents.afterRender(screen).register((s, graphics, mouseX, mouseY, partialTick) ->
					Sparkles.draw(graphics, partialTick));
			//? }
		});
	}
}
