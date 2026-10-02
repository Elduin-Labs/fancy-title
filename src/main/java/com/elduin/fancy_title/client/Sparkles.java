package com.elduin.fancy_title.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//? if >=26 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///? } else {
import net.minecraft.client.gui.GuiGraphics;
//? }
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * Little four-pointed stars that twinkle in and out all over the MINECRAFT logo on the title
 * screen. Each one grows, shines, and fades over about a second while drifting upward.
 */
public final class Sparkles {

	/** How many new sparkles appear each tick (20 ticks a second). */
	private static final int PER_TICK = 2;

	/** The logo's top edge on the title screen. */
	private static final int LOGO_TOP = LogoRenderer.DEFAULT_HEIGHT_OFFSET;

	private static final int[] COLOURS = {0xFFFFFF, 0xFFF4A0, 0xA8E8FF, 0xFFC8F0};

	private static final RandomSource RANDOM = RandomSource.create();
	private static final List<Sparkle> SPARKLES = new ArrayList<>();

	private static final class Sparkle {
		float x;
		float y;
		int age;
		final int life;
		final int colour;

		Sparkle(float x, float y, int life, int colour) {
			this.x = x;
			this.y = y;
			this.life = life;
			this.colour = colour;
		}
	}

	private Sparkles() {
	}

	/** Called every tick while the title screen is open. */
	public static void tick(int screenWidth) {
		if (PopIn.justPopped()) {
			burst(screenWidth);
		}
		Iterator<Sparkle> it = SPARKLES.iterator();
		while (it.hasNext()) {
			Sparkle s = it.next();
			s.age++;
			s.y -= 0.15f;
			if (s.age >= s.life) {
				it.remove();
			}
		}
		if (PopIn.scale() < 0.5f) {
			return;  // nothing to twinkle on while the logo is hidden
		}
		int left = screenWidth / 2 - LogoRenderer.LOGO_WIDTH / 2;
		for (int i = 0; i < PER_TICK; i++) {
			float x = left - 6 + RANDOM.nextFloat() * (LogoRenderer.LOGO_WIDTH + 12);
			float y = LOGO_TOP - 6 + RANDOM.nextFloat() * (LogoRenderer.LOGO_HEIGHT + 12);
			SPARKLES.add(new Sparkle(x, y, 14 + RANDOM.nextInt(14), COLOURS[RANDOM.nextInt(COLOURS.length)]));
		}
	}

	//? if >=26 {
	/*public static void draw(GuiGraphicsExtractor graphics, float partialTick) {
	*///? } else {
	public static void draw(GuiGraphics graphics, float partialTick) {
	//? }
		for (Sparkle s : SPARKLES) {
			// 0 -> 1 -> 0 over its life: grow, shine, fade.
			float t = (s.age + partialTick) / s.life;
			float shine = Mth.sin(Mth.clamp(t, 0.0f, 1.0f) * Mth.PI);
			int alpha = (int) (255 * shine);
			if (alpha < 8) {
				continue;
			}
			int x = Math.round(s.x);
			int y = Math.round(s.y);
			int core = (alpha << 24) | s.colour;
			int glow = ((alpha / 2) << 24) | s.colour;
			int arm = 1 + Math.round(3 * shine);

			graphics.fill(x, y, x + 1, y + 1, core);
			graphics.fill(x - arm, y, x, y + 1, glow);
			graphics.fill(x + 1, y, x + 1 + arm, y + 1, glow);
			graphics.fill(x, y - arm, x + 1, y, glow);
			graphics.fill(x, y + 1, x + 1, y + 1 + arm, glow);
		}
	}

	/** A shower of sparkles flying out of the middle of the logo as it pops out. */
	private static void burst(int screenWidth) {
		float cx = screenWidth / 2.0f;
		float cy = LOGO_TOP + LogoRenderer.LOGO_HEIGHT / 2.0f;
		for (int i = 0; i < 40; i++) {
			double angle = RANDOM.nextDouble() * Math.PI * 2.0;
			double reach = 20 + RANDOM.nextDouble() * 130;
			float x = (float) (cx + Math.cos(angle) * reach);
			float y = (float) (cy + Math.sin(angle) * reach * 0.35);
			SPARKLES.add(new Sparkle(x, y, 10 + RANDOM.nextInt(16), COLOURS[RANDOM.nextInt(COLOURS.length)]));
		}
	}

	public static void clear() {
		SPARKLES.clear();
	}
}
