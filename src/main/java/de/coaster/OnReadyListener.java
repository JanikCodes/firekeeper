package de.coaster;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnReadyListener extends ListenerAdapter {

    public static DiscordBotListAPI api;
    public static boolean run = true;
    private static int status = 0;

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        api = new DiscordBotListAPI.Builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc2MDk5MzI3MDEzMzU1NTIzMSIsImJvdCI6dHJ1ZSwiaWF0IjoxNjA3MTkzMTE4fQ.GmqwVuGDG-904Jhq1HKKXH-YRKiMq7_MwmVDyh6MS4w")
                .botId("760993270133555231")
                .build();

        ExecutorService esSetGuild = Executors.newSingleThreadExecutor();
        esSetGuild.execute(() -> {
            while (run) {
                try {
                    api.setStats(event.getJDA().getGuilds().size());
                    Thread.sleep(5*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!run)
                esSetGuild.shutdown();
        });

        ExecutorService esSetStatus = Executors.newSingleThreadExecutor();
        esSetStatus.execute(() -> {
            while (run) {
                int time = Math.toIntExact((System.currentTimeMillis() / 1000));
                Database.duelCheckTime(time);

                try {
                    Activity a = null;

                    switch (status){
                        case 0:{
                            a = Activity.of(Activity.ActivityType.WATCHING, event.getJDA().getGuilds().size()+" servers");
                            status++;
                            break;
                        } case 1: {
                            a = Activity.of(Activity.ActivityType.WATCHING, "Update 2.6");
                            status = 0;
                            break;
                        }
                    }
                    event.getJDA().getPresence().setActivity(a);

                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!run)
                esSetStatus.shutdown();
        });
    }
}
