/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ciswotserver;

import static ciswotserver.CisWotServer.api;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author alikp
 */
public class WargamingAPI {
    

    public static final String  APP_ID ="ef731bfbcbd4c17f5f8097e440eb0d9c";
    
    
    public static final String  LANGUAGE ="cs";
    public static final String  SERVER ="api.worldoftanks.eu";
    public static final String  GAME ="/wot/";
    
    //CLAN METHODS
    public static final String  METHOD_CLAN_DETAILS ="clans/info/"; //Method returns detailed clan information.
    public static final String  METHOD_CLAN_RATING ="clanratings/clans/"; //Method returns clan ratings by specified IDs.
    public static final String  METHOD_CLAN_MEMBER_DETAILS ="clans/accountinfo/"; //Method returns detailed clan member information and brief clan details.
    public static final String  METHOD_CLAN_MESSAGE_BOARD ="clans/messageboard/"; //Method returns messages of clan message board.
    public static final String  METHOD_CLAN_MEMBER_HISTORY ="clans/memberhistory/"; //Method returns information about player's clan history. Data on 10 last clan memberships are presented in the response.
    //PLAYER METHODS
    public static final String  METHOD_PLAYER_INFO ="account/info/"; //Method returns player details.
    public static final String  METHOD_PLAYER_VEHICLES ="account/tanks/"; //Method returns details on player's vehicles.
    public static final String  METHOD_PLAYER_ACHIEVEMENTS ="account/achievements/"; //Method returns players' achievement details.
    //PLAYER VEHICLE METHODS
    public static final String  METHOD_PLAYER_VEHICLES_STATS ="tanks/stats/"; //Method returns overall statistics, Tank Company statistics, and clan statistics per each vehicle for each user.
    public static final String  METHOD_PLAYER_VEHICLES_ARCHIEVEMENTS ="tanks/achievements/"; //Method returns list of achievements on all player's vehicles.
    //CLAN EXTRA
    public static final String  EXTRA_ONLINE_PLAYER = "private.online_members";
    //PLAYER EXTRA
    public static final String  EXTRA_BOOSTERS = "private.boosters";
    public static final String  EXTRA_GARAGE = "private.garage";
    public static final String  EXTRA_CONTACTS = "private.grouped_contacts";
    public static final String  EXTRA_MISSIONS = "private.personal_missions";
    public static final String  EXTRA_RENTED = "private.rented";
    public static final String  EXTRA_STATISTIC_RANDOM = "statistics.random";
    public static final String  EXTRA_STATISTIC_RANKED = "statistics.ranked_battles";
    public static final String  EXTRA_STATISTIC_RANKED_CURR = "statistics.ranked_battles_current";
    public static final String  EXTRA_STATISTIC_RANKED_PREV = "statistics.ranked_battles_previous";
    //VEHICLE EXTRA
    public static final String  EXTRA_RANDOM = "random"; 
    public static final String  EXTRA_RANKED_BATTLE = "statistics.ranked_battles_previous";
    //ENCYCLOPEDIA
    public static final String ENCYCLOPEDIA_VEHICLES = "encyclopedia/vehicles/";
    public static final String ENCYCLOPEDIA_ACHIEVEMENTS = "encyclopedia/achievements/";
    
    public String clan_info = "";
    
   
    public static String connect(String method, String extra, int account_id, String clan_id, String access_token, String tank_id){

    String json_data = "";    
        
    try {
            System.out.println(LocalTime.now() + ": Navazuji spojení s WG API");
            URL url = new URL("https://"+SERVER+GAME+method+"?application_id="+APP_ID+"&access_token="+access_token+"&account_id="+account_id+"&tank_id="+tank_id+"&clan_id="+clan_id+"&extra="+extra+"&language="+LANGUAGE);
            System.out.println(LocalTime.now() + ": "+ url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    json_data += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();
                   
             

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(LocalTime.now() + ": data WargamingAPI "+method+" : " + json_data);
        
                
        return json_data;
    
    
    }
    
    public String GET_CLAN_DETAILS(String clan_id){

        return connect(METHOD_CLAN_DETAILS,"",0,clan_id,"",""); //method, extra, account_id, clan_id, access_token
    }
    public String GET_CLAN_RATING(String clan_id){
    
        return connect(METHOD_CLAN_RATING,"",0,clan_id,"",""); //method, extra, account_id, clan_id, access_token
    }
    
    public String GET_PLAYER_DETAILS(int account_id){
     
        return connect(METHOD_PLAYER_INFO,EXTRA_STATISTIC_RANDOM,account_id,"","",""); //method, extra, account_id, clan_id, access_token;
    }
    
    public String GET_PLAYER_ACHIEVEMENTS(int account_id){
 
        return connect(METHOD_PLAYER_ACHIEVEMENTS,"",account_id,"","",""); //method, extra, account_id, clan_id, access_token;
    }
    
    public String GET_PLAYER_VEHICLES(int account_id){
 
        return connect(METHOD_PLAYER_VEHICLES_STATS,EXTRA_RANDOM,account_id,"","",""); //method, extra, account_id, clan_id, access_token;
    }
    

    public String GET_BIG_ICON_VEHICLE(String tank_id){
    
            String json = connect(ENCYCLOPEDIA_VEHICLES,"",0,"","",tank_id);
            JSONObject obj1 = new JSONObject(json);
            JSONObject json_data = obj1.getJSONObject("data");
            JSONObject tank_data = json_data.getJSONObject(tank_id);
            JSONObject images_data = tank_data.getJSONObject("images");
  
    return images_data.getString("big_icon");
        
    }
    
    public String GET_NAME_VEHICLE(String tank_id){
    
            String json = connect(ENCYCLOPEDIA_VEHICLES,"",0,"","",tank_id);
            JSONObject obj1 = new JSONObject(json);
            JSONObject json_data = obj1.getJSONObject("data");
            JSONObject tank_data = json_data.getJSONObject(tank_id);
  
    return tank_data.getString("short_name");
        
    }
    
    public String GET_LIST_AVAIBLE_VEHICLES (){

        return connect(ENCYCLOPEDIA_VEHICLES,"",0,"","",""); 
    }
        
    public String GET_LIST_ACHIEVEMENTS (){

        return connect(ENCYCLOPEDIA_ACHIEVEMENTS,"",0,"","",""); 
    }
        
    
    
}

