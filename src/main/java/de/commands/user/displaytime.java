package de.commands.user;

import de.coaster.Database;
import de.coaster.Main;
import de.utilities.calculateTime;
import de.utilities.createEmbed;
import de.utilities.findRole;
import de.utilities.getTime;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class displaytime {


    public static void methode(TextChannel currchat, GuildMessageReceivedEvent event){
        int lastclear = Database.getStatistic("last_clear", event.getMember().getId());
        int lastboss = Database.getStatistic("last_boss", event.getMember().getId());
        int lastdaily = Database.getStatistic("last_daily", event.getMember().getId());

        long time = System.currentTimeMillis();
        String endTimeClear = calculateTime.methode(getTime.getClearTime(time, event) - ((time / 1000) - lastclear));
        String endTimeBoss = calculateTime.methode(getTime.getBossTime(time, event) - ((time / 1000) - lastboss));
        String endTimeDaily = calculateTime.methode(86400 - ((time / 1000) - lastdaily));

        if(endTimeClear.contains("-")){
            endTimeClear = "**ready to use!**";
        }
        if(endTimeBoss.contains("-")){
            endTimeBoss = "**ready to use!**";
        }
        if (findRole.methode(event.getMember(), Main.tier2) != null) {
            if(endTimeDaily.contains("-")) {
                endTimeDaily = "**ready to use!**";
            }
        }else{
            endTimeDaily = "**Patreon only**";
        }



        currchat.sendMessage(createEmbed.methode("Information","Time till clear: **" + endTimeClear + "**\nTime till bossfight: **" + endTimeBoss + "**\nTime till daily: **" + endTimeDaily + "**", Color.red,null,null,null).build()).queue();
    }
}
