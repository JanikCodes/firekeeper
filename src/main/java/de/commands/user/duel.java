package de.commands.user;

import de.coaster.Database;
import de.coaster.Main;
import de.utilities.createEmbed;
import de.utilities.getRandomNumberInRange;
import de.utilities.initHealthBars;
import de.utilities.rewardUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.exceptions.PermissionException;

import javax.xml.crypto.Data;
import java.awt.*;

public class duel {

    public static void methode(String idMember2, String idMember1, GuildMessageReactionAddEvent event, TextChannel currchat){
        Member player1 = event.getGuild().retrieveMemberById(idMember1).complete();
        Member player2 = event.getGuild().retrieveMemberById(idMember2).complete();

        int playerHealth1 = Database.getStatistic("vitality",idMember1);
        int playerHealth2 = Database.getStatistic("vitality",idMember2);

        int playerFaith1 = Database.getStatistic("faith",idMember1);
        int playerFaith2 = Database.getStatistic("faith",idMember2);

        int playerEstus1 = getEstus(playerFaith1);
        int playerEstus2 = getEstus(playerFaith2);

        int playerRes1 = Database.getStatistic("Resistance", idMember1);
        int playerRes2 = Database.getStatistic("Resistance", idMember2);

        //int playerArmor1 = Database.getArmorBonus(idMember1);
        //int playerArmor2 = Database.getArmorBonus(idMember2);

        int playerMaxHealth1 = 150 + ((playerHealth1 * 2)+ playerRes1) * 8;
        int playerMaxHealth2 = 150 + ((playerHealth2 * 2)+ playerRes2) * 8;

        final String[] messageID = {null};

        currchat.sendMessage(createEmbed.methode("Duel", player2.getAsMention() + " please choose an action!\n" + initHealthBars.initDescriptionPvp(idMember1,idMember2, playerMaxHealth1, playerMaxHealth1, playerMaxHealth2, playerMaxHealth2, "", "",event) + "\n\n**" + player1.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus1 + "**x \n**" + player2.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus2 + "**x \n\n**React below** to choose your next **action**! \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null).build()).queue(message -> {
            message.addReaction("⚔️").queue();
            message.addReaction("sh:784184343416537141").queue();
            message.addReaction("hea:784185738681516053").queue();
            message.addReaction("ddg:784349634163507240").queue();
            messageID[0] = message.getId();
            int time = Math.toIntExact((System.currentTimeMillis() / 1000));
            Database.createDuelRelation(messageID[0],player1.getId(),player2.getId(),playerMaxHealth1,playerMaxHealth2,playerEstus1,playerEstus2,2, time, currchat.getId());
        });

    }

    public static int getEstus(Integer faith){
        int amt = 0;

        if(Integer.toString(faith).length() == 1){
            amt = 0;
        }else if (faith == 100) {
            amt = 10;
        } else {
            amt = Integer.parseInt(Integer.toString(faith).substring(0, 1));
        }

        return amt;
    }

