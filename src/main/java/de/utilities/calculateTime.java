package de.utilities;

import java.util.concurrent.TimeUnit;

public class calculateTime {

    public static String methode(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        //maybe display seconds in the future when its 0h and 0minutes
        return hours + "h " + minute + "min.";
    }
}
