package de.utilities;

import de.coaster.Database;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class getRank {

    public static void methode(Member member, GuildMessageReceivedEvent event, String prefix) {
        int currXP = Database.getXP(member.getId());
        int currLevel = Database.getLevel(member.getId());
        int xpToNextLevel = (currLevel * (currLevel * 15) + 100);
        int z = (currXP * 100) / xpToNextLevel;
        int globalPos = Database.getGlobalPosition(member.getId());
        String title = Database.getTitleFromMember(member.getId());
        //Calculate percantage to display XP Bar
        TextChannel currchat = event.getChannel();
        currchat.sendMessage(createEmbed.methode(member.getEffectiveName() + " " + title, "**Level:** " + currLevel + " <:lv:761544969546629170> \n" + "Global Position: #" + globalPos + "\n **Xp:** " + currXP + "/" + xpToNextLevel + "\n" + calcXPBar.methode(z, "xp") + z + "%", Color.red, "you can view other people rank by typing " + prefix + "rank @person", member, null).build()).queue();

    }

}
