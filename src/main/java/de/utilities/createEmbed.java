package de.utilities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class createEmbed {

    public static EmbedBuilder methode(String title, String desc, Color color, String footer, Member member, String url) {
        EmbedBuilder messageStyle = new EmbedBuilder();
        messageStyle.setTitle(title);
        messageStyle.setDescription(desc);
        messageStyle.setColor(color);

        if (member != null) {
            messageStyle.setThumbnail(member.getUser().getAvatarUrl());
        }
        if (url != null) {
            messageStyle.setThumbnail(url);
        }
        if (footer != null) {
            messageStyle.setFooter(footer);
        }
        return messageStyle;
    }
}
