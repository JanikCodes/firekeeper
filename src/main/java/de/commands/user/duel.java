package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import de.utilities.initHealthBars;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;

public class duel {

    public static void methode(String idMember1, String idMember2, GuildMessageReactionAddEvent event, TextChannel currchat){

        Member player1 = event.getGuild().getMemberById(idMember1);
        Member player2 = event.getGuild().getMemberById(idMember2);

        int playerHealth1 = Database.getStatistic("vitality",idMember1);
        int playerHealth2 = Database.getStatistic("vitality",idMember2);

        int playerFaith1 = Database.getStatistic("faith",idMember1);
        int playerFaith2 = Database.getStatistic("faith",idMember2);

        int playerEstus1 = getEstus(playerFaith1);
        int playerEstus2 = getEstus(playerFaith2);

        int playerRes1 = Database.getStatistic("Resistance", idMember1);
        int playerRes2 = Database.getStatistic("Resistance", idMember2);

        int playerArmor1 = Database.getArmorBonus(idMember1);
        int playerArmor2 = Database.getArmorBonus(idMember2);

        int playerMaxHealth1 = 250 + (playerHealth1 + playerRes1 + playerArmor1) * 10;
        int playerMaxHealth2 = 250 + (playerHealth2 + playerRes2 + playerArmor2) * 10;

        Message msg = currchat.sendMessage(createEmbed.methode("Duel", "As you enter the arena you see **" + player1.getEffectiveName() + "**!\n \n" + initHealthBars.initDescriptionPvp(idMember1,idMember2, playerMaxHealth1, playerMaxHealth1, playerMaxHealth2, playerMaxHealth2, "", "",event) + "\n \n " + player2.getAsMention() + " is choosing his action! \n \n**React below** to choose your next **action**! \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null).build()).complete();
        msg.addReaction("⚔️").queue();
        msg.addReaction("sh:784184343416537141").queue();
        msg.addReaction("hea:784185738681516053").queue();
        msg.addReaction("ddg:784349634163507240").queue();

    }

    private static int getEstus(Integer faith){
        int amt = 0;

        if (faith == 100) {
            amt = 10;
        } else {
            amt = Integer.parseInt(Integer.toString(faith).substring(0, 1));
        }

        return amt;
    }
}
