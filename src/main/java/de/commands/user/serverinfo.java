package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.xml.soap.Text;
import java.awt.*;
import java.util.List;

public class serverinfo {

    public static void methode(GuildMessageReceivedEvent event,String serverID, String prefix, TextChannel currchat){
        List<Member> members = event.getGuild().getMembers();
        int memcount = members.size();
        String channelID = Database.getNotificationChannel(serverID);
        int clearAmount = Database.getTotalClearsFromServer(serverID);
        int bossAmount = Database.getTotalBossesFromServer(serverID);
        int levelnoti = Database.getNotificationState(serverID);
        TextChannel channel = null;

        if (!channelID.equals("empty")) {
            channel = event.getChannel().getGuild().getTextChannelById(channelID);
        }

        String levelUpString = null;
        if (levelnoti == 2) {
            levelUpString = "disabled";
        } else {
            levelUpString = "enabled";
        }

        if (channelID.equals("empty") || event.getChannel().getGuild().getTextChannelById(channelID) == null) {
            currchat.sendMessage(createEmbed.methode("Information",
                    "Server Member: **" + memcount + "** \n " +
                            "Server prefix: **" + prefix + "** \n" +
                            "Level-up channel: **not set** \n" +
                            "Level-up notification: **" + levelUpString + "** \n" +
                            "Defeated bosses on this server: **" + bossAmount + "** \n" +
                            "Cleared areas on this server: **" + clearAmount + "** \n"
                    , Color.red, null, null, null).build()).complete();
        } else {
            currchat.sendMessage(createEmbed.methode("Information",
                    "Server Member: **" + memcount + "** \n " +
                            "Server prefix: **" + prefix + "** \n" +
                            "Level-up channel: **" + channel.getAsMention() + "** \n" +
                            "Level-up notification: **" + levelUpString + "** \n" +
                            "Defeated bosses on this server: **" + bossAmount + "** \n" +
                            "Cleared areas on this server: **" + clearAmount + "** \n"
                    , Color.red, null, null, null).build()).complete();
        }
    }

    public static void globalInfo(GuildMessageReceivedEvent event, TextChannel currchat){
        //int totalMemberCount = Database.getTotalMemberCount();
        //int totalServerCount = Database.getTotalServerCount();
        int totalBossAmount = Database.getTotalBossCount();
        int totalClearAmount = Database.getTotalClearCount();

        List realServerCount = event.getJDA().getGuilds();
        List realMemberCount = event.getJDA().getUsers();

        currchat.sendMessage(createEmbed.methode("Information",
                "Global member count: **" + realMemberCount.size() + "** \n" +
                        "Global server count: **" + realServerCount.size() + "** \n" +
                        "Defeated bosses globally: **" + totalBossAmount + "** \n" +
                        "Cleared areas globally: **" + totalClearAmount + "** \n"
                , Color.red, null, null, null).build()).complete();
    }
}