    public static EmbedBuilder editDuelMessage(String messageid, String attack_type, GuildMessageReactionAddEvent event) {
        String idMember1 = Database.getDuelPlayerID(messageid,1);
        String idMember2 = Database.getDuelPlayerID(messageid,2);

        Member player1 = event.getGuild().retrieveMemberById(idMember1).complete();
        Member player2 = event.getGuild().retrieveMemberById(idMember2).complete();

        int playerHealth1 = Database.getStatistic("vitality",idMember1);
        int playerHealth2 = Database.getStatistic("vitality",idMember2);

        int playerFaith1 = Database.getStatistic("faith",idMember1);
        int playerFaith2 = Database.getStatistic("faith",idMember2);

        int playerStrength1 = Database.getStatistic("strength",idMember1);
        int playerStrength2 = Database.getStatistic("strength",idMember2);

        int playerDexterity1 = Database.getStatistic("dexterity",idMember1);
        int playerDexterity2 = Database.getStatistic("dexterity",idMember2);

        int playerEstus1 = Database.getDuelEstusCount(messageid,1);
        int playerEstus2 = Database.getDuelEstusCount(messageid,2);

        int playerRes1 = Database.getStatistic("Resistance", idMember1);
        int playerRes2 = Database.getStatistic("Resistance", idMember2);

        int playerArmor1 = Database.getArmorBonus(idMember1);
        int playerArmor2 = Database.getArmorBonus(idMember2);

        int playerWeapon1 = Database.getWeaponDamage(idMember1);
        int playerWeapon2 = Database.getWeaponDamage(idMember2);

        int playerMaxHealth1 = 150 + ((playerHealth1 * 2) + playerRes1) * 8;
        int playerMaxHealth2 = 150 + ((playerHealth2 * 2) + playerRes2) * 8;

        playerRes1 = playerRes1 / 2;
        playerRes2 = playerRes2 / 2;


        int playerCurrentHealth1 = Database.getDuelPlayerHeal(messageid,1);
        int playerCurrentHealth2 = Database.getDuelPlayerHeal(messageid,2);

        int rndDMG1 = getRandomNumberInRange.methode(5,30);

        int playerDamage1 = (100 + ((playerStrength1/2) + (playerDexterity1/2) + playerWeapon1) * 2) + rndDMG1;
        int playerDamage2 = (100 + ((playerStrength2/2) + (playerDexterity2/2) + playerWeapon2) * 2) + rndDMG1;

        int healAmount = 550;

        int dodgeCounter1 = 0;
        int dodgeCounter2 = 0;

        Member nextPlayerTurnMember = null;
        Member lastPlayerTurnMember = null;

        String player1HealthText = "";
        String player2HealthText = "";

        String last_attack = Database.getDuelLastAttack(messageid);

        int extra_turn = Database.getDuelExtraTurn(messageid);
        int curr_turn = Database.getDuelTurn(messageid);
        String actionString = attack_type;
        String playerReactString = null;

        if(extra_turn == 1){
            curr_turn = 1;
            nextPlayerTurnMember = player1;
            lastPlayerTurnMember = player2;
            Database.updateDuelLastAttack(messageid,attack_type);
            playerReactString = nextPlayerTurnMember.getAsMention() + " is reacting to the latest action";
        }else if(extra_turn == 2){
            curr_turn = 1;
            nextPlayerTurnMember = player1;
            lastPlayerTurnMember = player2;
            Database.updateDuelLastAttack(messageid,"none");
            playerReactString = nextPlayerTurnMember.getAsMention() + " is choosing his action";
        }else if(extra_turn == 3){
            curr_turn = 2;
            nextPlayerTurnMember = player2;
            lastPlayerTurnMember = player1;
            Database.updateDuelLastAttack(messageid,attack_type);
            playerReactString = nextPlayerTurnMember.getAsMention() + " is reacting to the latest action";
        }else if(extra_turn == 4){
            curr_turn = 2;
            nextPlayerTurnMember = player2;
            lastPlayerTurnMember = player1;
            Database.updateDuelLastAttack(messageid,"none");
            playerReactString = nextPlayerTurnMember.getAsMention() + " is choosing his action";
        }

        if(attack_type.equals("damage")){

            if(last_attack.equals("damage")){
                if (curr_turn == 1){

                    if(((playerCurrentHealth2 - (playerDamage1 - playerArmor2))) <= 0){
                        playerCurrentHealth2 = 0;
                    }else{
                        playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                    }

                    if(((playerCurrentHealth1 - (playerDamage2 - playerArmor1))) <= 0){
                        playerCurrentHealth1 = 0;
                    }else{
                        playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                    }

                    player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**";
                    player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**";
                }else{

                    if(((playerCurrentHealth1 - (playerDamage2 - playerArmor1))) <= 0){
                        playerCurrentHealth1 = 0;
                    }else{
                        playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                    }

                    if(((playerCurrentHealth2 - (playerDamage1 - playerArmor2))) <= 0){
                        playerCurrentHealth2 = 0;
                    }else{
                        playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                    }

                    player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**";
                    player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**";

                }
            }else if(last_attack.equals("block")){
                if (curr_turn == 1){

                    if(((playerCurrentHealth2 - ((playerDamage1 - playerArmor2 - playerRes2) / 2))) <= 0){
                        playerCurrentHealth2 = 0;
                        player1HealthText = "** -" + ((playerDamage1 - playerArmor2 - playerRes2) / 2) + "**, blocked 50% damage!";
                    }else if((playerDamage1 - playerArmor2 - playerRes2) <= 0){
                        playerCurrentHealth2 = playerCurrentHealth2 - 25;
                        player1HealthText = "** -25**, blocked 50% damage!";
                    }else{
                        playerCurrentHealth2 = playerCurrentHealth2 - ((playerDamage1 - playerArmor2 - playerRes2) / 2);
                        player1HealthText = "** -" + ((playerDamage1 - playerArmor2 - playerRes2) / 2) + "**, blocked 50% damage!";
                    }
                    player2HealthText = "";
                }else{

                    if(((playerCurrentHealth1 - ((playerDamage2 - playerArmor1 - playerRes1) / 2))) <= 0){
                        playerCurrentHealth1 = 0;
                        player2HealthText = "** -" + ((playerDamage2 - playerArmor1 - playerRes1) / 2) + "**, blocked 50% damage!";
                    }else if((playerDamage2 - playerArmor1 - playerRes1) <= 0){
                        playerCurrentHealth1 = playerCurrentHealth1 - 25;
                        player2HealthText = "** -25**, blocked 50% damage!";
                    }else{
                        playerCurrentHealth1 = playerCurrentHealth1 - ((playerDamage2 - playerArmor1 - playerRes1) / 2);
                        player2HealthText = "** -" + ((playerDamage2 - playerArmor1 - playerRes1) / 2) + "**, blocked 50% damage!";
                    }
                    player1HealthText = "";

                }
            }else if(last_attack.equals("heal")){
                if (curr_turn == 1){

                    if((Database.getDuelEstusCount(messageid,2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        if ((playerCurrentHealth2 - (playerDamage1- playerArmor2)) + healAmount <= 0) {
                            playerCurrentHealth2 = 0;
                        } else {
                            playerCurrentHealth2 = (playerCurrentHealth2 - (playerDamage1 - playerArmor2)) + healAmount;
                        }

                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, healed " + (healAmount - (playerDamage1 - playerArmor2)) + " health!";
                        player2HealthText = "";
                    }else{

                        if(((playerCurrentHealth2 - (playerDamage1 - playerArmor2))) <= 0){
                            playerCurrentHealth2 = 0;
                        }else{
                            playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                        }

                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, no healing left!";
                        player2HealthText = "";
                    }

                }else{

                    if((Database.getDuelEstusCount(messageid,1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        if ((playerCurrentHealth1 - (playerDamage2- playerArmor1)) + healAmount <= 0) {
                            playerCurrentHealth1 = 0;
                        } else {
                            playerCurrentHealth1 = (playerCurrentHealth1 - (playerDamage2 - playerArmor1)) + healAmount;
                        }

                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, healed " + (healAmount - (playerDamage2 - playerArmor1)) + " health!";
                        player1HealthText = "";
                    }else{

                        if(((playerCurrentHealth1 - (playerDamage2 - playerArmor1))) <= 0){
                            playerCurrentHealth1 = 0;
                        }else{
                            playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                        }

                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, no healing left!";
                        player1HealthText = "";
                    }

                }
            }else if(last_attack.equals("dodge")){
                if (curr_turn == 1) {
                    int dodgechance = 25 + (Database.getStatistic("intelligence",idMember2) / 2);
                    int randomNum = getRandomNumberInRange.methode(1, 100);
                    dodgeCounter2++;
                    //CAN HE DODGE
                    if ((dodgechance / 2) >= randomNum) {
                        player1HealthText = "** dodged!**";
                    } else {
                        if ((playerCurrentHealth2 - (playerDamage1 - playerArmor2)) <= 0) {
                            playerCurrentHealth2 = 0;
                        } else {
                            playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                        }
                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, couldn't dodge!";

                    }
                    player2HealthText = "";

                }else{
                    int dodgechance = 25 + (Database.getStatistic("intelligence",idMember1) / 2);
                    int randomNum = getRandomNumberInRange.methode(1, 100);
                    dodgeCounter1++;
                    //CAN HE DODGE
                    if ((dodgechance / 2) >= randomNum) {
                        player2HealthText = "** dodged!**";
                    } else {
                        if ((playerCurrentHealth1 - (playerDamage2 - playerArmor1)) <= 0) {
                            playerCurrentHealth1 = 0;
                        } else {
                            playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                        }
                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, couldn't dodge!";

                    }
                    player1HealthText = "";
                }
            }

        }else if(attack_type.equals("block")){

            if(last_attack.equals("damage")){
                if (curr_turn == 1){

                    if(((playerCurrentHealth1 - ((playerDamage2 - playerArmor1 - playerRes1) / 2))) <= 0){
                        playerCurrentHealth1 = 0;
                        player2HealthText = "** -" + ((playerDamage2 - playerArmor1 - playerRes1) / 2) + "**, blocked 50% damage!";
                    }else if((playerDamage2 - playerArmor1 - playerRes1) <= 0){
                        playerCurrentHealth1 = playerCurrentHealth1 - 25;
                        player2HealthText = "** -25**, blocked 50% damage!";
                    }else{
                        playerCurrentHealth1 = playerCurrentHealth1 - ((playerDamage2 - playerArmor1 - playerRes1) / 2);
                        player2HealthText = "** -" + ((playerDamage2 - playerArmor1 - playerRes1) / 2) + "**, blocked 50% damage!";
                    }
                    player1HealthText = "";
                }else{

                    if(((playerCurrentHealth2 - ((playerDamage1 - playerArmor2 - playerRes2) / 2))) <= 0){
                        playerCurrentHealth2 = 0;
                        player1HealthText = "** -" + ((playerDamage1 - playerArmor2 - playerRes2) / 2) + "**, blocked 50% damage!";

                    }else if((playerDamage1 - playerArmor2 - playerRes2) <= 0){
                        playerCurrentHealth2 = playerCurrentHealth2 - 25;
                        player1HealthText = "** -25**, blocked 50% damage!";
                    }else{
                        playerCurrentHealth2 = playerCurrentHealth2 - ((playerDamage1 - playerArmor2 - playerRes2) / 2);
                        player1HealthText = "** -" + ((playerDamage1 - playerArmor2 - playerRes2) / 2) + "**, blocked 50% damage!";

                    }
                    player2HealthText = "";

                }
            }else if(last_attack.equals("block")){
                if (curr_turn == 1) {
                    player1HealthText = " blocked nothing";
                    player2HealthText = " blocked nothing";
                }else{
                    player1HealthText = " blocked nothing";
                    player2HealthText = " blocked nothing";
                }

            }else if(last_attack.equals("heal")){
                if (curr_turn == 1){

                    if((Database.getDuelEstusCount(messageid,2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        playerCurrentHealth2 = playerCurrentHealth2 + healAmount;

                        player1HealthText = " healed " + healAmount + " health!";
                        player2HealthText = " blocked nothing";
                    }else{
                        player1HealthText = " no healing left!";
                        player2HealthText = " blocked nothing";
                    }
                }else{
                    if((Database.getDuelEstusCount(messageid,1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        playerCurrentHealth1 = playerCurrentHealth1 + healAmount;

                        player2HealthText = " healed " + healAmount + " health!";
                        player1HealthText = " blocked nothing";
                    }else{
                        player2HealthText = " no healing left!";
                        player1HealthText = " blocked nothing";
                    }
                }
            }else if(last_attack.equals("dodge")){
                if (curr_turn == 1) {
                    dodgeCounter2++;
                    player1HealthText = " dodged for nothing";
                    player2HealthText = " blocked nothing";
                }else{
                    dodgeCounter1++;
                    player1HealthText = " blocked nothing";
                    player2HealthText = " dodged for nothing";
                }
            }


        }else if(attack_type.equals("dodge")){

            if(last_attack.equals("damage")){
                if (curr_turn == 1) {
                    int dodgechance = 25 + (Database.getStatistic("intelligence",idMember2) / 2);
                    int randomNum = getRandomNumberInRange.methode(1, 100);
                    dodgeCounter2++;
                    //CAN HE DODGE
                    if ((dodgechance / 2) >= randomNum) {
                        player2HealthText = "** dodged!**";
                    } else {
                        if ((playerCurrentHealth1 - (playerDamage2 - playerArmor1)) <= 0) {
                            playerCurrentHealth1 = 0;
                        } else {
                            playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                        }
                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, couldn't dodge!";

                    }
                    player1HealthText = "";

                }else{
                    int dodgechance = 25 + (Database.getStatistic("intelligence",idMember1) / 2);
                    int randomNum = getRandomNumberInRange.methode(1, 100);
                    dodgeCounter1++;
                    //CAN HE DODGE
                    if ((dodgechance / 2) >= randomNum) {
                        player1HealthText = "** dodged!**";
                    } else {
                        if ((playerCurrentHealth2 - (playerDamage1 - playerArmor2)) <= 0) {
                            playerCurrentHealth2 = 0;
                        } else {
                            playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                        }
                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, couldn't dodge!";

                    }
                    player2HealthText = "";
                }
            }else if(last_attack.equals("block")){
                if (curr_turn == 1) {
                    dodgeCounter2++;
                    player1HealthText = " blocked nothing";
                    player2HealthText = " dodged for nothing";
                }else{
                    dodgeCounter1++;
                    player1HealthText = " dodged for nothing";
                    player2HealthText = " blocked nothing";
                }

            }else if(last_attack.equals("heal")){
                if (curr_turn == 1){

                    if((Database.getDuelEstusCount(messageid,2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        playerCurrentHealth2 = playerCurrentHealth2 + healAmount;

                        player1HealthText = " healed " + healAmount + " health!";
                        player2HealthText = " dodged for nothing";
                    }else{
                        player1HealthText = " no healing left!";
                        player2HealthText = " dodged for nothing";
                    }
                    dodgeCounter2++;
                }else{
                    if((Database.getDuelEstusCount(messageid,1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        playerCurrentHealth1 = playerCurrentHealth1 + healAmount;

                        player2HealthText = " healed " + healAmount + " health!";
                        player1HealthText = " dodged for nothing";
                    }else{
                        player2HealthText = " no healing left!";
                        player1HealthText = " dodged for nothing";
                    }
                    dodgeCounter1++;
                }
            }else if(last_attack.equals("dodge")){
                if (curr_turn == 1) {
                    dodgeCounter2++;
                    player1HealthText = " dodged for nothing";
                    player2HealthText = " dodged for nothing";
                }else{
                    dodgeCounter1++;
                    player1HealthText = " dodged for nothing";
                    player2HealthText = " dodged for nothing";
                }
            }

        }else if(attack_type.equals("heal")){
            if(last_attack.equals("damage")){
                if (curr_turn == 2){

                    if((Database.getDuelEstusCount(messageid,2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        if ((playerCurrentHealth2 - (playerDamage1- playerArmor2)) + healAmount <= 0) {
                            playerCurrentHealth2 = 0;
                        } else {
                            playerCurrentHealth2 = (playerCurrentHealth2 - (playerDamage1 - playerArmor2)) + healAmount;
                        }

                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, healed " + (healAmount - (playerDamage1 - playerArmor2)) + " health!";
                        player2HealthText = "";
                    }else{

                        if(((playerCurrentHealth2 - (playerDamage1 - playerArmor2))) <= 0){
                            playerCurrentHealth2 = 0;
                        }else{
                            playerCurrentHealth2 = playerCurrentHealth2 - (playerDamage1 - playerArmor2);
                        }

                        player1HealthText = "** -" + (playerDamage1 - playerArmor2) + "**, no healing left!";
                        player2HealthText = "";
                    }

                }else{

                    if((Database.getDuelEstusCount(messageid,1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        if ((playerCurrentHealth1 - (playerDamage2- playerArmor1)) + healAmount <= 0) {
                            playerCurrentHealth1 = 0;
                        } else {
                            playerCurrentHealth1 = (playerCurrentHealth1 - (playerDamage2 - playerArmor1)) + healAmount;
                        }

                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, healed " + (healAmount - (playerDamage2 - playerArmor1)) + " health!";
                        player1HealthText = "";
                    }else{

                        if(((playerCurrentHealth1 - (playerDamage2 - playerArmor1))) <= 0){
                            playerCurrentHealth1 = 0;
                        }else{
                            playerCurrentHealth1 = playerCurrentHealth1 - (playerDamage2 - playerArmor1);
                        }

                        player2HealthText = "** -" + (playerDamage2 - playerArmor1) + "**, no healing left!";
                        player1HealthText = "";
                    }

                }
            }else if(last_attack.equals("block")){
                if (curr_turn == 2){

                    if((Database.getDuelEstusCount(messageid,2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        playerCurrentHealth2 = playerCurrentHealth2 + healAmount;

                        player1HealthText = " healed " + healAmount + " health!";
                        player2HealthText = " blocked nothing";
                    }else{
                        player1HealthText = " no healing left!";
                        player2HealthText = " blocked nothing";
                    }
                }else{
                    if((Database.getDuelEstusCount(messageid,1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        playerCurrentHealth1 = playerCurrentHealth1 + healAmount;

                        player2HealthText = " healed " + healAmount + " health!";
                        player1HealthText = " blocked nothing";
                    }else{
                        player2HealthText = " no healing left!";
                        player1HealthText = " blocked nothing";
                    }
                }
            }else if(last_attack.equals("heal")){
                    if ((Database.getDuelEstusCount(messageid, 2)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        playerCurrentHealth2 = playerCurrentHealth2 + healAmount;

                        player1HealthText = " healed " + healAmount + " health!";
                    } else {
                        player1HealthText = " no healing left!";

                    }
                    if ((Database.getDuelEstusCount(messageid, 1)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        playerCurrentHealth1 = playerCurrentHealth1 + healAmount;

                        player2HealthText = " healed " + healAmount + " health!";
                    } else {
                        player2HealthText = " no healing left!";
                    }
            }else if(last_attack.equals("dodge")) {
                if (curr_turn == 1) {
                    if ((Database.getDuelEstusCount(messageid, 2)) >= 1) {
                        if (healAmount + playerCurrentHealth1 > playerMaxHealth1) {
                            healAmount = playerMaxHealth1 - playerCurrentHealth1;
                        }
                        Database.removeEstusFromDuel(messageid, 2);

                        playerCurrentHealth1 = playerCurrentHealth1 + healAmount;

                        player2HealthText = " healed " + healAmount + " health!";
                        player1HealthText = " dodged for nothing";
                        dodgeCounter2++;
                    } else {
                        player2HealthText = " no healing left!";
                        player1HealthText = " dodged for nothing";
                        dodgeCounter1++;
                    }
                } else {
                    if ((Database.getDuelEstusCount(messageid, 1)) >= 1) {
                        if (healAmount + playerCurrentHealth2 > playerMaxHealth2) {
                            healAmount = playerMaxHealth2 - playerCurrentHealth2;
                        }
                        Database.removeEstusFromDuel(messageid, 1);

                        playerCurrentHealth2 = playerCurrentHealth2 + healAmount;

                        player1HealthText = " healed " + healAmount + " health!";
                        player2HealthText = " dodged for nothing";
                        dodgeCounter2++;
                    } else {
                        player1HealthText = " no healing left!";
                        player2HealthText = " dodged for nothing";
                        dodgeCounter1++;
                    }
                }
            }
        }

        playerEstus1 = Database.getDuelEstusCount(messageid,1);
        playerEstus2 = Database.getDuelEstusCount(messageid,2);


        //Display the different states in a string
        String isNone = Database.getDuelLastAttack(messageid);
        if(!isNone.equals("none")) {
            if (attack_type.equals("damage")) {
                actionString = lastPlayerTurnMember.getEffectiveName() + " **is about to attack** " + nextPlayerTurnMember.getEffectiveName() + "!";
            }else if(attack_type.equals("block")){
                actionString = lastPlayerTurnMember.getEffectiveName() + " **is about to block incoming attacks!**";
            }else if(attack_type.equals("dodge")){
                actionString = lastPlayerTurnMember.getEffectiveName() + " **is about to dodge the next attack!**";
            }else if(attack_type.equals("heal")){
                actionString = lastPlayerTurnMember.getEffectiveName() + " **is about to heal himself!**";
            }
        }else{
           actionString = "";
        }


        //loop for the states
        if(extra_turn == 4){
            Database.updateDuelExtraTurn(messageid,"-");
        }else {
            Database.updateDuelExtraTurn(messageid, "+");
        }



        EmbedBuilder mb = null;
        if(playerCurrentHealth2 <= 0 && playerCurrentHealth1 <= 0) {
            killedDeveloper(idMember1,idMember2,curr_turn);
            killedDeveloper(idMember2,idMember1,curr_turn);
            killedPlayer(idMember1,playerMaxHealth2,playerDamage1);
            killedPlayer(idMember2,playerMaxHealth1,playerDamage2);
            mb = createEmbed.methode("Duel",initHealthBars.initDescriptionPvp(idMember1,idMember2, playerCurrentHealth1, playerMaxHealth1, playerCurrentHealth2, playerMaxHealth2, player1HealthText, player2HealthText,event) + "\n\n It was a draw! Nobody won! \n\n\n**React below** to choose your next **action**!\n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null);
            Database.deleteDuelRelation(messageid);
        }else if(playerCurrentHealth1 <= 0){
            killedDeveloper(idMember2,idMember1,curr_turn);

            killedPlayer(idMember2,playerMaxHealth1,playerDamage2);
            HealthAchievement(playerMaxHealth2,playerCurrentHealth2,idMember2);
            mb = createEmbed.methode("Duel",initHealthBars.initDescriptionPvp(idMember1,idMember2, playerCurrentHealth1, playerMaxHealth1, playerCurrentHealth2, playerMaxHealth2, player1HealthText, player2HealthText,event) + "\n\n "+ player2.getAsMention() +" won the duel! \n\n\n**React below** to choose your next **action**!\n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null);
            Database.deleteDuelRelation(messageid);
        }else if(playerCurrentHealth2 <= 0){
            killedDeveloper(idMember1,idMember2,curr_turn);
            killedPlayer(idMember1,playerMaxHealth2,playerDamage1);
            HealthAchievement(playerMaxHealth1,playerCurrentHealth1,idMember1);
            mb = createEmbed.methode("Duel",initHealthBars.initDescriptionPvp(idMember1,idMember2, playerCurrentHealth1, playerMaxHealth1, playerCurrentHealth2, playerMaxHealth2, player1HealthText, player2HealthText,event) + "\n\n "+ player1.getAsMention() +" won the duel! \n\n\n**React below** to choose your next **action**!\n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null);
            Database.deleteDuelRelation(messageid);
        }else {
            //Update Health and turn
            Database.updateDuelHealth(messageid, playerCurrentHealth1, 1);
            Database.updateDuelHealth(messageid, playerCurrentHealth2, 2);
            Database.updateDuelTurn(messageid, curr_turn);

            mb = createEmbed.methode("Duel",playerReactString + "\n" + initHealthBars.initDescriptionPvp(idMember1,idMember2, playerCurrentHealth1, playerMaxHealth1, playerCurrentHealth2, playerMaxHealth2, player1HealthText, player2HealthText,event) + "\n\n "+ actionString +" \n\n**" + player1.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus1 + "**x \n**" + player2.getEffectiveName() + "'s** Estus Flakon: **" + playerEstus2 + "**x \n\n**React below** to choose your next **action**!\n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, null);
        }
        dodgeAchievement(idMember1,dodgeCounter1);
        dodgeAchievement(idMember2,dodgeCounter2);
        return mb;
    }

    private static void HealthAchievement(int playerMaxHealth, int playerCurrentHealth,String idMember){
            int healthleft = (int) (10 * playerMaxHealth) / 100;

            if (playerCurrentHealth <= healthleft) {
                if (Database.completeAchievement(idMember, 18)) {
                    rewardUser.methode(idMember, "title", 9);
                }
            }
    }

    private static void killedDeveloper(String idMember, String idMember2,Integer curr_turn){
        if(curr_turn.equals(1)){
            if (idMember.equals("321649314382348288")) {

                if (Database.completeAchievement(idMember2, 10)) {
                    rewardUser.methode(idMember2, "item", 23);
                }
            }
        }else{
            if (idMember2.equals("321649314382348288")) {

                if (Database.completeAchievement(idMember, 10)) {
                    rewardUser.methode(idMember, "item", 23);
                }
            }
        }
    }

    public static void killedPlayer(String idMember,Integer maxHealth,Integer damage){
            if (Database.completeAchievement(idMember, 6)) {
                rewardUser.methode(idMember, "title", 2);
            }
            Database.giveKill(idMember);

            if(damage >= maxHealth){
                Database.completeAchievement(idMember,19);
            }

    }

    public static void dodgeAchievement(String memberID, int dodgeCounter){
        if(dodgeCounter >= 7){
            if (Database.completeAchievement(memberID, 6)) {
                rewardUser.methode(memberID, "title", 1);
            }
        }
    }

    public static void updateDuelMessageTimeout(String userIDWinner, String channelID, String messageid){
    try {
        TextChannel channel = Main.jda.getTextChannelById(channelID);
        Guild guild = channel.getGuild();

        channel.retrieveMessageById(messageid).queue(msg -> {
            guild.retrieveMemberById(userIDWinner).queue(winner -> {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Duel");
                eb.setDescription(winner.getAsMention() + " won due to their opponent not reacting for one minute.");
                eb.setColor(Color.red);
                msg.editMessage(eb.build()).queue();
                try {
                    msg.clearReactions().queue();
                }catch(InsufficientPermissionException in){

                }
            });
        });

        Database.deleteDuelRelation(messageid);
        }catch(InsufficientPermissionException e ){

        }
    }
}
