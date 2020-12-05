package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class getSouls {

    public static void methode(Member member, GuildMessageReceivedEvent event, String prefix) {
        int souls = Database.getStatistic("souls", member.getId());
        TextChannel currchat = event.getChannel();
        currchat.sendMessage(createEmbed.methode("Information", "You currently have **" + souls + " souls**! <:sl:761557221590433792>", Color.red, "You can view other peoples souls by typing " + prefix + "souls @person.", null, null).build()).complete();
    }

}
