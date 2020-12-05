package de.commands.user;

import de.coaster.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;

public class achievements {

    public static void methode(Member member, TextChannel currchat) {
        EmbedBuilder ms = new EmbedBuilder();
        int table_size = Database.getAchievementLength();
        ArrayList<String> achievements = Database.getAllAchievements(table_size);
        ArrayList<String> desc = Database.getAllDescAchievements(table_size);
        ArrayList<String> userAchievements = Database.getAllUserAchievements(member.getId(), table_size);
        ArrayList<String> reward = Database.getAllRewardsAchievements(table_size);


        ms.setTitle("Information");
        ms.setColor(Color.red);
        int completed = 0;
        int maxpages = (int) Math.ceil(achievements.size() / 12F);

        for (int t = 0; t < achievements.size(); t++) {
            if (t < 12) {
                if (userAchievements.contains(achievements.get(t))) {
                    if (!reward.get(t).equals("nothing")) {
                        ms.addField("Achievement " + achievements.get(t), "✅ " + desc.get(t) + "\n **reward:** " + reward.get(t), true);
                    } else {
                        ms.addField("Achievement " + achievements.get(t), "✅ " + desc.get(t), true);
                    }
                } else {
                    if (!reward.get(t).equals("nothing")) {
                        ms.addField("Achievement " + achievements.get(t), "❌ " + desc.get(t) + "\n **reward:** " + reward.get(t), true);
                    } else {
                        ms.addField("Achievement " + achievements.get(t), "❌ " + desc.get(t), true);
                    }
                }
            } else {

            }
        }

        for (int p = 0; p < achievements.size(); p++) {
            if (userAchievements.contains(achievements.get(p))) {
                completed++;
            }
        }

        ms.setDescription("Completed Achievements: **" + completed + "/" + achievements.size() + "**");
        ms.setFooter("page 1/" + maxpages);

        Message msg = currchat.sendMessage(ms.build()).complete();
        Database.createAchievementRelation(member.getId(), msg.getId());
        msg.addReaction("⬅").queue();
        msg.addReaction("➡").queue();
    }

    public static EmbedBuilder editAchievementPage(Member member, int page, String messageID, String dir) {
        EmbedBuilder ms = new EmbedBuilder();
        try {
            int table_size = Database.getAchievementLength();
            ArrayList<String> achievements = Database.getAllAchievements(table_size);
            ArrayList<String> desc = Database.getAllDescAchievements(table_size);
            ArrayList<String> userAchievements = Database.getAllUserAchievements(member.getId(), table_size);
            ArrayList<String> reward = Database.getAllRewardsAchievements(table_size);

            ms.clearFields();
            ms.setTitle("Inventory");
            ms.setColor(Color.red);
            int completed = 0;
            int startpoint = 1;
            int maxpages = (int) Math.ceil(achievements.size() / 12F);

            if (dir.equals("forward")) {
                if (page > maxpages) {
                    page = 1;
                    Database.setAchievementPage(member.getId(), messageID, 1);
                } else {
                    Database.setAchievementPage(member.getId(), messageID, Database.getAchievementPage(member.getId(), messageID) + 1);
                }
            } else {
                if (page < 1) {
                    page = maxpages;
                    Database.setAchievementPage(member.getId(), messageID, maxpages);
                } else {
                    Database.setAchievementPage(member.getId(), messageID, Database.getAchievementPage(member.getId(), messageID) - 1);
                }
            }
            for (int b = 1; b < page; b++) {
                if (page != 1) {
                    startpoint = startpoint + 11;
                }
            }
            if (page == 1) {
                startpoint = 0;
            }

            for (int t = startpoint; t < achievements.size(); t++) {
                if (t <= (startpoint + 11)) {
                    if (userAchievements.contains(achievements.get(t))) {
                        if (!reward.get(t).equals("nothing")) {
                            ms.addField("Achievement " + achievements.get(t), "✅ " + desc.get(t) + "\n **reward:** " + reward.get(t), true);
                        } else {
                            ms.addField("Achievement " + achievements.get(t), "✅ " + desc.get(t), true);
                        }
                    } else {
                        if (!reward.get(t).equals("nothing")) {
                            ms.addField("Achievement " + achievements.get(t), "❌ " + desc.get(t) + "\n **reward:** " + reward.get(t), true);
                        } else {
                            ms.addField("Achievement " + achievements.get(t), "❌ " + desc.get(t), true);
                        }
                    }
                } else {
                }
            }

            for (int p = 0; p < achievements.size(); p++) {
                if (userAchievements.contains(achievements.get(p))) {
                    completed++;
                }
            }
            ms.setDescription("Completed Achievements: **" + completed + "/" + achievements.size() + "**");
            ms.setFooter("page " + page + "/" + maxpages);


        } catch (NullPointerException n) {
            System.out.println("Exception while showing Achievements");
        }
        return ms;
    }

}
