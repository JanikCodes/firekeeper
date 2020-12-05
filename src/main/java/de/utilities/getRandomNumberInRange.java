package de.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class getRandomNumberInRange {

    public static int methode(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
