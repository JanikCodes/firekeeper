package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class leaderboard {

    public static void methode(String arg1, GuildMessageReceivedEvent event, TextChannel currchat){

        if (arg1.equalsIgnoreCase("ranks")) {
            ArrayList<String> leaderboard = Database.showLevelLeaderboard();
            ArrayList<Integer> level = new ArrayList<>();
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> title = new ArrayList<>();


            int globalPos = Database.getGlobalPosition(event.getMember().getId());
            String BigString = "Displaying the top **15** ranks globally! \n";

            for (int i = 0; i < leaderboard.size(); i++) {
                level.add(Database.showLevelLeaderboardOrderBy(leaderboard.get(i)));
                username.add(Database.showUsernameLeaderboardLevel(leaderboard.get(i)));
                title.add(Database.showTitleLeaderboardLevel(leaderboard.get(i)));
                if (event.getGuild().getMemberById(leaderboard.get(i)) != null) {
                    BigString = BigString + "**" + (i + 1) + "**. " + event.getGuild().getMemberById(leaderboard.get(i)).getAsMention() + " " + title.get(i) + " - Level " + level.get(i) + "\n";
                } else {
                    BigString = BigString + "**" + (i + 1) + "**. **" + username.get(i) + "** " + title.get(i) + " - Level " + level.get(i) + "\n";
                }
            }
            currchat.sendMessage(createEmbed.methode("Leaderboard", BigString, Color.red, "Your position in the leaderboard is at #" + globalPos, null, null).build()).queue();

        } else if (arg1.equalsIgnoreCase("souls")) {
            ArrayList<String> leaderboard = Database.showSoulsLeaderboard();
            ArrayList<Integer> souls = new ArrayList<>();
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> title = new ArrayList<>();

            int globalPos = Database.getGlobalSoulPosition(event.getMember().getId());
            String BigString = "Displaying the top **15** users with the most souls! \n";

            for (int i = 0; i < leaderboard.size(); i++) {
                souls.add(Database.showSoulsLeaderboardOrderBy(leaderboard.get(i)));
                username.add(Database.showUsernameLeaderboardSouls(leaderboard.get(i)));
                title.add(Database.showTitleLeaderboardSouls(leaderboard.get(i)));

                if (event.getGuild().getMemberById(leaderboard.get(i)) != null) {
                    BigString = BigString + "**" + (i + 1) + "**. " + event.getGuild().getMemberById(leaderboard.get(i)).getAsMention() + " " + title.get(i) + " - **" + souls.get(i) + "** Souls \n";
                } else {
                    BigString = BigString + "**" + (i + 1) + "**. **" + username.get(i) + "** " + title.get(i) + " - **" + souls.get(i) + "** Souls \n";
                }
            }
            currchat.sendMessage(createEmbed.methode("Leaderboard", BigString, Color.red, "Your position in the leaderboard is at #" + globalPos, null, null).build()).queue();
        }
    }
}
