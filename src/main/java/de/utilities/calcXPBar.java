package de.utilities;

public class calcXPBar {

    public static String methode(int perc, String reason) {
        String str = "";
        String emp = "";
        int firstDigit = 0;
        if (reason.equals("xp")) {
            if (perc >= 10) {
                if (perc == 100) {
                    firstDigit = 10;
                } else {
                    firstDigit = Integer.parseInt(Integer.toString(perc).substring(0, 1));
                }
            }
        } else if (reason.equals("boss")) {
            if (perc <= 10 && perc > 0) {
                firstDigit = 1;
            } else if (perc == 100) {
                firstDigit = 10;
            } else {
                firstDigit = Integer.parseInt(Integer.toString(perc).substring(0, 1));
            }
        }
        for (int i = 0; i < firstDigit; i++) {
            if (reason.equals("xp")) {
                str = str + "⬜️️️";
            } else if (reason.equals("boss")) {
                str = str + "\uD83D\uDFE9";
            }
        }
        for (int y = 0; y < 10 - firstDigit; y++) {
            if (reason.equals("xp")) {
                emp = emp + "⬛️️️";
            } else if (reason.equals("boss")) {
                str = str + "\uD83D\uDFE5";
            }
        }
        str = str + emp;
        return str;
    }


}
