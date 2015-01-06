package pl.infiniteshield.main;

import java.util.Random;

public class RandomDelay {

    private static final Random random = new Random();
    private static final int MEAN = 163;
    private static final int VARIANCE = 20;

    public static int getNext() {
        return 1000 * (int) (MEAN + random.nextGaussian() * VARIANCE);
    }
}
