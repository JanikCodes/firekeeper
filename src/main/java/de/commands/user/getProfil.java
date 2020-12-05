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

import java.awt.*;

public class getProfil {

    public static void methode(Member memmber, GuildMessageReceivedEvent event, String prefix, boolean myself) {
        int strength = Database.getStatistic("strength", memmber.getId());
        int dexterity = Database.getStatistic("dexterity", memmber.getId());
        int vitality = Database.getStatistic("vitality", memmber.getId());
        int intelligence = Database.getStatistic("intelligence", memmber.getId());
        int resistance = Database.getStatistic("resistance", memmber.getId());
        int faith = Database.getStatistic("faith", memmber.getId());
        int souls = Database.getStatistic("souls", memmber.getId());
        int rank = Database.getStatistic("level", memmber.getId());

        TextChannel currchat = event.getChannel();
        Message msg = currchat.sendMessage(createProfilEmbed(strength, dexterity, vitality, intelligence, resistance, faith, rank, memmber, "React below to upgrade your skills", Color.black, souls).build()).complete();
        if (myself) {
            try {
                msg.addReaction("str:761547258205437982").queue();
                msg.addReaction("dex:761546853279203328").queue();
                msg.addReaction("vt:761544992648855564").queue();
                msg.addReaction("in:761546824703410207").queue();
                msg.addReaction("res:761546973115056128").queue();
                msg.addReaction("ft:761546791891894282").queue();
                msg.addReaction("lv:761544969546629170").queue();
            } catch (ErrorResponseException e) {
            }
            Database.createUpgradeRelation(msg.getId(), memmber.getId());
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
