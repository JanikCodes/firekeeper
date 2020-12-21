package de.commands.user;

import de.coaster.Database;
import de.utilities.calcUpgradeCost;
import de.utilities.calcXPBar;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.awt.*;

public class getProfil {

    public static void methode(Member memmber, GuildMessageReceivedEvent event, String prefix, boolean myself) {
        try {
            int strength = Database.getStatistic("strength", memmber.getId());
            int dexterity = Database.getStatistic("dexterity", memmber.getId());
            int vitality = Database.getStatistic("vitality", memmber.getId());
            int intelligence = Database.getStatistic("intelligence", memmber.getId());
            int resistance = Database.getStatistic("resistance", memmber.getId());
            int faith = Database.getStatistic("faith", memmber.getId());
            int souls = Database.getStatistic("souls", memmber.getId());
            int rank = Database.getStatistic("level", memmber.getId());

            final String[] messageID = {null};

            TextChannel currchat = event.getChannel();
            currchat.sendMessage(createProfilEmbed(strength, dexterity, vitality, intelligence, resistance, faith, rank, memmber, "React below to upgrade your skills", Color.black, souls).build()).queue(message -> {
                if (myself) {
                    message.addReaction("str:761547258205437982").queue();
                    message.addReaction("dex:761546853279203328").queue();
                    message.addReaction("vt:761544992648855564").queue();
                    message.addReaction("in:761546824703410207").queue();
                    message.addReaction("res:761546973115056128").queue();
                    message.addReaction("ft:761546791891894282").queue();
                    message.addReaction("lv:761544969546629170").queue();
                    messageID[0] = message.getId();
                    Database.createUpgradeRelation(messageID[0], memmber.getId());
                }
            });

        }catch(InsufficientPermissionException ip){

        }
    }

    public static EmbedBuilder createProfilEmbed(int strength, int dexterity, int vitality, int intelligence, int resistance, int faith, int rank, Member member, String foot, Color col, Integer souls) {
        String title = Database.getTitleFromMember(member.getId());

        EmbedBuilder ms = new EmbedBuilder();
        ms.setAuthor(member.getEffectiveName() + " " + title, member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        ms.setTitle("Profile information");
        ms.setDescription("Below are your statistics. These statistics are universal and apply on every server with this bot. \n You can upgrade a skill by reacting to the correct emoji at the bottom. \n **Owned souls: " + souls + "**");
        ms.addField("<:str:761547258205437982> **Strength: **" + strength, calcXPBar.methode((strength * 100) / 100, "xp") + strength + "/100  **cost: " + calcUpgradeCost.methode(strength) + "**", false);
        ms.addField("<:dex:761546853279203328> **Dexterity: **" + dexterity, calcXPBar.methode((dexterity * 100) / 100, "xp") + dexterity + "/100  **cost: " + calcUpgradeCost.methode(dexterity) + "**", false);
        ms.addField("<:vt:761544992648855564> **Vitality: **" + vitality, calcXPBar.methode((vitality * 100) / 100, "xp") + vitality + "/100  **cost: " + calcUpgradeCost.methode(vitality) + "**", false);
        ms.addField("<:in:761546824703410207> **Intelligence: **" + intelligence, calcXPBar.methode((intelligence * 100) / 100, "xp") + intelligence + "/100  **cost: " + calcUpgradeCost.methode(intelligence) + "**", false);
        ms.addField("<:res:761546973115056128> **Resistance: **" + resistance, calcXPBar.methode((resistance * 100) / 100, "xp") + resistance + "/100  **cost: " + calcUpgradeCost.methode(resistance) + "**", false);
        ms.addField("<:ft:761546791891894282> **Faith: **" + faith, calcXPBar.methode((faith * 100) / 100, "xp") + faith + "/100  **cost: " + calcUpgradeCost.methode(faith) + "**", false);
        ms.addField("<:lv:761544969546629170> **Convert souls into rank levels**", "You're currently rank **" + rank + "**, cost to level up: **" + (rank * 1100) * 3 + "** \n React with <:lv:761544969546629170> to upgrade!", false);
        ms.setFooter(foot);
        ms.setColor(col);
        return ms;
    }


}
