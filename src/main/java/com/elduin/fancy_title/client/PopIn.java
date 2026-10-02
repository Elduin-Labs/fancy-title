package com.elduin.fancy_title.client;

/**
 * The logo hides and pops out again, over and over, every three seconds: it bursts out of nothing,
 * overshoots a little, settles, sits there shining, then shrinks away and does it again.
 */
public final class PopIn {

	/** One whole hide-and-pop, in seconds. */
	private static final double CYCLE = 3.0;
	/** The pop out at the start of each cycle. */
	private static final double POP = 0.8;
	/** When it starts shrinking away again. */
	private static final double HIDE_FROM = 2.6;

	/** How far past full size it bounces (bigger = bouncier). */
	private static final double BOUNCE = 2.2;

	private static long startNanos = -1;
	private static long lastCycleSeen = -1;

	private PopIn() {
	}

	public static void start() {
		startNanos = System.nanoTime();
		lastCycleSeen = -1;
	}

	private static double seconds() {
		return startNanos < 0 ? 0.0 : (System.nanoTime() - startNanos) / 1_000_000_000.0;
	}

	/** How big the logo is right now: 0 is hidden, 1 is normal, a bit over 1 at the bounce. */
	public static float scale() {
		if (startNanos < 0) {
			return 1.0f;
		}
		double phase = seconds() % CYCLE;
		if (phase < POP) {
			// "ease out back": shoots past 1 and comes back.
			double u = phase / POP - 1.0;
			return (float) (1.0 + (BOUNCE + 1.0) * u * u * u + BOUNCE * u * u);
		}
		if (phase < HIDE_FROM) {
			return 1.0f;
		}
		double t = (phase - HIDE_FROM) / (CYCLE - HIDE_FROM);
		return (float) (1.0 - t * t);
	}

	/** True once each cycle, the first time it's asked after the logo starts popping out. */
	public static boolean justPopped() {
		if (startNanos < 0) {
			return false;
		}
		long cycle = (long) (seconds() / CYCLE);
		if (cycle != lastCycleSeen) {
			lastCycleSeen = cycle;
			return true;
		}
		return false;
	}
}
