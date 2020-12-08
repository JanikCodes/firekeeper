package de.commands.user;

import de.coaster.Database;
import de.utilities.createEmbed;
import de.utilities.initHealthBars;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;

public class duel {

    public static void methode(String idMember2, String idMember1, GuildMessageReactionAddEvent event, TextChannel currchat){
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

        Message msg = currchat.sendMessage(createEmbed.methode("Duel", player2.getAsMention() + " please choose an action!\n" + initHealthBars.initDescriptionPvp(idMember1,idMember2, playerMaxHealth1, playerMaxHealth1, playerMaxHealth2, playerMaxHealth2, "", "",event) + "\n\n**React below** to choose your next **action**! \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null).build()).complete();
        msg.addReaction("⚔️").queue();
        msg.addReaction("sh:784184343416537141").queue();
        msg.addReaction("hea:784185738681516053").queue();
        msg.addReaction("ddg:784349634163507240").queue();

        Database.createDuelRelation(msg.getId(),player1.getId(),player2.getId(),playerMaxHealth1,playerMaxHealth2,playerEstus1,playerEstus2,2);
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

    public static EmbedBuilder editDuelMessage(String messageid, String attack_type, GuildMessageReactionAddEvent event) {
        String idMember1 = Database.getDuelPlayerID(messageid,1);
        String idMember2 = Database.getDuelPlayerID(messageid,2);

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

        int playerCurrentHealth1 = Database.getDuelPlayerHeal(messageid,1);
        int playerCurrentHealth2 = Database.getDuelPlayerHeal(messageid,2);

        Member nextPlayerTurnMember = null;
        Member lastPlayerTurnMember = null;

        String player1HealthText = null;
        String player2HealthText = null;

        String last_attack = Database.getDuelLastAttack(messageid);

        int curr_turn = Database.getDuelTurn(messageid);
        if(curr_turn == 1){
            curr_turn = 2;
            nextPlayerTurnMember = player2;
            lastPlayerTurnMember = player1;
        }else if(curr_turn == 2){
            curr_turn = 1;
            nextPlayerTurnMember = player1;
            lastPlayerTurnMember = player2;
        }

        //damage block heal dodge

        //make actionString because most attack_types actually already display what the player is going to do (except attack)
        String actionString = attack_type;
        if(attack_type.equals("damage")){ actionString = "attack"; }


        if(attack_type.equals("damage")){

            if(last_attack.equals("damage")){
                playerCurrentHealth1 = playerCurrentHealth1 - 100;
                playerCurrentHealth2 = playerCurrentHealth2 - 100;
            }else if(last_attack.equals("block")){

            }else if(last_attack.equals("heal")){

            }else if(last_attack.equals("dodge")){

            }

        }

        Database.updateDuelHealth(messageid,playerCurrentHealth1,1);
        Database.updateDuelHealth(messageid,playerCurrentHealth2,2);
        Database.updateDuelTurn(messageid,curr_turn);

        if(curr_turn == 1) {
            Database.updateDuelLastAttack(messageid,attack_type);
        }else{
            Database.updateDuelLastAttack(messageid,"none");
        }

        EmbedBuilder mb = createEmbed.methode("Duel",nextPlayerTurnMember.getAsMention() + " is now **choosing** his action! \n" + initHealthBars.initDescriptionPvp(idMember1,idMember2, playerCurrentHealth1, playerMaxHealth1, playerCurrentHealth2, playerMaxHealth2, "", "",event) + "\n\n❗️ " + lastPlayerTurnMember.getEffectiveName() + " is about to **"+ actionString + "**!\n\n**" + player1.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus1 + "**x \n**" + player2.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus2 + "**x \n\n**React below** to choose your next **action**!\n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null);
        return mb;
    }
}
