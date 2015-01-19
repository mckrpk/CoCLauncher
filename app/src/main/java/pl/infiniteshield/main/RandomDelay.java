package pl.infiniteshield.main;

import java.util.Random;

public class RandomDelay {

    private static final Random random = new Random();
    // In seconds
    private static final int SHORT_MEAN = 163;
    private static final int SHORT_VARIANCE = 20;
    // In minutes
    private static final int LONG_MEAN = 7 * 60 + 30;
    private static final int LONG_VARIANCE = 5;

    /*
     * @return Short delay in ms, a bit less than 5 minutes.
     */
    public static int getNextShort() {
        return 1000 * (int) (SHORT_MEAN + random.nextGaussian() * SHORT_VARIANCE);
    }

    /*
     * @return Long delay in ms, a bit less than 8 hours.
     */
    public static int getNextLong() {
        return 1000 * 60 * (int) (LONG_MEAN + random.nextGaussian() * LONG_VARIANCE);
    }
}
