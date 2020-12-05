package de.utilities;

import de.coaster.Database;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class hasVoted {

    public static boolean methode(long curtime, GuildMessageReceivedEvent event) {
        boolean ret = false;
        int vote_time = Database.getStatistic("last_vote", event.getMember().getId());
        if ((curtime / 1000) - vote_time > 43200) {     //check if users last vote is from 12h ago
            //hasnt voted the last 12h
            ret = false;
        } else {
            //has voted
            ret = true;
        }

        return ret;
    }


}
