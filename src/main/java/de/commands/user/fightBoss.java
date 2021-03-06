package de.commands.user;

import de.coaster.Database;
import de.coaster.Main;
import de.utilities.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.util.ArrayList;

public class fightBoss {

    public static void methode(GuildMessageReceivedEvent event, TextChannel currchat) {
        long time = System.currentTimeMillis();
        int newtime = Math.toIntExact((time / 1000));
        int lastclear = Database.getStatistic("last_boss", event.getMember().getId());
        boolean voted = hasVoted.methode(time, event);

        if ((time / 1000) - lastclear > getTime.getBossTime(time, event)) {
            String memberID = event.getMember().getId();
            int bossID = Database.getPlayerBoss(memberID);
            String bossName = Database.getBossName(bossID);
            String bossURL = Database.getBossImage(bossID);

            Database.updateBossTime(newtime, memberID);
            Database.updateBossWon(memberID,0);

            //Get player statistics
            int vitality = Database.getStatistic("Vitality", memberID);
            int resistance = Database.getStatistic("Resistance", memberID);
            int faith = Database.getStatistic("Faith", memberID);

            int estus_amount = duel.getEstus(faith);

            int playerMaxHealth = 150 + ((vitality * 2) + resistance) * 8;
            int bossMaxHealth = Database.getBossHealth(bossID);
            ArrayList<String> bossAttacks = Database.getBossAttacks(bossID,1,0);
            int randomAttack = getRandomNumberInRange.methode(0,bossAttacks.size() - 1);
            String attackName = bossAttacks.get(randomAttack);
            int attackID = Database.getBossAttackID(attackName);

            final String[] messageID = {null};

            int finalEstus_amount = estus_amount;
            currchat.sendMessage(createEmbed.methode(bossName, "As you leave the fog you see **" + bossName + "**! \n" + initHealthBars.methode(event.getMember(), playerMaxHealth, playerMaxHealth, bossMaxHealth, bossMaxHealth, "", "") + "\n \n " + attackName + "\n \n**React below** to choose your next **action**! \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, bossURL).build()).queue(message -> {
                message.addReaction("⚔️").queue();
                message.addReaction("sh:784184343416537141").queue();
                message.addReaction("hea:784185738681516053").queue();
                message.addReaction("ddg:784349634163507240").queue();
                messageID[0] = message.getId();
                Database.createBossRelation(messageID[0],memberID,bossID,attackID,playerMaxHealth,bossMaxHealth, finalEstus_amount);
            });


        } else {
            //Can't do boss
            String endTime = calculateTime.methode(getTime.getBossTime(time, event) - ((time / 1000) - lastclear));
            if (voted) {
                currchat.sendMessage(createEmbed.methode("**ERROR**", "You've already fought a boss recently! \n You'll need to wait **" + endTime + "** to fight a new boss!", Color.orange, null, null, null).build()).queue();
            } else {
                currchat.sendMessage(createEmbed.methode("**ERROR**", "You've already fought a boss recently! \n You'll need to wait **" + endTime + "** to fight a new boss! \n ❕*Vote the bot on* [top.gg](https://top.gg/bot/760993270133555231/vote) *for reduced cooldown!*", Color.orange, null, null, null).build()).queue();
            }
        }

    }


