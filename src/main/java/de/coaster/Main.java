package de.coaster;
import javax.security.auth.login.LoginException;

import com.sun.net.httpserver.HttpServer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {


    public static boolean test = false;
    public static String displayVersion = "2.6.3";

    public static String defaultprefix = "!";
    public static String botAddLink = "https://discord.com/api/oauth2/authorize?client_id=760993270133555231&permissions=1074097216&scope=bot";
    public static int bossTime = 1800;
    public static int clearTime = 1800;
    public static int voteClearTime = 900;
    public static int voteBossTime = 900;

    public static final int duelSurrenderTime = 60;

    public static int bossTimeWon = 3600;
    public static int voteBossTimeWon = 2700;

    public static int patreonTime = 900;
    public static int patreonTimeBossWon = 1800;
    public static double tier1SoulMulti = 1.5;
    public static int tier2SoulMulti = 2;

    public static String tier1 = "782558977459617793";
    public static String tier2 = "782565550642954261";
    public static String patreonlink = "https://www.patreon.com/firekeeperbot";

    public static JDA jda;

    private static String realVersion = "NzYwOTkzMjcwMTMzNTU1MjMx.X3UINg.s8k6-zhT13HDBRFpCFSc4EFPTvc";
    private static String testVersion = "NzYzNDQ1ODk3NDc1MzkxNDk4.X330Zg.mwrGhbZipvaBF-X2XOmQoS5JIww";
    private static String version = null;

    public static void main(String[] args) throws IOException {

        if(test){
            version = testVersion;
        }else{
            version = realVersion;
        }

        try {
            jda = JDABuilder.createDefault(version)
                    .setActivity(Activity.of(Activity.ActivityType.WATCHING,"the servers"))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new CommandListener())
                    .addEventListeners(new GuildMessageReceived())
                    .addEventListeners(new GuildsListener())
                    .addEventListeners(new OnReadyListener())
                    .build();


        } catch (LoginException e) {
            System.out.println("Problem with JDA Builder");
            e.printStackTrace();
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8101), 0); // create a Httpserver listening on port 8000
        server.createContext("/dblwebhook", new Reciever());  // creates a handler for when a requests comes into https://yourserverip.com/dblwebhook
        server.setExecutor(null); // creates a default executor
        server.start(); // Starts your httpserver
        System.out.println("HttpServer started");

    }



}
