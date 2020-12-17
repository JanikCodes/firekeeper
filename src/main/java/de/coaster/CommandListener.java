package de.coaster;

import de.commands.user.*;
import de.objects.Clan;
import de.utilities.canUpgrade;
import de.utilities.createEmbed;
import de.utilities.rewardUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.util.List;
import java.util.Random;


public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        try{
            if (event.isFromType(ChannelType.TEXT)) {
                String serverID = event.getGuild().getId();
                String prefix = Database.getPrefix(serverID);
                TextChannel currchat;

                //LEVEL SYSTEM
                if (!event.getAuthor().isBot()) {
                    String memberID = event.getMember().getId();

                    long time = System.currentTimeMillis();

                    int lastmess = Database.getLastMessage(memberID);
                    int newtime = Math.toIntExact((time / 1000));

                    if ((time / 1000) - lastmess > 30) {
                        Random rand = new Random();
                        int randomNum = rand.nextInt((2 - 1) + 1);
                        int currXP = 0;
                        if (randomNum == 1) {
                            currXP = Database.giveXP(memberID, 20);
                        } else {
                            currXP = Database.giveXP(memberID, 30);
                        }

                        int currLevel = Database.getLevel(memberID);
                        double xpToNextLevel = (currLevel * (currLevel * 15) + 100);
                        if (currXP >= xpToNextLevel) {
                            Database.giveXP(memberID, -(currLevel * (currLevel * 15)) - 100);
                            Database.giveLevel(memberID);
                            Database.giveSouls(memberID, currLevel * 1100);

                            if (Database.getNotificationState(serverID) == 1) {
                                String channelID = Database.getNotificationChannel(serverID);

                                if (!channelID.equals("empty")) {
                                    if (event.getGuild().getTextChannelById(channelID) == null) {
                                        currchat = event.getTextChannel();
                                    } else {
                                        currchat = event.getGuild().getTextChannelById(channelID);
                                    }
                                } else {
                                    currchat = event.getTextChannel();
                                }

                                if(event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE,Permission.MESSAGE_MENTION_EVERYONE)){
                                    currchat.sendMessage(event.getMember().getAsMention()).queue();
                                    EmbedBuilder msg = createEmbed.methode(event.getMember().getEffectiveName(), "You've gained **1 Level**! <:lv:761544969546629170> \n you are now level " + (currLevel + 1) + ".\n and you've recieved **" + (currLevel * 1100) + " souls!**", Color.orange, "Admins can disable this notification by typing " + prefix + "levelNotification false", null,null);
                                    currchat.sendMessage(msg.build()).queue();
                                }
                            }
                        }
                        Database.updateLastMessage(memberID, newtime);
                    }
                }
            }
        } catch (InsufficientPermissionException exception) {
        }
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        try{
            Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
            TextChannel currchat = event.getChannel();

            if (event.getReactionEmote().getName().equals("✅") && (!event.getUser().isBot())) {
                String memberID = event.getMember().getId();

                if(Database.findDuelMessageID(event.getMessageId())){
                    //It's a pvp duel
                    String membID = Database.getOtherPlayer(message.getId());
                    if(!Database.getPlayerInDuel(memberID,membID)) {
                        if(Database.isAllowedToAcceptDuel(memberID,event.getMessageId())) {
                            List<MessageEmbed> lmess = message.getEmbeds();
                            MessageEmbed emb = lmess.get(0);
                            message.editMessage(createEmbed.methode(emb.getTitle(), emb.getDescription(), Color.green, "This duel was accepted", null, null).build()).complete();
                            duel.methode(memberID, membID, event, currchat);
                            Database.deletePvpRelation(event.getMessageId());
                        }
                    }else{
                        List<MessageEmbed> lmess = message.getEmbeds();
                        MessageEmbed emb = lmess.get(0);
                        message.editMessage(createEmbed.methode(emb.getTitle(), emb.getDescription(), Color.red, "A player is already in a duel!", null, null).build()).complete();
                        Database.deletePvpRelation(event.getMessageId());
                    }

                }else if(Database.findSellingMessageID(event.getMessageId(),memberID)){
                    //It's a selling message
                    List<MessageEmbed> lmess = message.getEmbeds();
                    MessageEmbed emb = lmess.get(0);
                    message.editMessage(createEmbed.methode(emb.getTitle(),"Many deal…many thanks! Gah hah! \n You? Go home? \n Umm… Thanks!",Color.green,"You've sold your items for " + Database.getTotalItemValue(memberID) + " souls!",null,"https://cdn.discordapp.com/attachments/773175900178743297/781193058095857704/gavlanframe.jpg").build()).complete();
                    Database.giveSouls(memberID,Database.getTotalItemValue(memberID));
                    Database.clearInventory(memberID);
                    Database.deleteSellingRelation(event.getMessageId());
                }else if(!Database.findUpgradeMessageID(event.getMessageId(),memberID).equals("error")){
                    //It's a upgrade item message
                    event.getReaction().removeReaction(event.getUser()).queue();
                    upgradeItem(message,memberID,Database.findUpgradeMessageID(event.getMessageId(),memberID));
                }

            } else if (event.getReactionEmote().getName().equals("❌") && (!event.getUser().isBot())) {
                String memberID = event.getMember().getId();

                if(Database.findDuelMessageID(event.getMessageId())){
                    //It's a duel message
                    if(Database.isAllowedToAcceptDuel(memberID, memberID)) {
                        List<MessageEmbed> lmess = message.getEmbeds();
                        MessageEmbed emb = lmess.get(0);
                        message.editMessage(createEmbed.methode(emb.getTitle(), emb.getDescription(), Color.red, "This duel was declined", null, null).build()).complete();
                        Database.deletePvpRelation(event.getMessageId());
                    }
                }else if(Database.findSellingMessageID(event.getMessageId(),memberID)){
                    //It's a selling message
                    List<MessageEmbed> lmess = message.getEmbeds();
                    MessageEmbed emb = lmess.get(0);
                    message.editMessage(createEmbed.methode(emb.getTitle(),"Umm… \n You? Go home? \n . . .",Color.green,"You have left",null,null).build()).complete();
                    Database.deleteSellingRelation(event.getMessageId());
                }else if(!Database.findUpgradeMessageID(event.getMessageId(),event.getMember().getId()).equals("error")){
                    //It's a upgrade item message
                    List<MessageEmbed> lmess = message.getEmbeds();
                    MessageEmbed emb = lmess.get(0);
                    Database.deleteUpgradeItemRelation(message.getId());
                    message.editMessage(createEmbed.methode(emb.getTitle(),emb.getDescription(),Color.red,"Canceled the upgrade process.",null,null).build()).queue();
                }


            } else if(event.getReactionEmote().getName().equals("▶️") && (!event.getUser().isBot())){
                String memberID = event.getMember().getId();
                if(Database.findAchievementMessageID(event.getMessageId(),memberID)){
                    //It's a achievement message
                    message.editMessage(achievements.editAchievementPage(event.getMember(),Database.getAchievementPage(memberID,message.getId()) + 1,message.getId(),"forward").build()).complete();
                    event.getReaction().removeReaction(event.getUser()).queue();
                }else if(Database.findInventoryMessageID(event.getMessageId(),memberID)){
                    //It's a Inventory message
                    message.editMessage(inventory.editInventoryPage(event.getMember(),Database.getInventoryPage(memberID,message.getId()) + 1 ,message.getId(),"forward").build()).complete();
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            } else if(event.getReactionEmote().getName().equals("◀️") && (!event.getUser().isBot())){
                String memberID = event.getMember().getId();
                if(Database.findAchievementMessageID(event.getMessageId(),memberID)){
                    //It's a achievement message
                    message.editMessage(achievements.editAchievementPage(event.getMember(),Database.getAchievementPage(memberID,message.getId()) - 1,message.getId(),"backwards").build()).complete();
                    event.getReaction().removeReaction(event.getUser()).queue();
                }else if(Database.findInventoryMessageID(event.getMessageId(),memberID)){
                    //It's a Inventory message
                    message.editMessage(inventory.editInventoryPage(event.getMember(), Database.getInventoryPage(memberID, message.getId()) - 1, message.getId(), "backwards").build()).complete();
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }

            if(Database.getBossFightRelation(event.getMessageId(),event.getMember().getId())) {
                if (event.getReactionEmote().getName().equals("⚔️") && (!event.getUser().isBot())) {
                    String memberID = event.getMember().getId();
                    if (Database.findBossMessageID(message.getId(), memberID)) {
                        //It's a boss fight damage func
                        event.getReaction().removeReaction(event.getUser()).queue();
                        message.editMessage(fightBoss.editBossMessage(memberID, message.getId(), "damage", event).build()).queue();
                    }
                }
            }

            if(Database.getDuelRelation(message.getId())) {
                if (event.getReactionEmote().getName().equals("⚔️") && (!event.getUser().isBot())) {
                    String memberID = event.getMember().getId();
                    int turn = Database.getDuelTurn(message.getId());
                    //Is player allowed to do an action
                    int time = Math.toIntExact((System.currentTimeMillis() / 1000));
                    Database.updateDuelTime(event.getUserId(), time);
                    if(Database.getDuelPlayerTurn(message.getId(),turn).equals(memberID)){
                        message.editMessage(duel.editDuelMessage(message.getId(),"damage",event).build()).queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                    }
                }
            }


            if(event.getReactionEmote().isEmote() && (!event.getUser().isBot())) {
                String memberID = event.getMember().getId();

                if (Database.getUpgradeRelation(event.getMessageId(), memberID)) {
                    if (event.getReactionEmote().getId().equals("761547258205437982") && (!event.getUser().isBot())) {
                        upgradeSkill("strength", message, event);
                    } else if (event.getReactionEmote().getId().equals("761546853279203328") && (!event.getUser().isBot())) {
                        upgradeSkill("dexterity", message, event);
                    } else if (event.getReactionEmote().getId().equals("761544992648855564") && (!event.getUser().isBot())) {
                        upgradeSkill("vitality", message, event);
                    } else if (event.getReactionEmote().getId().equals("761546824703410207") && (!event.getUser().isBot())) {
                        upgradeSkill("intelligence", message, event);
                    } else if (event.getReactionEmote().getId().equals("761546973115056128") && (!event.getUser().isBot())) {
                        upgradeSkill("resistance", message, event);
                    } else if (event.getReactionEmote().getId().equals("761546791891894282") && (!event.getUser().isBot())) {
                        upgradeSkill("faith", message, event);
                    } else if (event.getReactionEmote().getId().equals("761544969546629170") && (!event.getUser().isBot())) {
                        upgradeSkill("level",message, event);
                    } else {
                        return;
                    }
                }
                if(Database.getBossFightRelation(event.getMessageId(),memberID)) {
                    if (event.getReactionEmote().getId().equals("784185738681516053") && (!event.getUser().isBot())) {
                        if (Database.findBossMessageID(message.getId(), memberID)) {
                            //It's a boss fight heal func
                            event.getReaction().removeReaction(event.getUser()).queue();
                            message.editMessage(fightBoss.editBossMessage(memberID, message.getId(), "heal", event).build()).queue();
                        }

                    } else if (event.getReactionEmote().getId().equals("784184343416537141") && (!event.getUser().isBot())) {
                        if (Database.findBossMessageID(message.getId(), memberID)) {
                            //It's a boss fight block func
                            event.getReaction().removeReaction(event.getUser()).queue();
                            message.editMessage(fightBoss.editBossMessage(memberID, message.getId(), "block", event).build()).queue();
                        }
                    } else if (event.getReactionEmote().getId().equals("784349634163507240") && (!event.getUser().isBot())) {
                        if (Database.findBossMessageID(message.getId(), memberID)) {
                            //It's a boss fight block func
                            event.getReaction().removeReaction(event.getUser()).queue();
                            message.editMessage(fightBoss.editBossMessage(memberID, message.getId(), "dodge", event).build()).queue();
                        }
                    }
                }

                if(Database.getDuelRelation(message.getId())) {
                    if (event.getReactionEmote().getId().equals("784184343416537141") && (!event.getUser().isBot())) {
                        int turn = Database.getDuelTurn(message.getId());
                        //Is player allowed to do an action

                        if(Database.getDuelPlayerTurn(message.getId(),turn).equals(memberID)){
                            message.editMessage(duel.editDuelMessage(message.getId(),"block",event).build()).queue();
                            event.getReaction().removeReaction(event.getUser()).queue();
                        }
                    }

                    if (event.getReactionEmote().getId().equals("784185738681516053") && (!event.getUser().isBot())) {
                        int turn = Database.getDuelTurn(message.getId());
                        //Is player allowed to do an action

                        if(Database.getDuelPlayerTurn(message.getId(),turn).equals(memberID)){
                            message.editMessage(duel.editDuelMessage(message.getId(),"heal",event).build()).queue();
                            event.getReaction().removeReaction(event.getUser()).queue();
                        }
                    }
                    if (event.getReactionEmote().getId().equals("784349634163507240") && (!event.getUser().isBot())) {
                        int turn = Database.getDuelTurn(message.getId());
                        //Is player allowed to do an action

                        if(Database.getDuelPlayerTurn(message.getId(),turn).equals(memberID)){
                            message.editMessage(duel.editDuelMessage(message.getId(),"dodge",event).build()).queue();
                            event.getReaction().removeReaction(event.getUser()).queue();
                        }
                    }
                }
            }
        } catch (InsufficientPermissionException exception) {
        }catch(ErrorResponseException er){
            System.out.println("Message was deleted EXCEPTION");
        }
    }

    private void upgradeItem(Message message, String memberID, String type) {
        try{
            List<MessageEmbed> lmess = message.getEmbeds();
            MessageEmbed emb = lmess.get(0);
            Integer itemID = Database.getItemIDFromRelation(message.getId());
            Integer soulamount = Database.getStatistic("souls", memberID);
            String itemName = Database.getItemName(itemID);
            Integer itemStats = Database.getItemBonus(itemID,memberID);
            Integer itemLevel = Database.getItemLevel(itemID,memberID);
            Integer itemCost = 25000 + (30000 * itemLevel);
            Integer displayitemCost = 25000 + (30000 * (itemLevel + 1));

            if(itemLevel == 9){
                if(Database.completeAchievement(memberID,16)){
                    rewardUser.methode(memberID, "souls", 10000);
                }
            }

            if(itemLevel >= 10){
                message.editMessage(createEmbed.methode("Forge", emb.getDescription(), Color.red, "Item cannot be upgraded anymore!", null, null).build()).complete();
                Database.deleteUpgradeItemRelation(message.getId());
            }else{
                if(soulamount >= itemCost){
                    //Upgraded
                    message.editMessage(createEmbed.methode("Forge","Do you want to upgrade the " + type + "? \n Weapon: **" + itemName + " +" + (itemLevel + 1) +"** \n Current damage: **" + itemStats + "** \n After upgrade: **" + (itemStats + 5) + "** \n Upgrade cost: **" + displayitemCost + " souls**" ,Color.green,"successfully upgraded!",null,null).build()).complete();
                    Database.upgradeItem(itemID,memberID);
                    Database.reduceSouls(memberID, itemCost);
                }else{
                    //Not enough
                    message.editMessage(createEmbed.methode("Forge",emb.getDescription() ,Color.red,"Not enough souls!",null,null).build()).complete();
                }
            }


        }catch (InsufficientPermissionException exception) {
        }
    }

    private static void upgradeSkill(String skillname, Message message, GuildMessageReactionAddEvent event){
        String memberID = event.getMember().getId();
        try{
            event.getReaction().removeReaction(event.getUser()).complete();
            List<MessageEmbed> lmess = message.getEmbeds();
            MessageEmbed emb = lmess.get(0);
            Integer soulamount = Database.getStatistic("souls", memberID);
            Integer value = Database.getStatistic(skillname, memberID);
            long endprice = 0;
            if(value >= 50){
                endprice = Math.round((value * 125) * 2);
            }else{
                endprice = (value * 125);
            }
            if(skillname.equals("level")){
                endprice = (value * 1100) * 3;
            }

            int strength = Database.getStatistic("strength", memberID);
            int dexterity = Database.getStatistic("dexterity", memberID);
            int vitality = Database.getStatistic("vitality", memberID);
            int intelligence = Database.getStatistic("intelligence", memberID);
            int resistance = Database.getStatistic("resistance", memberID);
            int faith = Database.getStatistic("faith", memberID);
            int rank = Database.getStatistic("level", memberID);

            if(value == 100 && !skillname.equals("level")){
                //Is max level
                message.editMessage(getProfil.createProfilEmbed(strength, dexterity, vitality, intelligence, resistance, faith,rank, event.getMember(), "You're already max level with the " + skillname + " skill.", Color.red,soulamount).build()).complete();
            }else {
                if (canUpgrade.methode(endprice, soulamount)) {
                    //has enough souls to upgrade!
                    Database.reduceSouls(event.getMember().getId(), endprice);
                    Database.upgradeSkill(event.getMember().getId(), skillname, 1);
                    soulamount = Database.getStatistic("souls", memberID);

                    if (skillname == "strength") {
                        strength = Database.getStatistic("strength", memberID);
                    } else if (skillname == "dexterity") {
                        dexterity = Database.getStatistic("dexterity", memberID);
                    } else if (skillname == "vitality") {
                        vitality = Database.getStatistic("vitality", memberID);
                    } else if (skillname == "intelligence") {
                        intelligence = Database.getStatistic("intelligence", memberID);
                    } else if (skillname == "resistance") {
                        resistance = Database.getStatistic("resistance", memberID);
                    } else if (skillname == "faith") {
                        faith = Database.getStatistic("faith", memberID);
                    } else if (skillname == "level") {
                        rank = Database.getStatistic("level", memberID);
                    }

                    message.editMessage(getProfil.createProfilEmbed(strength, dexterity, vitality, intelligence, resistance, faith,rank, event.getMember(), "Sucessfully upgraded your " + skillname + " skill!", Color.green,soulamount).build()).complete();
                } else {
                    message.editMessage(getProfil.createProfilEmbed(strength, dexterity, vitality, intelligence, resistance, faith,rank, event.getMember(), "You don't have enough souls! You need " + (endprice - soulamount) + " more souls!", Color.red,soulamount).build()).complete();
                }
            }
        } catch (InsufficientPermissionException exception) {
        }
    }
}
