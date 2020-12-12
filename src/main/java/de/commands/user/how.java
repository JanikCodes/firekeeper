package de.commands.user;

import de.utilities.createEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class how {

    public static void methode(TextChannel currchat){
        currchat.sendMessage(createEmbed.methode("**How does this bot work?**", "You get **xp** by typing in the chat and with **xp you'll level up!** \n Level-ups will give you **souls** which then can be used to **upgrade your skills or weapons/armor**! \n \n You'll need" +
                        " a decent amount of **strength, vitality, dexterity, faith** to have a good chance of beating a boss! *I suggest atleast level 25 in all of them* \n\n" +
                        ":white_small_square: **Strength** increases your damage.\n" +
                        ":white_small_square: **Dexterity** increases your damage aswell.\n" +
                        ":white_small_square: **Vitality** increases your health.\n" +
                        ":white_small_square: **Intelligence** increases your **dodge** chance against bosses + you have a higher chance of winning the **special encounters**. \n" +
                        ":white_small_square: **Resistance** reduces incoming damage + increases your maximum health. \n" +
                        ":white_small_square: **Faith** gives your **extra souls** while clearing areas \n **+** Depending on how high your faith skill is you'll have a different amount of **Estus Flakon** to use in Boss fights and duels.\n 100 Faith = 10 Estus Flakon, 30 Faith = 3 Estus Flakon \n" +
                        ":white_small_square: **All stats** increases your total **soul gain**. All stats are universal and the same on every server with this bot on! \n"
                , Color.red, null, null, null).build()).complete();
    }
}
