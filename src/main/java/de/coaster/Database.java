package de.coaster;
import net.dv8tion.jda.api.entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Connection myCon;
    private static Statement myStmnt;
    private static ResultSet myRS;



    private static String user = "root";
    private static String pwd = !Main.test ? "87172000" : "1111";
    private static String url = "jdbc:mysql://localhost:3306/firekeeper?useSSL=false";

    public static void insertIntoDatabase(String serverID){

        try {

            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idServer FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (!myRS.next()) {     //Didnt found the server already in the database
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO server (idServer, prefix, enableNotification, clearedAreas, defeatedBosses, notificationChannel, onlychannel) VALUES(?, ?, ?, ?, ?, ?, ?)");
                prepStmntPersonInsert.setString(1, serverID);
                prepStmntPersonInsert.setString(2, Main.defaultprefix);
                prepStmntPersonInsert.setInt(3, 1);
                prepStmntPersonInsert.setInt(4, 0);
                prepStmntPersonInsert.setInt(5, 0);
                prepStmntPersonInsert.setString(6, "0");
                prepStmntPersonInsert.setString(7, "0");


                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }


    public static String getOnlyChannel(String serverID){
        String channelID = "0";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT onlyChannel FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                channelID = myRS.getString(1);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return channelID;
    }

    public static void changePrefix(String serverID, String newPrefix) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update server set prefix = ? where idServer = ?");
            prepStmntPersonInsert.setString(1, newPrefix);
            prepStmntPersonInsert.setString(2, serverID);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void getUsernames(List<Member> allMembers) {
        try {

            for(int i=0; i<allMembers.size();i++){
                java.sql.PreparedStatement prepStmntPersonInsert;

                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
                prepStmntPersonInsert.setString(1, allMembers.get(i).getId());

                myRS = prepStmntPersonInsert.executeQuery();
                if (myRS.next()) {
                    myCon = DriverManager.getConnection(url, user, pwd);
                    prepStmntPersonInsert = myCon.prepareStatement("update users set username = ? where idUsers = ?");
                    prepStmntPersonInsert.setString(1, allMembers.get(i).getEffectiveName());
                    prepStmntPersonInsert.setString(2, allMembers.get(i).getId());
                    prepStmntPersonInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static String getPrefix(String serverID) {
        String prefix = "!";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT prefix FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                prefix = myRS.getString(1);
            }

        } catch(SQLException e){
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return prefix;
    }

    public static void addMemberToTable(String memberID, String username) {

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                prepStmntPersonInsert.setString(1, memberID);
                prepStmntPersonInsert.setInt(2, 0);
                prepStmntPersonInsert.setInt(3, 1);
                prepStmntPersonInsert.setInt(4, 0);
                //Stats
                prepStmntPersonInsert.setInt(5, 0);
                prepStmntPersonInsert.setInt(6, 1);
                prepStmntPersonInsert.setInt(7, 1);
                prepStmntPersonInsert.setInt(8, 1);
                prepStmntPersonInsert.setInt(9, 1);
                prepStmntPersonInsert.setInt(10, 1);
                prepStmntPersonInsert.setInt(11, 1);
                prepStmntPersonInsert.setInt(12, 0);
                prepStmntPersonInsert.setInt(13, 0);
                prepStmntPersonInsert.setString(14, username);
                prepStmntPersonInsert.setInt(15, 0);
                prepStmntPersonInsert.setInt(16, 0);
                prepStmntPersonInsert.setInt(17, 0);
                prepStmntPersonInsert.setInt(18, 0);
                prepStmntPersonInsert.setInt(19, 0);
                prepStmntPersonInsert.setInt(20, 0);
                prepStmntPersonInsert.setInt(21, 1);
                prepStmntPersonInsert.setInt(22, 0);


                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void addMembersToDatabase(List<Member> allMembers) {
        try {

            for(int i=0; i<allMembers.size();i++){
                java.sql.PreparedStatement prepStmntPersonInsert;

                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
                prepStmntPersonInsert.setString(1, allMembers.get(i).getId());

                myRS = prepStmntPersonInsert.executeQuery();
                if (!myRS.next()) {
                    prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? , ?)");
                    prepStmntPersonInsert.setString(1, allMembers.get(i).getId());
                    prepStmntPersonInsert.setInt(2, 0);
                    prepStmntPersonInsert.setInt(3, 1);
                    prepStmntPersonInsert.setInt(4, 0);
                    //Stats
                    prepStmntPersonInsert.setInt(5, 0);
                    prepStmntPersonInsert.setInt(6, 1);
                    prepStmntPersonInsert.setInt(7, 1);
                    prepStmntPersonInsert.setInt(8, 1);
                    prepStmntPersonInsert.setInt(9, 1);
                    prepStmntPersonInsert.setInt(10, 1);
                    prepStmntPersonInsert.setInt(11, 1);
                    prepStmntPersonInsert.setString(12, allMembers.get(i).getEffectiveName());
                    prepStmntPersonInsert.setInt(13,0);
                    prepStmntPersonInsert.setInt(14,0);
                    prepStmntPersonInsert.setInt(15, 0);
                    prepStmntPersonInsert.setInt(16, 0);
                    prepStmntPersonInsert.setInt(17, 0);
                    prepStmntPersonInsert.setInt(18, 0);
                    prepStmntPersonInsert.setInt(19, 0);
                    prepStmntPersonInsert.setInt(20, 0);
                    prepStmntPersonInsert.setInt(21, 1);
                    prepStmntPersonInsert.setInt(22, 0);


                    prepStmntPersonInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static int giveXP(String id, int xpamount) {
        int plrxp = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set xp = xp + ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, xpamount);
            prepStmntPersonInsert.setString(2, id);
            prepStmntPersonInsert.executeUpdate();

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT xp FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, id);

            myRS = prepStmntPersonInsert.executeQuery();

            if(myRS.next()){
                plrxp = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return plrxp;
    }

    public static int getXP(String id){
        int currlev = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT xp FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, id);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                currlev = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return currlev;
    }

    public static int getLevel(String memberID){
        int currlev = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT level FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                currlev = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return currlev;
    }

    public static void giveLevel(String id) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set level = level + 1 where idUsers = ?");
            prepStmntPersonInsert.setString(1, id);
            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void updateLastMessage(String id, int second) {

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_send = ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, second);
            prepStmntPersonInsert.setString(2, id);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static int getLastMessage(String id){
        int lastm = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT last_send FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, id);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                lastm = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return lastm;
    }

    public static int getStatistic(String statName, String memberID){
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT " + statName + " FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }


    public static int getGlobalPosition(String memberID){
        int pos = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select rowNR from (select ROW_NUMBER() OVER (ORDER BY level DESC) AS rowNR,idUsers FROM users) sub WHERE sub.idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                pos = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return pos;

    }

    public static int getGlobalSoulPosition(String memberID){
        int pos = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select rowNR from (select ROW_NUMBER() OVER (ORDER BY souls DESC) AS rowNR,idUsers FROM users) sub WHERE sub.idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                pos = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return pos;

    }

    public static int getNotificationState(String serverID){
        int noti = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT enableNotification FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                noti = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return noti;
    }

    public static void giveSouls(String memberid, int amt) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberid);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set souls = souls + ? where idUsers = ?");
                prepStmntPersonInsert.setInt(1, amt);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void giveBossCount(String serverID, int amt) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idServer FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update server set defeatedBosses = defeatedBosses + ? where idServer = ?");
                prepStmntPersonInsert.setInt(1, amt);
                prepStmntPersonInsert.setString(2, serverID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void giveAreaClear(String serverID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idServer FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update server set clearedAreas = clearedAreas + ? where idServer = ?");
                prepStmntPersonInsert.setInt(1, 1);
                prepStmntPersonInsert.setString(2, serverID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void setNotificationState(String serverID, int value) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idServer FROM server WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update server set enableNotification = ? where idServer = ?");
                prepStmntPersonInsert.setInt(1, value);
                prepStmntPersonInsert.setString(2, serverID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean upgradeSkill(String memberID, String value, int amount) {
        boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set " + value + " = " + value + " + " + amount + " where idUsers = ?");
                prepStmntPersonInsert.setString(1, memberID);
                prepStmntPersonInsert.executeUpdate();
                ret = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }

        return ret;
    }

    public static void reduceSouls(String memberID, long amount) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set souls = souls - ? where idUsers = ?");
                prepStmntPersonInsert.setDouble(1, amount);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }

    }

    public static void createUpgradeRelation(String messageID, String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM upgrade_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageID);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO upgrade_message (idMessage, idMember) VALUES( ?, ?)");
                prepStmntPersonInsert.setString(1, messageID);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    private static void doFinally() {
        if(myRS != null) {
            try {
                myRS.close();
                myRS = null;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(myStmnt != null) {
            try {
                myStmnt.close();
                myStmnt = null;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(myCon != null) {
            try {
                myCon.close();
                myCon = null;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static boolean getUpgradeRelation(String messageID, String memberID) {
        boolean ret = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM upgrade_message WHERE idMessage = ? and idMember = ?");
            prepStmntPersonInsert.setString(1, messageID);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return ret;
    }

    public static void updateClearTime(String id, int newtime) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_clear = ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, newtime);
            prepStmntPersonInsert.setString(2, id);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static int getTotalClearsFromServer(String serverID) {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select SUM(clearedAreas) from server where idServer = ?;");
            prepStmntPersonInsert.setString(1, serverID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getTotalBossesFromServer(String serverID) {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select SUM(defeatedBosses) from server where idServer = ?;");
            prepStmntPersonInsert.setString(1, serverID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getAreaProgress(String memberID) {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select sum(strength + dexterity + vitality + resistance) from users where idUsers = ?;");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getTotalBossCount() {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select sum(defeatedBosses) from server;");
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getTotalItemValue(String memberID) {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select sum(value * count) from items i, item_user_relation r where r.idItem = i.idItem and idUser = ?  and type = 'item'");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getTotalClearCount() {
        int value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select sum(clearedAreas) from server;");
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void setNotificationChannel(String serverID, String channelID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update server set notificationChannel = ? WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, channelID);
            prepStmntPersonInsert.setString(2, serverID);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static String getNotificationChannel(String serverID){
        String value = "empty";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT notificationChannel from server where idServer = ?");
            prepStmntPersonInsert.setString(1, serverID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getString(1);
            }else{
                value = "empty";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void clearAreaTime() {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_clear = 0;");
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void updateBossTime(Integer newtime,String id) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_boss = ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, newtime);
            prepStmntPersonInsert.setString(2, id);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void clearBossTime() {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_boss = 0;");
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static ArrayList<String> showLevelLeaderboard() {
        int count = 0;
        ArrayList<String> values = new ArrayList<String>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users ORDER BY level DESC;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= 14){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static ArrayList<String> getOwnedItems(String memberid, String item_type, String field) {
        ArrayList<String> values = new ArrayList<>();

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select " + field + " from items i, item_user_relation r where i.idItem = r.idItem AND idUser = ? and type = '" + item_type + "' ORDER BY i.bonus + (5 * r.level) DESC;");
            prepStmntPersonInsert.setString(1, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            while (myRS.next()) {
                values.add(myRS.getString(1));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }



    public static ArrayList<Integer> getAllitems() {

        ArrayList<Integer> values = new ArrayList<Integer>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select iditem from items where obtainable = 0;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next()){
                values.add(myRS.getInt(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }


    public static String getItemName(Integer itemID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from items where idItem = ?");
            prepStmntPersonInsert.setInt(1, itemID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static String getTitle(Integer itemID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from titles where tID = ?");
            prepStmntPersonInsert.setInt(1, itemID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static String showUsernameLeaderboardLevel(String memberID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select username from users where idusers = ? ORDER BY level DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static String showUsernameLeaderboardKills(String memberID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select username from users where idusers = ? ORDER BY kills DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }


    public static int showLevelLeaderboardOrderBy(String memID){
        int cunt = 0;
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select level from users where idusers = ? ORDER BY level DESC;");
            prepStmntPersonInsert.setString(1, memID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){

                cunt = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return cunt;
    }

    public static void deletePvpRelation(String messageId) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("DELETE FROM duel_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteDuelRelation(String messageId) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("DELETE FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean completeAchievement(String memberID, Integer aID){
        boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT aID FROM achievements_user_relation WHERE idUser = ? and aID = ?");
            prepStmntPersonInsert.setString(1, memberID);
            prepStmntPersonInsert.setInt(2, aID);


            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                ret = true;
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("insert into achievements_user_relation values(NULL, ?, ?);");
                prepStmntPersonInsert.setString(1, memberID);
                prepStmntPersonInsert.setInt(2, aID);

                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static ArrayList<String> showSoulsLeaderboard() {
        int count = 0;
        ArrayList<String> values = new ArrayList<String>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users ORDER BY souls DESC;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= 14){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static Integer showSoulsLeaderboardOrderBy(String memID) {
        int cunt = 0;
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select souls from users where idusers = ? ORDER BY souls DESC;");
            prepStmntPersonInsert.setString(1, memID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){

                cunt = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return cunt;
    }

    public static String showUsernameLeaderboardSouls(String memberID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select username from users where idusers = ? ORDER BY souls DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static void giveItem(String memberid, Integer itemid) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idItem FROM item_user_relation WHERE idItem = ? and idUser = ?");
            prepStmntPersonInsert.setInt(1, itemid);
            prepStmntPersonInsert.setString(2, memberid);


            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO item_user_relation (relID, idUser, idItem, count, level) VALUES( NULL, ?, ?, ?, 0)");
                prepStmntPersonInsert.setString(1, memberid);
                prepStmntPersonInsert.setInt(2, itemid);
                prepStmntPersonInsert.setInt(3, 1);
                prepStmntPersonInsert.executeUpdate();
            }else{
                prepStmntPersonInsert = myCon.prepareStatement("update item_user_relation set count = count + 1 where idItem = ? AND idUser = ?");
                prepStmntPersonInsert.setInt(1, itemid);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void createSellRelation(String messageID, String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM selling_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageID);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO selling_message (idMessage, idMember) VALUES( ?, ?)");
                prepStmntPersonInsert.setString(1, messageID);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean findSellingMessageID(String messageId, String memberID) {
        boolean value = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idMessage from selling_message where idMessage = ? AND idMember = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void deleteSellingRelation(String messageId) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("DELETE FROM selling_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void clearInventory(String id) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("delete from item_user_relation where idUser = ? and exists(select type from items where items.idItem = item_user_relation.idItem and type = 'item');");
            prepStmntPersonInsert.setString(1, id);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void setOnlyChannelID(String id, String channelID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update server set onlychannel = ? WHERE idServer = ?");
            prepStmntPersonInsert.setString(1, channelID);
            prepStmntPersonInsert.setString(2, id);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void upvoteReward(String userID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set souls = souls + 5000 WHERE idUsers = ?");
            prepStmntPersonInsert.setString(1, userID);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void updateVoteTime(String userID, int newtime) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_vote = ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, newtime);
            prepStmntPersonInsert.setString(2, userID);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static int getAchievementLength() {
        int amt = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select count(*) from achievements;");

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                amt = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return amt;
    }

    public static ArrayList<String> getAllAchievements(Integer table_size) {
        int count = 0;
        ArrayList<String> values = new ArrayList<>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT aID FROM achievements;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= table_size){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static ArrayList<String> getAllUserAchievements(String memberID, int table_size) {
        int count = 0;
        ArrayList<String> values = new ArrayList<>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select aID from achievements_user_relation where idUser = ?;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= table_size){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static ArrayList<String> getAllDescAchievements(int table_size) {
        int count = 0;
        ArrayList<String> values = new ArrayList<>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT name FROM achievements;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= table_size){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static ArrayList<String> getAllRewardsAchievements(int table_size) {
        int count = 0;
        ArrayList<String> values = new ArrayList<>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT reward FROM achievements;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= table_size){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static void createAchievementRelation(String memberid, String messageID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM achievement_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageID);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO achievement_message (idMessage, idUser, page) VALUES( ?, ?, 1)");
                prepStmntPersonInsert.setString(1, messageID);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static Integer getAchievementPage(String memberid, String messageID){
        Integer page = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select page from achievement_message where idMessage = ? AND idUser = ?");
            prepStmntPersonInsert.setString(1, messageID);
            prepStmntPersonInsert.setString(2, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                page = myRS.getInt(1);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return page;
    }


    public static boolean findAchievementMessageID(String messageId, String memberID) {
        boolean value = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idMessage from achievement_message where idMessage = ? AND idUser = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void setAchievementPage(String memberID, String messageId,int page) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM achievement_message WHERE idmessage = ? and idUser = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update achievement_message set page = ? where idUser = ? and idMessage = ?");
                prepStmntPersonInsert.setDouble(1, page);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.setString(3, messageId);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void createInventoryRelation(String memberid, String messageID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM inventory_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageID);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO inventory_message (idMessage, idUser, page) VALUES( ?, ?, 1)");
                prepStmntPersonInsert.setString(1, messageID);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean findInventoryMessageID(String messageId, String memberID) {
        boolean value = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idMessage from inventory_message where idMessage = ? AND idUser = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static Integer getInventoryPage(String memberid, String messageID) {
        Integer page = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select page from inventory_message where idMessage = ? AND idUser = ?");
            prepStmntPersonInsert.setString(1, messageID);
            prepStmntPersonInsert.setString(2, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                page = myRS.getInt(1);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return page;
    }

    public static void setInventoryPage(String memberID, String messageId, int page) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM inventory_message WHERE idmessage = ? and idUser = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update inventory_message set page = ? where idUser = ? and idMessage = ?");
                prepStmntPersonInsert.setDouble(1, page);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.setString(3, messageId);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static int getTotalInventoryItems(String inventory_type) {
        int num = 0;
        try {
            if(inventory_type.equals("special")){
                java.sql.PreparedStatement prepStmntPersonInsert;
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("select count(*) from items where type = ?");
                prepStmntPersonInsert.setString(1, inventory_type);

                myRS = prepStmntPersonInsert.executeQuery();
            }else {
                java.sql.PreparedStatement prepStmntPersonInsert;
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("select count(*) from items where type = ? and obtainable = 0");
                prepStmntPersonInsert.setString(1, inventory_type);

                myRS = prepStmntPersonInsert.executeQuery();
            }
            if(myRS.next()){
                num = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return num;
    }

    public static int getTotalInventoryItemsOwned(String inventory_type, String memberID) {
        int num = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select count(*) from item_user_relation r, items i where i.idItem = r.idItem and idUser = ? and type = '"+ inventory_type +"';");
            prepStmntPersonInsert.setString(1, memberID);


            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                num = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return num;
    }

    public static int getTotalTitles() {
        int num = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select count(*) from titles;");

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                num = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return num;
    }

    public static int getTotalTitlesOwned(String memberid) {
        int num = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select count(*) from titles_user_relation r, titles t where t.tID = r.tID and idUser = ?;");
            prepStmntPersonInsert.setString(1, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                num = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return num;
    }

    public static String showTitlesInInventory(String memberid) {
        String name = "";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name, t.tID from titles t, titles_user_relation r where t.tId = r.tID AND idUser = ?;");
            prepStmntPersonInsert.setString(1, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            while (myRS.next()) {
                name = name + "\n <:title:781925338216923187> **" + myRS.getString(1) + "** *ID: " + myRS.getString(2) + "*";
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }

        return name;
    }

    public static void giveTitle(String memberid, Integer itemid) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT name FROM titles t, titles_user_relation r WHERE t.tID = r.tID and idUser = ? and t.tID = ?");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.setInt(2, itemid);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO titles_user_relation (trID, tID, idUser) VALUES( NULL, ?, ?)");
                prepStmntPersonInsert.setInt(1, itemid);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean equipItemInInventory(String memberid, Integer itemID, String field, String item_type) {
        boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select r.idItem from item_user_relation r, items i where i.idItem = r.IdItem and i.type = '" + item_type + "' and r.idItem = ? and idUser = ?;");
            prepStmntPersonInsert.setInt(1, itemID);
            prepStmntPersonInsert.setString(2, memberid);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set " + field + " = ? where idUsers = ?;");
                prepStmntPersonInsert.setInt(1, itemID);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static boolean equipTitleInInventory(String memberid, Integer itemID, String field) {
        boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select tID from titles_user_relation where idUser = ? and tID = ?;");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.setInt(2, itemID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set " + field + " = ? where idUsers = ?;");
                prepStmntPersonInsert.setInt(1, itemID);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static boolean findItemInInventory(String memberid, Integer itemID) {
        boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT i.idItem FROM item_user_relation r, items i WHERE i.idItem = r.idItem and i.idItem = ? and idUser = ? and not i.type = 'item';");
            prepStmntPersonInsert.setInt(1, itemID);
            prepStmntPersonInsert.setString(2, memberid);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }


    public static void giveKill(String idMember2) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set kills = kills + 1 where idUsers = ?;");
            prepStmntPersonInsert.setString(1, idMember2);
            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static Integer getItemBonus(int itemID,String memberid) {
        Integer ret = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select bonus + (5 * r.level) from item_user_relation r, items i where i.idItem = r.idItem and idUser = ? and r.idItem = ?;");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.setInt(2, itemID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = myRS.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static Integer getItemLevel(int itemID, String memberid) {
        Integer ret = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select r.level from item_user_relation r, items i where i.idItem = r.idItem and idUser = ? and r.idItem = ?;");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.setInt(2, itemID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = myRS.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static void createItemUpgradeRelation(int itemid, String memberid, String messageid,String type) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM upgrade_item_message WHERE idMessage = ? and idUser = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberid);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO upgrade_item_message (idMessage, idUser, idItem, type) VALUES( ?, ?, ?, ?)");
                prepStmntPersonInsert.setString(1, messageid);
                prepStmntPersonInsert.setString(2, memberid);
                prepStmntPersonInsert.setInt(3, itemid);
                prepStmntPersonInsert.setString(4, type);


                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static String findUpgradeMessageID(String messageId, String memberID) {
        String value = "error";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select type from upgrade_item_message where idMessage = ? AND idUser = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void deleteUpgradeItemRelation(String messageId) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("DELETE FROM upgrade_item_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static Integer getItemIDFromRelation(String messageId) {
        Integer value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idItem from upgrade_item_message where idMessage = ?");
            prepStmntPersonInsert.setString(1, messageId);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static void upgradeItem(Integer itemID, String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idItem FROM item_user_relation WHERE idItem = ? and idUser = ?");
            prepStmntPersonInsert.setInt(1, itemID);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update item_user_relation set level = level + 1 where idUser = ? and idItem = ?;");
                prepStmntPersonInsert.setString(1, memberID);
                prepStmntPersonInsert.setInt(2, itemID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteInventoryData(Member member) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("delete from inventory_message where idUser = ?");
            prepStmntPersonInsert.setString(1, member.getId());
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteAchievementData(String memberid) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("delete from achievement_message where idUser = ?");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteUpgradeData(String memberid) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("delete from upgrade_item_message where idUser = ?");
            prepStmntPersonInsert.setString(1, memberid);
            prepStmntPersonInsert.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static int getWeaponDamage(String memberID) {
        Integer value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select (bonus + (5 * r.level)) as Damage from item_user_relation r, items i, users u where i.idItem = r.idItem and r.idUser = ? and r.idItem = u.e_weapon;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static int getArmorBonus(String memberID) {
        Integer value = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select (bonus + (5 * r.level)) as Damage from item_user_relation r, items i, users u where i.idItem = r.idItem and r.idUser = ? and r.idItem = u.e_armor;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return value;
    }

    public static String showTitleLeaderboardLevel(String memberID) {
        String name = "";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from titles t, users u where u.e_title = t.tID and u.idUsers = ? ORDER BY level DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = "*" + myRS.getString(1) + "*";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static String getTitleFromMember(String memberID) {
        String name = "";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from titles t, users u where u.e_title = t.tID and u.idUsers = ?;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static String showTitleLeaderboardSouls(String memberID) {
        String name = "";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from titles t, users u where u.e_title = t.tID and u.idUsers = ? ORDER BY souls DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = "*" + myRS.getString(1) + "*";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static int getBossSize() {
        Integer amount = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select count(*) from bosses;");

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                amount = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return amount;
    }

    public static void createBossRelation(String messageid, String memberID, int bossID, int attackID, int playerHealth, int bossMaxHealth, int estus_amount) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idMessage from boss_message where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageid);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("insert into boss_message value(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                prepStmntPersonInsert.setString(1, messageid);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.setInt(3, bossID);
                prepStmntPersonInsert.setInt(4, attackID);
                prepStmntPersonInsert.setInt(5, playerHealth);
                prepStmntPersonInsert.setInt(6, bossMaxHealth);
                prepStmntPersonInsert.setInt(7, 0);
                prepStmntPersonInsert.setInt(8, 0);
                prepStmntPersonInsert.setInt(9, estus_amount);
                prepStmntPersonInsert.setInt(10, 1);

                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }


    public static String getBossName(int bossID) {
        String name = "unknown";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from bosses where idBoss = ?");
            prepStmntPersonInsert.setInt(1, bossID);
            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static int getBossHealth(int bossID) {
        Integer health = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select health from bosses where idBoss = ?");
            prepStmntPersonInsert.setInt(1, bossID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                health = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return health;
    }

    public static boolean findBossMessageID(String messageId, String memberID) {
        Boolean ret = false;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idMessage from boss_message where idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                ret = true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return ret;
    }

    public static ArrayList<String> getBossAttacks(Integer bossID, Integer phase, Integer phase2) {
        ArrayList<String> values = new ArrayList<String>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select attack_name from boss_attacks where idBoss = ? and phase = ? or phase = ? and idBoss = ?;");
            prepStmntPersonInsert.setInt(1,bossID);
            prepStmntPersonInsert.setInt(2,phase);
            prepStmntPersonInsert.setInt(3,phase2);
            prepStmntPersonInsert.setInt(4,bossID);
            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next()){
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static Integer getBossAttackID(String attackName) {
        Integer type = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select idAttack from boss_attacks where attack_name = ?");
            prepStmntPersonInsert.setString(1, attackName);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                type = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return type;
    }

    public static int getBossCurrentHealth(String messageid, String memberID) {
        Integer health = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select bossHealth from boss_message where idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                health = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return health;
    }

    public static int getPlayerCurrentHealth(String messageid, String memberID) {
        Integer health = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select playerHealth from boss_message where idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                health = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return health;
    }

    public static int getBossLastAttack(String messageid, String memberID) {
        Integer id = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select last_attack from boss_message where idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                id = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return id;
    }

    public static String getBossAttackType(int attackID) {
        String type = "";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select type from boss_attacks where idAttack = ?");
            prepStmntPersonInsert.setInt(1, attackID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                type = myRS.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return type;
    }

    public static int getBossAttackValue(int last_attack) {
        Integer value = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select value from boss_attacks where idAttack = ?");
            prepStmntPersonInsert.setInt(1, last_attack);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                value = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return value;
    }

    public static void updateBossFightHealth(String who, int newHealth,String messageid, String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update boss_message set " + who + " = ? where userID = ? and idMessage = ?;");
            prepStmntPersonInsert.setInt(1, newHealth);
            prepStmntPersonInsert.setString(2, memberID);
            prepStmntPersonInsert.setString(3, messageid);

            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static int getBossId(String messageid, String memberID) {
        Integer id = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select bossID from boss_message where idmessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                id = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return id;
    }

    public static void updateBossLastAttack(String messageid, String memberID, int attackID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update boss_message set last_attack = ? where userID = ? and idMessage = ?;");
            prepStmntPersonInsert.setInt(1, attackID);
            prepStmntPersonInsert.setString(2, memberID);
            prepStmntPersonInsert.setString(3, messageid);

            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static Integer getEstusCount(String messageid, String memberID) {
        int amt = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select sum(player_estus) from boss_message where idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                amt = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return amt;
    }

    public static void removeEstusFromBossFight(String messageid, String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update boss_message set player_estus = player_estus - 1 where userID = ? and idMessage = ?;");
            prepStmntPersonInsert.setString(1, memberID);
            prepStmntPersonInsert.setString(2, messageid);

            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteBossFightRelation(String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("Delete from boss_message where userID = ?");
            prepStmntPersonInsert.setString(1, memberID);

            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void deleteProfilRelation(String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("Delete from upgrade_message where idMember = ?;");
            prepStmntPersonInsert.setString(1, memberID);

            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void updateDailyTime(int newtime, String memberid) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_daily = ? where idUsers = ?");
            prepStmntPersonInsert.setInt(1, newtime);
            prepStmntPersonInsert.setString(2, memberid);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void clearDailyTime() {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set last_daily = 0;");
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static boolean getBossFightRelation(String messageId, String memberID) {
        boolean ret = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM boss_message WHERE idMessage = ? and userID = ?");
            prepStmntPersonInsert.setString(1, messageId);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return ret;
    }

    public static int getMonsterSize() {
        int count = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT count(*) FROM monsters;");
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                count = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return count;
    }

    public static String getMonsterName(int randomEncounterNum) {
        String name = "";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT name FROM monsters WHERE idmonster = ?;");
            prepStmntPersonInsert.setInt(1, randomEncounterNum);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                name = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return name;
    }

    public static String getMonsterDescription(int randomEncounterNum) {
        String name = "";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT description FROM monsters WHERE idmonster = ?;");
            prepStmntPersonInsert.setInt(1, randomEncounterNum);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                name = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return name;
    }

    public static String getBossImage(int bossID) {
        String text = "";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT emojiID FROM bosses WHERE idBoss = ?;");
            prepStmntPersonInsert.setInt(1, bossID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                text = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return text;
    }

    public static String getBossPhaseType(int bossID) {
        String phase = "";

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT phase_type FROM bosses WHERE idBoss = ?;");
            prepStmntPersonInsert.setInt(1, bossID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                phase = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return phase;
    }

    public static int getCurrentPhase(String messageid, String memberID) {
        int count = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT current_phase FROM boss_message where idmessage = ? and userID = ?;");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, memberID);

            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                count = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return count;
    }

    public static void updateBossPhase(String messageid, String memberID, int phase) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update boss_message set current_phase = ? where userID = ? and idMessage = ?;");
            prepStmntPersonInsert.setInt(1, phase);
            prepStmntPersonInsert.setString(2, memberID);
            prepStmntPersonInsert.setString(3, messageid);


            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static int getPlayerBoss(String memberID) {
        int bossID = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT current_boss FROM users where idUsers = ?;");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                bossID = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return bossID;
    }

    public static void setNewBoss(String memberID, Integer bossID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idBoss FROM bosses where idBoss = ?;");
            prepStmntPersonInsert.setInt(1, bossID + 1);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set current_boss  = ? where idUsers = ?;");
                prepStmntPersonInsert.setInt(1, bossID + 1);
                prepStmntPersonInsert.setString(2, memberID);
                prepStmntPersonInsert.executeUpdate();
            }else{
                if (completeAchievement(memberID, 2)) {
                    rewardUser.methode(memberID, "title", 17);
                }
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update users set current_boss = 1 where idUsers = ?;");
                prepStmntPersonInsert.setString(1, memberID);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static void updateBossWon(String memberID, Integer time) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("update users set won_boss  = ? where idUsers = ?;");
            prepStmntPersonInsert.setInt(1, time);
            prepStmntPersonInsert.setString(2, memberID);
            prepStmntPersonInsert.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static double getBossReward(int bossID) {
        double boss = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT reward FROM bosses where idBoss = ?;");
            prepStmntPersonInsert.setInt(1, bossID);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                boss = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return boss;
    }

    public static boolean getDuelRelation(String messageId) {
        boolean ret = false;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM duel where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageId);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                ret = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return ret;
    }

    public static void createPvpRelation(String messageid, String memberID1, String memberID2) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM duel_message WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);

            myRS = prepStmntPersonInsert.executeQuery();
            if (!myRS.next()) {
                prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO duel_message VALUES(?, ?, ?);");
                prepStmntPersonInsert.setString(1, messageid);
                prepStmntPersonInsert.setString(2, memberID1);
                prepStmntPersonInsert.setString(3, memberID2);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static boolean findDuelMessageID(String messageId) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM duel_message where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageId);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return false;
    }

    public static String getOtherPlayer(String messageId) {
        String memberID = null;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerID1 FROM duel_message where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageId);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                memberID = myRS.getString(1);
            }else{
                System.out.println("Not found");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return memberID;
    }

    public static String getOtherPlayerInDuel(String messageId) {
        String memberID = null;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerID1 FROM duel where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageId);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                memberID = myRS.getString(1);
            }else{
                System.out.println("Not found");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return memberID;
    }

    public static boolean getPlayerInDuel(String memberID1, String memberID2) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM duel where playerID1 = ? or playerID2 = ? or playerID1 = ? or playerID2 = ?;");
            prepStmntPersonInsert.setString(1, memberID1);
            prepStmntPersonInsert.setString(2, memberID1);
            prepStmntPersonInsert.setString(3, memberID2);
            prepStmntPersonInsert.setString(4, memberID2);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return false;
    }

    public static void createDuelRelation(String messageid, String idMember1, String idmember2, int playerMaxHealth1, int playerMaxHealth2, int playerEstus1, int playerEstus2, int turn) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("INSERT INTO duel VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            prepStmntPersonInsert.setString(1, messageid);
            prepStmntPersonInsert.setString(2, idMember1);
            prepStmntPersonInsert.setString(3, idmember2);
            prepStmntPersonInsert.setInt(4, playerMaxHealth1);
            prepStmntPersonInsert.setInt(5, playerMaxHealth2);
            prepStmntPersonInsert.setInt(6, playerEstus1);
            prepStmntPersonInsert.setInt(7, playerEstus2);
            prepStmntPersonInsert.setString(8, "none");
            prepStmntPersonInsert.setInt(9, turn);
            prepStmntPersonInsert.setInt(10, 1);
            prepStmntPersonInsert.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }
    }

    public static Integer getDuelTurn(String messageId) {
        int new_turn = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT turn FROM duel where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageId);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                new_turn = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return new_turn;
    }

    public static boolean isAllowedToAcceptDuel(String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idMessage FROM duel_message where playerID2 = ?");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return false;
    }

    public static void updateDuelLastAttack(String messageid, String attack_type) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update duel set last_attack = ? where idMessage = ?;");
                prepStmntPersonInsert.setString(1, attack_type);
                prepStmntPersonInsert.setString(2, messageid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static void updateDuelTurn(String messageid, int turn) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update duel set turn = ? where idMessage = ?;");
                prepStmntPersonInsert.setInt(1, turn);
                prepStmntPersonInsert.setString(2, messageid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static String getDuelPlayerTurn(String messageid, int turn) {
        String memberID = null;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerID" + turn + " FROM duel where idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                memberID = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return memberID;
    }

    public static String getDuelPlayerID(String messageid, int who) {
        String memberID = null;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerID" + who + " FROM duel where idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                memberID = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return memberID;
    }

    public static int getDuelPlayerHeal(String messageID,Integer numb) {
        int health = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerCurrentHealth" + numb + " FROM duel where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageID);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                health = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return health;
    }

    public static String getDuelLastAttack(String messageid) {
        String attack = null;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT last_attack FROM duel where idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                attack = myRS.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return attack;
    }

    public static void updateDuelHealth(String messageid, int playerCurrentHealth2, int who) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update duel set playerCurrentHealth" + who + " = ? where idMessage = ?;");
                prepStmntPersonInsert.setInt(1, playerCurrentHealth2);
                prepStmntPersonInsert.setString(2, messageid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }


    public static int getDuelExtraTurn(String messageid) {
        int t = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT extra_turn FROM duel where idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                t = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return t;
    }

    public static void updateDuelExtraTurn(String messageid, String turn) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                if(turn.equals("+")) {
                    myCon = DriverManager.getConnection(url, user, pwd);
                    prepStmntPersonInsert = myCon.prepareStatement("update duel set extra_turn = extra_turn + 1 where idMessage = ?;");
                    prepStmntPersonInsert.setString(1, messageid);
                    prepStmntPersonInsert.executeUpdate();
                }else if(turn.equals("-")){
                    myCon = DriverManager.getConnection(url, user, pwd);
                    prepStmntPersonInsert = myCon.prepareStatement("update duel set extra_turn = 1 where idMessage = ?;");
                    prepStmntPersonInsert.setString(1, messageid);
                    prepStmntPersonInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static int getDuelEstusCount(String messageid, int numb) {
        int amt = 0;
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerEstus" + numb + " FROM duel where idMessage = ?;");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                amt = myRS.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return amt;
    }

    public static void removeEstusFromDuel(String messageid, int numb) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idmessage FROM duel WHERE idMessage = ?");
            prepStmntPersonInsert.setString(1, messageid);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                myCon = DriverManager.getConnection(url, user, pwd);
                prepStmntPersonInsert = myCon.prepareStatement("update duel set playerEstus" + numb + " = playerEstus" + numb + " - 1 where idMessage = ?;");
                prepStmntPersonInsert.setString(1, messageid);
                prepStmntPersonInsert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            doFinally();
        }
    }

    public static ArrayList<String> showKillsLeaderboard() {
        int count = 0;
        ArrayList<String> values = new ArrayList<String>();
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT idUsers FROM users ORDER BY kills DESC;");

            myRS = prepStmntPersonInsert.executeQuery();
            while(myRS.next() && count <= 14){
                count++;
                values.add(myRS.getString(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return values;
    }

    public static int getGlobalKillsPosition(String id) {
        int pos = 0;

        try {
            java.sql.PreparedStatement prepStmntPersonInsert;

            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select rowNR from (select ROW_NUMBER() OVER (ORDER BY kills DESC) AS rowNR,idUsers FROM users) sub WHERE sub.idUsers = ?");
            prepStmntPersonInsert.setString(1, id);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                pos = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            doFinally();
        }
        return pos;
    }

    public static Integer showKillsLeaderboardOrderBy(String memID) {
        int cunt = 0;
        try {

            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select kills from users where idusers = ? ORDER BY kills DESC;");
            prepStmntPersonInsert.setString(1, memID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){

                cunt = myRS.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return cunt;
    }

    public static String showTitleLeaderboardKills(String memberID) {
        String name = "";
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("select name from titles t, users u where u.e_title = t.tID and u.idUsers = ? ORDER BY kills DESC;");
            prepStmntPersonInsert.setString(1, memberID);

            myRS = prepStmntPersonInsert.executeQuery();
            if(myRS.next()){
                name = "*" + myRS.getString(1) + "*";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            doFinally();
        }
        return name;
    }

    public static boolean duelCheckFix(String memberID) {
        try {
            java.sql.PreparedStatement prepStmntPersonInsert;
            myCon = DriverManager.getConnection(url, user, pwd);
            prepStmntPersonInsert = myCon.prepareStatement("SELECT playerID1 FROM duel_message where playerID1 = ?");
            prepStmntPersonInsert.setString(1, memberID);
            myRS = prepStmntPersonInsert.executeQuery();

            if (myRS.next()) {
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            doFinally();
        }

        return true;
    }

    public static boolean doesClanTagExist(String clanTag) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT clanID FROM clan WHERE clanTag = ?");
            pStmnt.setString(1, clanTag);
            rs = pStmnt.executeQuery();

            if (rs.next()){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return false;
    }

    private static void doFinally(Connection con, PreparedStatement pStmnt, ResultSet rs) {
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pStmnt!= null){
            try {
                pStmnt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean insertClanIntoDatabase(String clanname, String clanTag, String clanOwnerID, String clanLogoURL) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("INSERT INTO clan VALUES(NULL, ?, ?, 1, 0, ?)", Statement.RETURN_GENERATED_KEYS);
            pStmnt.setString(1, clanname);
            pStmnt.setString(2, clanTag);
            pStmnt.setString(3, clanLogoURL);
            pStmnt.executeUpdate();
            rs = pStmnt.getGeneratedKeys();

            if (rs.next()){
                int clanID = rs.getInt(1);

                insertClanUserRelation(clanID, clanOwnerID);
                //TODO delete all Invite Clan Messages
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return false;
    }

    public static boolean isUserInClan(String clanOwnerID) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT clanID FROM clan_user_relation WHERE userID = ?");
            pStmnt.setString(1, clanOwnerID);
            rs = pStmnt.executeQuery();

            if (rs.next()){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return false;
    }

    public static void insertClanUserRelation(int clanID, String clanOwnerID){
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("INSERT INTO clan_user_relation VALUES(?, ?, 'Owner')");
            pStmnt.setString(1, clanOwnerID);
            pStmnt.setInt(2, clanID);
            pStmnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
    }

    public static String getClanRole(String userID) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT userRole FROM clan_user_relation WHERE userID = ?");
            pStmnt.setString(1, userID);
            rs = pStmnt.executeQuery();

            if (rs.next())
                return rs.getString(1);
            else
                return null;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return null;
    }

    public static void insertClanMessage(Clan clan, String userID, String messageID, String messageType) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT idMessage FROM clan_message WHERE userID = ? AND msgType = ? AND clanID = ?");
            pStmnt.setString(1, userID);
            pStmnt.setString(2, messageType);
            pStmnt.setInt(3, clan.getId());
            rs = pStmnt.executeQuery();

            //If there already exists
            if (rs.next()){
                pStmnt = con.prepareStatement("DELETE FROM clan_user_relation WHERE userID = ? AND clanID = ? AND msgType = ?");
                pStmnt.setString(1, userID);
                pStmnt.setInt(2, clan.getId());
                pStmnt.setString(3, messageType);
                pStmnt.executeUpdate();
            }

            pStmnt = con.prepareStatement("INSERT INTO clan_message VALUES(?, ?, ?, ?)");
            pStmnt.setString(1, userID);
            pStmnt.setInt(2, clan.getId());
            pStmnt.setString(3, messageType);
            pStmnt.setString(4, messageID);
            pStmnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
    }

    public static boolean findClanMessage(String messageId, String memberID, String messageType) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT userID FROM clan_message WHERE userID = ? AND idMessage = ? AND msgType = ?");
            pStmnt.setString(1, memberID);
            pStmnt.setString(2, messageId);
            pStmnt.setString(3, messageType);
            rs = pStmnt.executeQuery();

            if (rs.next()){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return false;
    }

    public static void leaveClan(String messageID, String userID) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("DELETE FROM clan_user_relation WHERE userID = ?");
            pStmnt.setString(1, userID);
            pStmnt.executeUpdate();

            deleteSingleClanMessage(messageID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
    }

    public static void deleteSingleClanMessage(String messageID) {
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("DELETE FROM clan_message WHERE idMessage = ?");
            pStmnt.setString(1, messageID);
            pStmnt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
    }

    public static void deleteClanMessage(String userID, int clanID, String messageType){
        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);

            if (clanID == -1) {
                pStmnt = con.prepareStatement("DELETE FROM clan_message WHERE userID = ? AND msgType = ?");
                pStmnt.setString(1, userID);
                pStmnt.setString(2, messageType);
                pStmnt.executeUpdate();
            } else {
                pStmnt = con.prepareStatement("DELETE FROM clan_message WHERE userID = ? AND msgType = ? AND clanID = ?");
                pStmnt.setString(1, userID);
                pStmnt.setString(2, messageType);
                pStmnt.setInt(3, clanID);
                pStmnt.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
    }

    public static Clan getClanOfUser(String userID){
        Clan ret = new Clan();

        Connection con = null;
        PreparedStatement pStmnt = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, pwd);
            pStmnt = con.prepareStatement("SELECT c.clanID, clanName, clanTag, souls, clanLevel, clanURL FROM clan c, clan_user_relation cu WHERE c.clanID = cu.clanID AND userID = ?");
            pStmnt.setString(1, userID);
            rs = pStmnt.executeQuery();

            if (rs.next()){
                ret.setId(rs.getInt("clanID"));
                ret.setName(rs.getString("clanName"));
                ret.setTag(rs.getString("clanTag"));
                ret.setSouls(rs.getLong("souls"));
                ret.setLevel(rs.getInt("clanLevel"));
                ret.setUrl(rs.getString("clanURL"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            doFinally(con, pStmnt, rs);
        }
        return ret;
    }
}
