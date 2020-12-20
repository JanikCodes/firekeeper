package de.commands.patreon;

import de.coaster.Database;
import de.utilities.calculateTime;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class collectDaily extends ListenerAdapter {


    public static void methode(String memberid, TextChannel currchat) {
        long time = System.currentTimeMillis();
        int newtime = Math.toIntExact((time / 1000));
        int lastdaily = Database.getStatistic("last_daily", memberid);

        if ((time / 1000) - lastdaily > 86400) {
            Database.updateDailyTime(newtime, memberid);
            Database.giveSouls(memberid,30000);
            currchat.sendMessage(createEmbed.methode("Patreon bonus!","You've collected **30000 souls**! \n You can use this command again in **24 hours**.", Color.green,null,null,null).build()).queue();
        }else {
            //Can't do boss
            String endTime = calculateTime.methode(86400 - ((time / 1000) - lastdaily));
            currchat.sendMessage(createEmbed.methode("**ERROR**", "You've already collected your daily bonus! \n You'll need to wait **" + endTime + "** to collect it again!", Color.orange, null, null, null).build()).queue();
        }
    }


}