    public static EmbedBuilder editBossMessage(String memberID, String messageid, String player_choice, GuildMessageReactionAddEvent event) {
        int bossID = Database.getBossId(messageid,memberID);
        String bossURL = Database.getBossImage(bossID);
        EmbedBuilder ms = null;

        String phase_type = Database.getBossPhaseType(bossID);

        //Get player statistics
        int vitality = Database.getStatistic("Vitality", memberID);
        int strength = Database.getStatistic("Strength", memberID);
        int dexterity = Database.getStatistic("Dexterity", memberID);
        int resistance = Database.getStatistic("Resistance", memberID);
        int faith = Database.getStatistic("Faith", memberID);
        int weaponDamage = Database.getWeaponDamage(memberID);
        int playerArmor = Database.getArmorBonus(memberID);

        int playerMaxHealth = 150 + ((vitality * 2) + resistance) * 8;
        int playerDamage = 100 + ((strength/2) + (dexterity/2) + weaponDamage) * 2;

        int bossMaxHealth = Database.getBossHealth(bossID);
        String bossName = Database.getBossName(bossID);
        int bossCurrentHealth = Database.getBossCurrentHealth(messageid,memberID);
        int playerCurrentHealth = Database.getPlayerCurrentHealth(messageid,memberID);
        resistance = resistance / 2;

        ArrayList<String> bossAttacks = new ArrayList<>();


        //Get boss phase attacks
        if(phase_type.equals("none")){
            bossAttacks = Database.getBossAttacks(bossID,1,0);
        }else if(phase_type.equals("half")){
            if(bossCurrentHealth <= (bossMaxHealth / 2)) {
                bossAttacks = Database.getBossAttacks(bossID, 2,0);
                if (Database.completeAchievement(memberID, 1)) {
                    rewardUser.methode(memberID, "souls", 5000);
                }
            }else{
                bossAttacks = Database.getBossAttacks(bossID,1,0);
            }
        }else if(phase_type.equals("full")){
            if(Database.getCurrentPhase(messageid,memberID) == 2){
                bossAttacks = Database.getBossAttacks(bossID,2,0);
            }else{
                bossAttacks = Database.getBossAttacks(bossID,1,0);
            }
        }

        int last_attack = Database.getBossLastAttack(messageid,memberID);
        String last_attack_type = Database.getBossAttackType(last_attack);
        int last_attack_value = Database.getBossAttackValue(last_attack);

        int randomAttack = getRandomNumberInRange.methode(0,bossAttacks.size() - 1);
        String attackName = bossAttacks.get(randomAttack);
        int attackID = Database.getBossAttackID(attackName);

        Database.updateBossLastAttack(messageid,memberID,attackID);

        String playerTextHealth = "";
        String bossTextHealth = "";

        //PLAYER CHOOSE TO ATTACK
        if(player_choice.equals("damage")){
            if(last_attack_type.equals("damage")){
                if((playerCurrentHealth - (last_attack_value - playerArmor)) <= 0){
                    playerCurrentHealth = 0;
                }else {
                    playerCurrentHealth = playerCurrentHealth - (last_attack_value - playerArmor);
                }

                if((bossCurrentHealth - playerDamage) <= 0){
                    bossCurrentHealth = 0;
                }else{
                    bossCurrentHealth = bossCurrentHealth - playerDamage;
                }

                bossTextHealth = "** -" + playerDamage + "**";
                playerTextHealth = "** -" + (last_attack_value - playerArmor)+ "**";

            }else if(last_attack_type.equals("block")){
                if(((bossCurrentHealth - (playerDamage / 2))) <= 0){
                    bossCurrentHealth = 0;
                }else{
                    bossCurrentHealth = bossCurrentHealth - (playerDamage / 2);
                }
                bossTextHealth = "** -" + (playerDamage / 2) + "**, blocked **" + (playerDamage / 2) + "** damage!";
                playerTextHealth = "";

            }else if(last_attack_type.equals("free")){
                if((bossCurrentHealth - playerDamage) <= 0){
                    bossCurrentHealth = 0;
                }else{
                    bossCurrentHealth = bossCurrentHealth - playerDamage;
                }

                bossTextHealth = "** -" + playerDamage + "**";
                playerTextHealth = "";

            }else if(last_attack_type.equals("cant")){
                bossTextHealth = "";
                playerTextHealth = " **missed!**";

            }else if(last_attack_type.equals("weakness")){
                if((bossCurrentHealth - (playerDamage + last_attack_value)) <= 0){
                    bossCurrentHealth = 0;
                }else{
                    bossCurrentHealth = bossCurrentHealth - (playerDamage + last_attack_value);
                }

                bossTextHealth = "** -" + (playerDamage + last_attack_value) + " ❗️**";
                playerTextHealth = "";
            }

            //PLAYER CHOOSE TO BLOCK
        }else if(player_choice.equals("block")){
            if(last_attack_type.equals("damage")){
                if((playerCurrentHealth - ((last_attack_value / 2) - playerArmor - resistance)) <= 0){
                    playerCurrentHealth = 0;

                    playerTextHealth = "** -" + (( last_attack_value / 2 ) - playerArmor - resistance) + "**, blocked **" +  last_attack_value / 2 + "** damage!";
                }else if(( (last_attack_value / 2) - playerArmor - resistance) <= 0){
                    //would do below 0 damage
                    playerCurrentHealth = playerCurrentHealth - 25;

                    playerTextHealth = "** -25**, blocked **" +  last_attack_value / 2 + "** damage!";
                }else {
                    playerCurrentHealth = playerCurrentHealth - ((last_attack_value / 2) - playerArmor - resistance);

                    playerTextHealth = "** -" + (( last_attack_value / 2 ) - playerArmor - resistance) + "**, blocked **" +  last_attack_value / 2 + "** damage!";
                }
                bossTextHealth = "";

            }else if(last_attack_type.equals("block")){
                bossTextHealth = "";
                playerTextHealth = "";

            }else if(last_attack_type.equals("free")){
                bossTextHealth = "";
                playerTextHealth = "";

            }else if(last_attack_type.equals("cant")){
                bossTextHealth = "";
                playerTextHealth = "";

            }else if(last_attack_type.equals("weakness")){
                bossTextHealth = "";
                playerTextHealth = "";
            }

            //PLAYER CHOOSE TO HEAL
        }else if(player_choice.equals("heal")){

            if(last_attack_type.equals("damage")){
                int healAmount = 550;
                //check if overhealed
                if((Database.getEstusCount(messageid,memberID)) >= 1) {
                    if (healAmount + playerCurrentHealth > playerMaxHealth) {
                        healAmount = playerMaxHealth - playerCurrentHealth;
                    }
                    Database.removeEstusFromBossFight(messageid, memberID);

                    if ((playerCurrentHealth - last_attack_value) + healAmount <= 0) {
                        playerCurrentHealth = 0;
                    } else {
                        playerCurrentHealth = (playerCurrentHealth - last_attack_value) + healAmount;
                    }

                    bossTextHealth = "";
                    playerTextHealth = "** -" + last_attack_value + "**, healed **" + healAmount + "** health";
                }else{
                    if (playerCurrentHealth - last_attack_value <= 0) {
                        playerCurrentHealth = 0;
                    } else {
                        playerCurrentHealth = playerCurrentHealth - last_attack_value;
                    }

                    bossTextHealth = "";
                    playerTextHealth = "** -" + last_attack_value + "**, no more healing left!";
                }


            }else if(last_attack_type.equals("block")){
                int healAmount = 550;
                //check if overhealed
                if((Database.getEstusCount(messageid,memberID)) >= 1) {
                    if (healAmount + playerCurrentHealth > playerMaxHealth) {
                        healAmount = playerMaxHealth - playerCurrentHealth;
                    }

                    Database.removeEstusFromBossFight(messageid, memberID);

                    playerCurrentHealth = playerCurrentHealth +  healAmount;

                    bossTextHealth = "";
                    playerTextHealth = "** +" + healAmount + "** health";
                }else{
                    bossTextHealth = "";
                    playerTextHealth = " no more healing left!";
                }


            }else if(last_attack_type.equals("free")){
                int healAmount = 550;
                //check if overhealed
                if((Database.getEstusCount(messageid,memberID)) >= 1) {
                    if (healAmount + playerCurrentHealth > playerMaxHealth) {
                        healAmount = playerMaxHealth - playerCurrentHealth;
                    }

                    Database.removeEstusFromBossFight(messageid, memberID);

                    playerCurrentHealth = playerCurrentHealth +  healAmount;

                    bossTextHealth = "";
                    playerTextHealth = "** +" + healAmount + "** health";
                }else{
                    bossTextHealth = "";
                    playerTextHealth = " no more healing left!";
                }

            }else if(last_attack_type.equals("cant")){
                int healAmount = 550;
                //check if overhealed
                if((Database.getEstusCount(messageid,memberID)) >= 1) {
                    if (healAmount + playerCurrentHealth > playerMaxHealth) {
                        healAmount = playerMaxHealth - playerCurrentHealth;
                    }

                    Database.removeEstusFromBossFight(messageid, memberID);

                    playerCurrentHealth = playerCurrentHealth +  healAmount;

                    bossTextHealth = "";
                    playerTextHealth = "** +" + healAmount + "** health";
                }else{
                    bossTextHealth = "";
                    playerTextHealth = " no more healing left!";
                }

            }else if(last_attack_type.equals("weakness")){
                int healAmount = 550;
                //check if overhealed
                if((Database.getEstusCount(messageid,memberID)) >= 1) {
                    if (healAmount + playerCurrentHealth > playerMaxHealth) {
                        healAmount = playerMaxHealth - playerCurrentHealth;
                    }

                    Database.removeEstusFromBossFight(messageid, memberID);

                    playerCurrentHealth = playerCurrentHealth +  healAmount;

                    bossTextHealth = "";
                    playerTextHealth = "** +" + healAmount + "** health";
                }else{
                    bossTextHealth = "";
                    playerTextHealth = " no more healing left!";
                }
            }

            //PLAYER CHOOSE TO DODGE
        }else if(player_choice.equals("dodge")){

            if(last_attack_type.equals("damage")){
                int dodgechance = 25 + (Database.getStatistic("intelligence",memberID) / 2);
                int randomNum = getRandomNumberInRange.methode(1,100);
                //CAN HE DODGE
                if(dodgechance >= randomNum) {
                    playerTextHealth = " **dodged!**";
                }else{
                    if ((playerCurrentHealth - (last_attack_value - playerArmor)) <= 0) {
                        playerCurrentHealth = 0;
                    } else {
                        playerCurrentHealth = playerCurrentHealth - (last_attack_value - playerArmor);
                    }
                    playerTextHealth = "** -" + (last_attack_value - playerArmor)+ "**, couldn't dodge!";

                }

                bossTextHealth = "";

            }else if(last_attack_type.equals("block")){
                bossTextHealth = "";
                playerTextHealth = " nothing to dodge";

            }else if(last_attack_type.equals("free")){
                bossTextHealth = "";
                playerTextHealth = " nothing to dodge";

            }else if(last_attack_type.equals("cant")){
                bossTextHealth = "";
                playerTextHealth = " nothing to dodge";

            }else if(last_attack_type.equals("weakness")){
                bossTextHealth = "";
                playerTextHealth = " nothing to dodge";
            }else if(last_attack_type.equals("none")){
                System.out.println("LOL");
            }
        }

        if(playerCurrentHealth <= 0){
            //Player Lost
            Database.deleteBossFightRelation(memberID);
            ms = createEmbed.methode(bossName, initHealthBars.methode(event.getMember(), playerCurrentHealth, playerMaxHealth, bossCurrentHealth, bossMaxHealth, "", "") + "\n \n " + attackName + "\n \n **YOU DIED**", Color.black, null, null, bossURL);

        }else if(bossCurrentHealth <= 0){

            //Player won
            if(phase_type.equals("full") && Database.getCurrentPhase(messageid,memberID) == 1){
                //Makes boss full life again
                if (Database.completeAchievement(memberID, 1)) {
                    rewardUser.methode(memberID, "souls", 5000);
                }
                Database.updateBossPhase(messageid,memberID,2);
                bossCurrentHealth = bossMaxHealth;
                Database.updateBossFightHealth("playerHealth",playerCurrentHealth,messageid,memberID);
                Database.updateBossFightHealth("bossHealth",bossCurrentHealth,messageid,memberID);
                ms = createEmbed.methode(bossName, initHealthBars.methode(event.getMember(), playerCurrentHealth, playerMaxHealth, bossCurrentHealth, bossMaxHealth, playerTextHealth, bossTextHealth) + "\n \n " + bossName + " **reached his second phase** and " + attackName + "\n \n Estus Flakon: **" + Database.getEstusCount(messageid,memberID) + "**x \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, bossURL);
            }else{
                if (Database.completeAchievement(memberID, 5)) {
                    rewardUser.methode(memberID, "souls", 2000);
                }

                //Give user reward for killing it
                double soulAmount = Database.getBossReward(bossID) ;

                if (findRole.methode(event.getMember(), Main.tier1) != null) {
                    soulAmount = soulAmount * Main.tier1SoulMulti;
                }
                if (findRole.methode(event.getMember(), Main.tier2) != null) {
                    soulAmount = soulAmount * Main.tier2SoulMulti;
                }

                int IntSoulAmount = (int) soulAmount + (Database.getAreaProgress(memberID) * 50);
                Database.updateBossWon(memberID,1);
                Database.giveSouls(memberID, IntSoulAmount);
                Database.giveBossCount(event.getGuild().getId(), 1);
                Database.setNewBoss(memberID,bossID);
                Database.deleteBossFightRelation(memberID);
                ms = createEmbed.methode(bossName, initHealthBars.methode(event.getMember(), playerCurrentHealth, playerMaxHealth, bossCurrentHealth, bossMaxHealth, "", "") + "\n \n " + bossName + " vanished.." + "\n \n**You've defeated " + bossName + "** and collected **" + IntSoulAmount + "** souls!", Color.black, null, null, bossURL);
            }

        }else {
            Database.updateBossFightHealth("playerHealth",playerCurrentHealth,messageid,memberID);
            Database.updateBossFightHealth("bossHealth",bossCurrentHealth,messageid,memberID);
            ms = createEmbed.methode(bossName, initHealthBars.methode(event.getMember(), playerCurrentHealth, playerMaxHealth, bossCurrentHealth, bossMaxHealth, playerTextHealth, bossTextHealth) + "\n \n " + attackName + "\n \n Estus Flakon: **" + Database.getEstusCount(messageid,memberID) + "**x \n \n ⚔ - attack <:sh:784184343416537141> - block \n <:hea:784185738681516053> - heal <:ddg:784349634163507240> - dodge", Color.black, null, null, bossURL);
        }
        return ms;
    }

}
