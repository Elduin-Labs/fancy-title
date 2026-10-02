package com.elduin.fancy_title.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.TitleScreen;

public final class FancyTitleClient {

	private FancyTitleClient() {
	}

	public static void register() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (!(screen instanceof TitleScreen)) {
				return;
			}
			Sparkles.clear();
			ScreenEvents.afterTick(screen).register(s -> Sparkles.tick(s.width));
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
