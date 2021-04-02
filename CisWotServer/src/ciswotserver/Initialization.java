/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ciswotserver;

import static ciswotserver.CisWotServer.api;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alikp
 */
public class Initialization {
    
    static  WargamingAPI api = new WargamingAPI();
    static  DatabaseController database = new DatabaseController();
    static  FileController file = new FileController();
    static  Calculation calc = new Calculation();
    static  Initialization ini = new Initialization();
    
    public static String VEHICLES;
    public static String ACHIEVEMENTS;
    
    public static JSONObject list_vehicle_jsonObject;
    public static JSONObject list_vehicle_obj1;
    
    
    public static JSONObject list_achievements_jsonObject;
    public static JSONObject list_achievements_obj1;
    
    public static JSONArray WNE_DATA;
    


    
    public static void Initialization(){
        
       VEHICLES = api.GET_LIST_AVAIBLE_VEHICLES();
       list_vehicle_jsonObject = new JSONObject(VEHICLES);
       list_vehicle_obj1 = list_vehicle_jsonObject.getJSONObject("data");                     
 
       ACHIEVEMENTS = api.GET_LIST_ACHIEVEMENTS();
       list_achievements_jsonObject = new JSONObject(ACHIEVEMENTS);
       list_achievements_obj1 = list_achievements_jsonObject.getJSONObject("data");  

        //https://static.modxvm.com/wn8-data-exp/json/wn8exp.json
        String json_data = "";    
        
        try {
            System.out.println(LocalTime.now() + ": Navazuji spojení s modxvm");
            URL url = new URL("https://static.modxvm.com/wn8-data-exp/json/wn8exp.json");
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
        JSONObject data = new JSONObject(json_data);
        WNE_DATA = data.getJSONArray("data");

    }
    
    public JSONArray getExpValue(){
        return WNE_DATA;
    }
    
    
    String GET_IniAccountVehicles(int account_id){
        
        //Inializace
        //Struktura polotovaru vozidel
        HashMap<String, String> personal_vehicle = new HashMap<String, String>();
        HashMap<String, String> personal_vehicle_random = new HashMap<String, String>();
        HashMap<String, String> personal_vehicle_achievements = new HashMap<String, String>();

        JSONObject player_vehicle_jsonObject = new JSONObject(api.GET_PLAYER_VEHICLES(account_id));
        JSONObject player_vehicle_obj1 = player_vehicle_jsonObject.getJSONObject("data");                     
        JSONArray player_vehicle_array = player_vehicle_obj1.getJSONArray(String.valueOf(account_id));
        
        JSONObject return_data = new JSONObject();
        
        for (int u = 0; u < player_vehicle_array.length(); u++){
                           
            JSONObject obj_player_vehicle = player_vehicle_array.getJSONObject(u);
            JSONObject obj_random_player_vehicle = obj_player_vehicle.getJSONObject("random");                            
    
            if(list_vehicle_obj1.has(String.valueOf(obj_player_vehicle.getInt("tank_id")))){
                
                JSONObject vehicle_data = list_vehicle_obj1.getJSONObject(String.valueOf(obj_player_vehicle.getInt("tank_id")));
  
                personal_vehicle.put("vehicle_id", String.valueOf(obj_player_vehicle.getInt("tank_id")));
                personal_vehicle.put("vehicle_name", vehicle_data.getString("name"));
                
                JSONObject vehicle_image = vehicle_data.getJSONObject("images");
                personal_vehicle.put("icon", vehicle_image.getString("contour_icon"));
                personal_vehicle.put("big_image", vehicle_image.getString("big_icon"));

                personal_vehicle.put("nation", vehicle_data.getString("nation"));
                personal_vehicle.put("tier", String.valueOf(vehicle_data.getInt("tier")));
                personal_vehicle.put("type", vehicle_data.getString("type"));

                personal_vehicle.put("in_garage", ""); //Private
                
                personal_vehicle.put("achievements", "");
                personal_vehicle.put("mark_of_mastery", String.valueOf(obj_player_vehicle.getInt("mark_of_mastery")));
                personal_vehicle.put("wg_rating", ""); //Příprava do budoucna

                //Random
                personal_vehicle_random.put("battles", String.valueOf(obj_random_player_vehicle.getInt("battles")));
                personal_vehicle_random.put("winrate", String.valueOf(calc.WinRate(obj_random_player_vehicle.getInt("battles"), obj_random_player_vehicle.getInt("wins"))));

                personal_vehicle_random.put("wins", String.valueOf(obj_random_player_vehicle.getInt("wins")));
                personal_vehicle_random.put("losses", String.valueOf(obj_random_player_vehicle.getInt("losses")));
                personal_vehicle_random.put("draws", String.valueOf(obj_random_player_vehicle.getInt("draws")));
                personal_vehicle_random.put("survived_battles", String.valueOf(obj_random_player_vehicle.getInt("survived_battles")));
                personal_vehicle_random.put("xp", String.valueOf(obj_random_player_vehicle.getInt("xp")));
                personal_vehicle_random.put("damage_dealt", String.valueOf(obj_random_player_vehicle.getInt("damage_dealt")));
                personal_vehicle_random.put("damage_received", String.valueOf(obj_random_player_vehicle.getInt("damage_received")));
                personal_vehicle_random.put("frags", String.valueOf(obj_random_player_vehicle.getInt("frags")));
                personal_vehicle_random.put("hits_percents", String.valueOf(obj_random_player_vehicle.getInt("hits_percents")));

                personal_vehicle_random.put("max_damage", String.valueOf(obj_random_player_vehicle.getInt("max_damage")));
                personal_vehicle_random.put("max_frags", String.valueOf(obj_random_player_vehicle.getInt("max_frags")));
                personal_vehicle_random.put("max_xp", String.valueOf(obj_random_player_vehicle.getInt("max_xp")));

                personal_vehicle_random.put("avgDmg", String.valueOf(calc.avg(Double.valueOf(obj_random_player_vehicle.getInt("damage_dealt")), Double.valueOf(obj_random_player_vehicle.getInt("battles")))));
                personal_vehicle_random.put("avgSpot", String.valueOf(calc.avg(Double.valueOf(obj_random_player_vehicle.getInt("spotted")), Double.valueOf(obj_random_player_vehicle.getInt("battles")))));
                personal_vehicle_random.put("avgFrag", String.valueOf(calc.avg(Double.valueOf(obj_random_player_vehicle.getInt("frags")), Double.valueOf(obj_random_player_vehicle.getInt("battles")))));
                personal_vehicle_random.put("avgDef", String.valueOf(calc.avg(Double.valueOf(obj_random_player_vehicle.getInt("dropped_capture_points")), Double.valueOf(obj_random_player_vehicle.getInt("battles")))));
                personal_vehicle_random.put("battle_avg_xp", String.valueOf(obj_random_player_vehicle.getInt("battle_avg_xp")));

                personal_vehicle_random.put("shots", String.valueOf(obj_random_player_vehicle.getInt("shots")));
                personal_vehicle_random.put("spotted", String.valueOf(obj_random_player_vehicle.getInt("spotted")));
                personal_vehicle_random.put("dropped_capture_points", String.valueOf(obj_random_player_vehicle.getInt("dropped_capture_points")));
                personal_vehicle_random.put("capture_points", String.valueOf(obj_random_player_vehicle.getInt("capture_points")));
                
                personal_vehicle.put("WN8",  String.valueOf(
                                                    calc.WN8(
                                                            Double.valueOf(
                                                                    personal_vehicle_random.get("avgDmg")
                                                            ), Double.valueOf(
                                                                    personal_vehicle_random.get("avgSpot")
                                                            ), Double.valueOf(
                                                                    personal_vehicle_random.get("avgFrag")
                                                            ), Double.valueOf(
                                                                    personal_vehicle_random.get("avgDef")
                                                            ), Double.valueOf(
                                                                    personal_vehicle_random.get("winrate")
                                                            ), obj_player_vehicle.getInt("tank_id")
                                                    )
                                        )
                                );

            } else {
                // The vehicle is not in the list
            }
            
            personal_vehicle.put("random", "");
            JSONObject random = new JSONObject(personal_vehicle_random);
            
            JSONObject json_personal_vehicle = new JSONObject(personal_vehicle);            
            json_personal_vehicle.put("random", random);
            
                       
            return_data.put(String.valueOf(u),json_personal_vehicle); // Vehicles assembly
            
            }
            System.out.println(return_data);
            
            
            return String.valueOf(return_data);//RETURN DATA IN JSON
    }
    
    String GET_IniStats (JSONObject obj, int account_id){
    
        JSONObject return_json = new JSONObject();
        
                return_json.put("battles", String.valueOf(obj.getInt("battles")));
                return_json.put("winrate", String.valueOf(calc.WinRate(obj.getInt("battles"), obj.getInt("wins"))));

                return_json.put("wins", String.valueOf(obj.getInt("wins")));
                return_json.put("losses", String.valueOf(obj.getInt("losses")));
                return_json.put("draws", String.valueOf(obj.getInt("draws")));
                return_json.put("survived_battles", String.valueOf(obj.getInt("survived_battles")));
                return_json.put("xp", String.valueOf(obj.getInt("xp")));
                return_json.put("damage_dealt", String.valueOf(obj.getInt("damage_dealt")));
                return_json.put("damage_received", String.valueOf(obj.getInt("damage_received")));
                return_json.put("frags", String.valueOf(obj.getInt("frags")));
                return_json.put("hits_percents", String.valueOf(obj.getInt("hits_percents")));

                return_json.put("avgDmg", String.valueOf(calc.avg(Double.valueOf(obj.getInt("damage_dealt")), Double.valueOf(obj.getInt("battles")))));
                return_json.put("avgSpot", String.valueOf(calc.avg(Double.valueOf(obj.getInt("spotted")), Double.valueOf(obj.getInt("battles")))));
                return_json.put("avgFrag", String.valueOf(calc.avg(Double.valueOf(obj.getInt("frags")), Double.valueOf(obj.getInt("battles")))));
                return_json.put("avgDef", String.valueOf(calc.avg(Double.valueOf(obj.getInt("dropped_capture_points")), Double.valueOf(obj.getInt("battles")))));
                return_json.put("battle_avg_xp", String.valueOf(obj.getInt("battle_avg_xp")));

                return_json.put("shots", String.valueOf(obj.getInt("shots")));
                return_json.put("spotted", String.valueOf(obj.getInt("spotted")));
                return_json.put("dropped_capture_points", String.valueOf(obj.getInt("dropped_capture_points")));
                return_json.put("capture_points", String.valueOf(obj.getInt("capture_points")));        

                // double avgDmg, double avgSpot, double avgFrag, double avgDef, double avgWinRate, int IDNum
                return_json.put("WN8",  String.valueOf(
                                                    calc.WN8(
                                                            Double.valueOf(
                                                                    return_json.getString("avgDmg")
                                                            ), Double.valueOf(
                                                                    return_json.getString("avgSpot")
                                                            ), Double.valueOf(
                                                                    return_json.getString("avgFrag")
                                                            ), Double.valueOf(
                                                                    return_json.getString("avgDef")
                                                            ), Double.valueOf(
                                                                    return_json.getString("winrate")
                                                            ), calc.TOP_VEHICLES_BATTLES(database.read("accounts","account_id", account_id, "vehicles"))
                                                    )
                                        )
                                );        
        
    
    return String.valueOf(return_json);
    }
    
    JSONObject GET_IniAccount_Archievements (int account_id){

        JSONObject jsonObject = new JSONObject(api.GET_PLAYER_ACHIEVEMENTS(account_id));
        JSONObject obj1 = jsonObject.getJSONObject("data");        
        JSONObject account = obj1.getJSONObject(String.valueOf(account_id));        
        JSONObject obj_achievements = account.getJSONObject("achievements");        
        JSONObject obj_frags = account.getJSONObject("frags");        
        JSONObject obj_max_series = account.getJSONObject("max_series");        
        
        Set<String> map_achievements = obj_achievements.keySet() ;
        Set<String> map_frags = obj_frags.keySet();
        Set<String> map_max_series = obj_max_series.keySet();
        
        List list_achievements = new ArrayList(map_achievements);
        List list_frags = new ArrayList(map_frags);
        List list_max_series = new ArrayList(map_max_series);
        
        JSONObject personal_list_achievements = new JSONObject();

        
        for(int a = 0; a < obj_achievements.length(); a++){

            JSONObject achievements_data = list_achievements_obj1.getJSONObject(String.valueOf(list_achievements.get(a)));     

            JSONObject achievements = new JSONObject();
            
                if(achievements_data.isNull("options")){
                    achievements.put("options", "");
                } else achievements.put("options", String.valueOf(achievements_data.getJSONArray("options")));

                if(achievements_data.isNull("image_big")){
                    achievements.put("image_big", "");
                } else achievements.put("image_big", achievements_data.getString("image_big"));
                
                if(achievements_data.isNull("image")){
                    achievements.put("image", "");
                } else achievements.put("image", achievements_data.getString("image"));
                
                if(achievements_data.isNull("condition")){
                    achievements.put("condition", "");
                } else achievements.put("condition", achievements_data.getString("condition"));

                if(achievements_data.isNull("name_i18n")){
                    achievements.put("name_i18n", "");
                } else achievements.put("name_i18n", achievements_data.getString("name_i18n"));

                achievements.put("name", achievements_data.getString("name"));
                achievements.put("outdated", achievements_data.getBoolean("outdated"));
                achievements.put("section", achievements_data.getString("section"));
                achievements.put("order", String.valueOf(achievements_data.getInt("order")));
                achievements.put("type", achievements_data.getString("type"));
                achievements.put("description", achievements_data.getString("description"));
            
            personal_list_achievements.put(achievements_data.getString("name"), achievements);    
            
        }
        
        for(int a = 0; a < obj_frags.length(); a++){
            
            JSONObject achievements_data = list_achievements_obj1.getJSONObject(String.valueOf(list_frags.get(a)));     

            JSONObject achievements = new JSONObject();
            
                if(achievements_data.isNull("options")){
                    achievements.put("options", "");
                } else achievements.put("options", String.valueOf(achievements_data.getJSONArray("options")));

                if(achievements_data.isNull("image_big")){
                    achievements.put("image_big", "");
                } else achievements.put("image_big", achievements_data.getString("image_big"));
                
                if(achievements_data.isNull("image")){
                    achievements.put("image", "");
                } else achievements.put("image", achievements_data.getString("image"));
                
                if(achievements_data.isNull("condition")){
                    achievements.put("condition", "");
                } else achievements.put("condition", achievements_data.getString("condition"));

                if(achievements_data.isNull("name_i18n")){
                    achievements.put("name_i18n", "");
                } else achievements.put("name_i18n", achievements_data.getString("name_i18n"));

                achievements.put("name", achievements_data.getString("name"));
                achievements.put("outdated", achievements_data.getBoolean("outdated"));
                achievements.put("section", achievements_data.getString("section"));
                achievements.put("order", String.valueOf(achievements_data.getInt("order")));
                achievements.put("type", achievements_data.getString("type"));
                achievements.put("description", achievements_data.getString("description"));
            
            personal_list_achievements.put(achievements_data.getString("name"), achievements);
        }
        
        for(int a = 0; a < obj_max_series.length(); a++){

            JSONObject achievements_data = list_achievements_obj1.getJSONObject(String.valueOf(list_max_series.get(a)));     
            JSONObject achievements = new JSONObject();
            
                if(achievements_data.isNull("options")){
                    achievements.put("options", "");
                } else achievements.put("options", String.valueOf(achievements_data.getJSONArray("options")));

                if(achievements_data.isNull("image_big")){
                    achievements.put("image_big", "");
                } else achievements.put("image_big", achievements_data.getString("image_big"));
                
                if(achievements_data.isNull("image")){
                    achievements.put("image", "");
                } else achievements.put("image", achievements_data.getString("image"));
                
                if(achievements_data.isNull("condition")){
                    achievements.put("condition", "");
                } else achievements.put("condition", achievements_data.getString("condition"));

                if(achievements_data.isNull("name_i18n")){
                    achievements.put("name_i18n", "");
                } else achievements.put("name_i18n", achievements_data.getString("name_i18n"));

                achievements.put("name", achievements_data.getString("name"));
                achievements.put("outdated", achievements_data.getBoolean("outdated"));
                achievements.put("section", achievements_data.getString("section"));
                achievements.put("order", String.valueOf(achievements_data.getInt("order")));
                achievements.put("type", achievements_data.getString("type"));
                achievements.put("description", achievements_data.getString("description"));
            
            personal_list_achievements.put(achievements_data.getString("name"), achievements);
        }
        
    
    return personal_list_achievements;
    }

    
}
