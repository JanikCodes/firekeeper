package de.utilities;

import de.coaster.Database;
import de.coaster.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class getTime {

    public static int getBossTime(long curtime, GuildMessageReceivedEvent event) {
        int rltime = 0;
        int vote_time = Database.getStatistic("last_vote", event.getMember().getId());
        int won_boss = Database.getStatistic("won_boss", event.getMember().getId());

        if (won_boss == 0){
            if ((curtime / 1000) - vote_time > 43200) {     //check if users last vote is from 12h ago
                //hasnt voted the last 12h
                rltime = Main.bossTime;
            } else {
                //has voted
                rltime = Main.voteBossTime;
            }
        }else{
            if ((curtime / 1000) - vote_time > 43200) {     //check if users last vote is from 12h ago
                //hasnt voted the last 12h
                rltime = Main.bossTimeWon;
            } else {
                //has voted
                rltime = Main.voteBossTimeWon;
            }
        }
        if (findRole.methode(event.getMember(), Main.tier1) != null || findRole.methode(event.getMember(), Main.tier2) != null) {
            //Was a patreon user
            if(won_boss == 0) {
                rltime = Main.patreonTime;
            }else{
                rltime = Main.patreonTimeBossWon;
            }
        }

        return rltime;
    }


    public static int getClearTime(long curtime, GuildMessageReceivedEvent event) {
        int rltime = 0;
        int vote_time = Database.getStatistic("last_vote", event.getMember().getId());
        if ((curtime / 1000) - vote_time > 43200) {     //check if users last vote is from 12h ago
            //hasnt voted the last 12h
            rltime = Main.clearTime;
        } else {
            //has voted
            rltime = Main.voteClearTime;
        }
        if (findRole.methode(event.getMember(), Main.tier1) != null || findRole.methode(event.getMember(), Main.tier2) != null) {
            //Was a patreon user
            rltime = Main.patreonTime;
        }

        return rltime;
    }

}
