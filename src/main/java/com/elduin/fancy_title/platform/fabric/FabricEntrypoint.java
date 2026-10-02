package com.elduin.fancy_title.platform.fabric;

//? fabric {

import com.elduin.fancy_title.FancyTitle;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		FancyTitle.onInitialize();
		FabricEventSubscriber.registerEvents();
	}
}
//?}
