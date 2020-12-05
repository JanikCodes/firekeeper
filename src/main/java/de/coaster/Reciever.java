package de.coaster;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.utilities.getRandomNumberInRange;
import de.utilities.rewardUser;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class Reciever implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();

        byte[] buffer = new byte[1024];
        StringBuilder stringBuilder = new StringBuilder();
        int length = 0;

        while ((length = is.read(buffer)) >= 0) {
            stringBuilder.append(new String(Arrays.copyOfRange(buffer, 0, length), "UTF-8"));
        }

        String[] lines = stringBuilder.toString().split(",");

        if(lines[2].contains("upvote") || lines[2].contains("test")){
            String userID = lines[1].replaceAll("\\D+","");
            User u = Main.jda.retrieveUserById(userID).complete();

            Main.jda.getTextChannelById("780388745534636032").sendMessage(u.getName() + "#" + u.getDiscriminator() + " has upvoted the Bot!").queue();

            long time = System.currentTimeMillis();
            int newtime = Math.toIntExact((time / 1000));

            //Give user upvote reward
            Database.upvoteReward(userID);
            Database.updateVoteTime(userID,newtime);
            if(Database.completeAchievement(userID,20)){
                rewardUser.methode(userID, "level", 1);
            }
            //Give user 1 random item
            ArrayList<Integer> items = Database.getAllitems();
            int itemNumber = getRandomNumberInRange.methode(0, items.size() - 1);
            Database.giveItem(userID, items.get(itemNumber));

        }
    }
}