package com.elduin.fancy_title.platform.fabric;

//? fabric {

import com.elduin.fancy_title.FancyTitle;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FancyTitle.onInitializeClient();
	}

}
//?}
