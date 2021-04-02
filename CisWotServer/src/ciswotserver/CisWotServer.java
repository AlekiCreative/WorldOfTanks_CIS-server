/*
       První protyp
       Chanelog: ...
*/
package ciswotserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.Math.*;
import java.util.Map;


public class CisWotServer extends Thread{
    
  static  WargamingAPI api = new WargamingAPI();
  static  DatabaseController database = new DatabaseController();
  static  FileController file = new FileController();
  static  Calculation calc = new Calculation();
  static  Initialization ini = new Initialization();

  static Thread thread = new Thread(){
      public void run(){
        Startup();
      }
  };

    public static void main(String[] args) throws InterruptedException{
        //ini.InitializationTankopedia();
        //System.out.println(ini.GET_IniAccount_Archievements(529439356));
    
        //api.connect("clans/messageboard/", "", 0, "", "6f28eacfa7ac7476c75f995c3894af7361f0514c", "");
        
        
       ControlPanel panel = new ControlPanel();
       panel.setVisible(true);

    }
    
    public static void RunServer(){

    }
    
    private static void Startup() {

        
        if (database.read("cis_ini","ID", 1, "option_name") == "Chyba DB"){
            
            System.out.println(LocalDate.now() +" "+ LocalTime.now() + ": Server byl ukočen (Chyba databáze)");
    
            System.exit(0);
            
        } else {
            
            ini.Initialization();
            System.out.println(LocalDate.now() +" "+ LocalTime.now() + ": Spouštím proces aktualizace database(UpdateDatabase())");
            UpdateDatabase();
        
        
        }
        
        try {
            Thread.sleep(3600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
  
        
    }
    
    static void UpdateDatabase(){
        
        
        System.out.println(LocalTime.now() + ": Zahajená aktualizace database");
        
        UpdateClans();

        long update_database = Instant.now().getEpochSecond();
        System.out.println(LocalTime.now() + ": Aktualizace database DOKONČENA |" + update_database);

    }
    
    
    static void UpdateClans(){

            //Update CLAN DETAILS
        System.out.println(LocalTime.now() + ": Počet klanu v databasi: "+database.getCOUNT("clans"));
        
        for (int i = 1; i <= database.getCOUNT("clans"); i++){
            try{
                String CLAN_ID = database.GET_CLAN_ID(i);
                String ACCESS_TOKEN = database.GET_ACCESS_TOKEN(i);

                System.out.println(LocalTime.now() + ": Odeslan požadavek pro clan: "+CLAN_ID);
                
                String clan_details = api.GET_CLAN_DETAILS(CLAN_ID);
                String clan_statistic = api.GET_CLAN_RATING(CLAN_ID);
                                
                JSONObject clan_details_jsonObject = new JSONObject(clan_details);
                JSONObject clan_details_obj1 = clan_details_jsonObject.getJSONObject("data");                     
                JSONObject clan_details_obj2 = clan_details_obj1.getJSONObject(CLAN_ID);
                
                JSONObject clan_statistic_jsonObject = new JSONObject(clan_statistic);    
                JSONObject clan_statistic_obj1 = clan_statistic_jsonObject.getJSONObject("data");                     
                JSONObject clan_statistic_obj2 = clan_statistic_obj1.getJSONObject(CLAN_ID);

                String leader_name = clan_details_obj2.getString("leader_name");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "leader_name", leader_name));
                 
                int leader_id = clan_details_obj2.getInt("leader_id");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "leader_id", String.valueOf(leader_id)));
                
                String clan_tag = clan_details_obj2.getString("tag");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "tag", String.valueOf(clan_tag)));
                
                String clan_name = clan_details_obj2.getString("name");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "full_name", String.valueOf(clan_name)));

                JSONObject emblems = clan_details_obj2.getJSONObject("emblems");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "emblems", String.valueOf(emblems)));
                
                int members_count = clan_details_obj2.getInt("members_count");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "members_count", String.valueOf(members_count)));
                
                JSONArray  members = clan_details_obj2.getJSONArray("members");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "members", String.valueOf(members)));
                
                String color = clan_details_obj2.getString("color");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "color", String.valueOf(color)));
                
                int created_at = clan_details_obj2.getInt("created_at");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "created_at", String.valueOf(created_at)));
                
                String description = clan_details_obj2.getString("description");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "description", String.valueOf(description)));
                
                String description_html = clan_details_obj2.getString("description_html");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "description_html", String.valueOf(description_html)));
                
                String motto = clan_details_obj2.getString("motto");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "motto", String.valueOf(motto)));
                
                String old_name = clan_details_obj2.getString("old_name");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "old_name", String.valueOf(old_name)));
                
                String old_tag = clan_details_obj2.getString("old_tag");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "old_tag", String.valueOf(old_tag)));
                
                int renamed_at = clan_details_obj2.getInt("renamed_at");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "renamed_at", String.valueOf(renamed_at)));
                
                int updated_at = clan_details_obj2.getInt("updated_at");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "updated_at", String.valueOf(updated_at)));
                
                JSONObject clan_statistic_efficiency = clan_statistic_obj2.getJSONObject("efficiency");
                int clan_rating = clan_statistic_efficiency.getInt("value");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "clan_rating", String.valueOf(clan_rating)));

                JSONObject clan_statistic_avg_battles = clan_statistic_obj2.getJSONObject("battles_count_avg");                                
                float avg_battles = clan_statistic_avg_battles.getFloat("value");
                System.out.println(LocalTime.now() + ": "+database.update("clans","clan_id", Integer.valueOf(CLAN_ID), "avg_battles", String.valueOf(avg_battles)));
                
                JSONObject clan_statistic_avg_daily_battles = clan_statistic_obj2.getJSONObject("battles_count_avg_daily");                                
                float battles_count_avg_daily = clan_statistic_avg_daily_battles.getFloat("value");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "avg_battles_daily", String.valueOf(battles_count_avg_daily)));
                
                JSONObject clan_statistic_winrate = clan_statistic_obj2.getJSONObject("wins_ratio_avg");                
                float winrate = clan_statistic_winrate.getFloat("value");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "winrate", String.valueOf(winrate)));
               
                JSONObject clan_statistic_v10l_avg = clan_statistic_obj2.getJSONObject("v10l_avg");                
                float v10l_avg = clan_statistic_v10l_avg.getFloat("value");
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "v10l_avg", String.valueOf(v10l_avg)));
  
                System.out.println(LocalTime.now() + ": Aktualizace členu: "+members.length());
                
                for (int a = 0; a <= members.length()-1; a++){

                        JSONObject obj_member = members.getJSONObject(a);
                        System.out.println(LocalTime.now() + ": Zpracovává se: "+obj_member);

                        Integer account_id = obj_member.getInt("account_id");
                        System.out.println(LocalTime.now() + ": "+database.update("accounts","account_id" ,account_id, "account_id", String.valueOf(account_id)));
                     
                        JSONObject data = new JSONObject(api.GET_PLAYER_DETAILS(account_id));
                        System.out.println(LocalTime.now() + ": Data ke zpracování: "+data);
                        JSONObject account_info_data = data.getJSONObject("data");
                        JSONObject account_info_account = account_info_data.getJSONObject(account_id.toString());
                        JSONObject account_info_statistics = account_info_account.getJSONObject("statistics");
                        JSONObject account_info_random = account_info_statistics.getJSONObject("random");

                       String account_name = obj_member.getString("account_name");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "account_name", String.valueOf(account_name)));
                       
                       int acc_created_at = account_info_account.getInt("created_at");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "created_at", String.valueOf(acc_created_at)));
                       
                       String setting="{}";
                       int joined_at = obj_member.getInt("joined_at");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "joined_at", String.valueOf(joined_at)));
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "clan_id", String.valueOf(CLAN_ID)));

                       String role = obj_member.getString("role");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts","account_id" ,account_id, "role", String.valueOf(role)));

                       int global_rating = account_info_account.getInt("global_rating");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "global_rating", String.valueOf(global_rating)));                     

                       int trees_cut = account_info_statistics.getInt("trees_cut");
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "trees_cut", String.valueOf(trees_cut)));
                       
                       String ini_vehicle = ini.GET_IniAccountVehicles(account_id);
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "vehicles", ini_vehicle)); 
                       
                       String ini_random = ini.GET_IniStats(account_info_random, account_id);
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "random", ini_random)); 
                       
                       String ini_acc_archievements = String.valueOf(ini.GET_IniAccount_Archievements(account_id));
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "archievements", ini_acc_archievements)); 
                       
                       long update_accounts_database = Instant.now().getEpochSecond();
                       System.out.println(LocalTime.now() + ": "+database.update("accounts", "account_id" ,account_id, "update_time", String.valueOf(update_accounts_database)));
                       
                    }// Konec aktualizace informacích o hrácich
               
                long update_clans_database = Instant.now().getEpochSecond();
                System.out.println(LocalTime.now() + ": "+database.update("clans", "clan_id", Integer.valueOf(CLAN_ID), "update_database", String.valueOf(update_clans_database)));
                
            } catch (Exception e) {e.printStackTrace();}
        }//END Cyckle      
    }//END UpdateClans() 
}//END CLASS
    

