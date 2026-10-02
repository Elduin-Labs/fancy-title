package com.elduin.fancy_title.client;

/**
 * The logo pops in when the title screen opens: it grows from nothing, overshoots a little, and
 * settles at its normal size, like something jumping out at you.
 */
public final class PopIn {

	/** How long the pop takes, in seconds. */
	private static final double DURATION = 0.8;

	/** How far past full size it bounces (bigger = bouncier). */
	private static final double BOUNCE = 2.2;

	private static long startNanos = -1;

	private PopIn() {
	}

	public static void start() {
		startNanos = System.nanoTime();
	}

	/** 0 at the start, a bit over 1 at the bounce, exactly 1 once it has settled. */
	public static float scale() {
		if (startNanos < 0) {
			return 1.0f;
		}
		double t = (System.nanoTime() - startNanos) / 1_000_000_000.0 / DURATION;
		if (t >= 1.0) {
			return 1.0f;
		}
		// "ease out back": shoots past 1 and comes back.
		double u = t - 1.0;
		return (float) (1.0 + (BOUNCE + 1.0) * u * u * u + BOUNCE * u * u);
	}

	public static boolean done() {
		return scale() == 1.0f;
	}
}
