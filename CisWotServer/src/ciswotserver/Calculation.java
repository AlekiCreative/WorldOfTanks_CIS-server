/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ciswotserver;


import static java.lang.Double.max;
import static java.lang.Double.min;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author alikp
 */
public class Calculation {
    
    static  WargamingAPI api = new WargamingAPI();
    static  DatabaseController database = new DatabaseController();
    static  FileController file = new FileController();
    static  Calculation calc = new Calculation();
    static  Initialization ini = new Initialization();

    
    public double WN8 (double avgDmg, double avgSpot, double avgFrag, double avgDef, double avgWinRate, int IDNum){
    
        JSONArray  exp_value = ini.getExpValue();

        
        
        double expDmg = 0;
        double expSpot = 0;
        double expFrag = 0;
        double expDef = 0;        
        double expWinRate = 0; 
        
        for(int i = 0; i <= exp_value.length()-1; i++){
        JSONObject a = exp_value.getJSONObject(i);
            if(a.getInt("IDNum") == IDNum){
            
                expDmg = a.getInt("expDamage");
                expSpot = a.getInt("expSpot");
                expFrag = a.getInt("expFrag");
                expDef = a.getInt("expDef");
                expWinRate = a.getInt("expWinRate");
            
            }
        
        }
        //JSONObject data_obj2 = data_obj1.getJSONObject(IDNum);
        
        double WN8 = 0.0;
       
              
                
        double rDAMAGE = avgDmg / expDmg; // Basically you divide your average damage / expected damage.
        double rSPOT = avgSpot / expSpot;// Divide Average Spots Per battle / Expected Average Spots Per Battle.
        double rFRAG = avgFrag / expFrag; // Divide Average Kills Per Battle / Expected Kills Per Battle.
        double rDEF = avgDef / expDef; // Divide Average "Decap" Points Per Battle ( Defense points) / Expected "Decap" Points per battle.
        double rWIN = avgWinRate / expWinRate; // Divide Average Winrate / Expected Winrate.
        
        double rWINc = max(0, (rWIN - 0.71) / (1 - 0.71) ); // Basically this is (rWIN - 0.71) / (1 - 0.71). 
        double rDAMAGEc = max(0, (rDAMAGE - 0.22) / (1 - 0.22) );
        double rFRAGc = max(0, min(rDAMAGEc + 0.2, (rFRAG - 0.12) / (1 - 0.12))); // Its either (rDAMAGEc + 0.2) or (rFRAG - 0.12) / (1 - 0.12). Depends which one is the lowest.
        double rSPOTc = max(0, min(rDAMAGEc + 0.1, (rSPOT - 0.38) / (1 - 0.38)));
        double rDEFc = max(0, min(rDAMAGEc + 0.1, (rDEF - 0.10) / (1 - 0.10)));

        
        WN8 = 980*rDAMAGEc + 210*rDAMAGEc*rFRAGc + 155*rFRAGc*rSPOTc + 75*rDEFc*rFRAGc + 145*min(1.8,rWINc);

        
        return WN8;
    }
    
    public double PersonalRatingWG(){
    
        
        double bc = 9410;
        double win =0.4554;
        double surv = 0.1866;
        double dmg = 485;
        double xp = 357;
        double radio = 227;
        double track = 362;
        
        double a = 540*Math.pow(bc, 0.37);
        System.out.println(a);
        double b = 0.00163*Math.pow(bc, -0.37); //Battles
        System.out.println(b);
        double c = 3500/(1+Math.pow(Math.E, 16-31*win)); //Wins
        System.out.println(c);
        double d = 1400/(1+Math.pow(Math.E, 8-27*surv)); //Survival
        System.out.println(d);
        double e = Math.asin(0.0006 * dmg); //Damage
        System.out.println(e);
        
        double f = Math.tanh(0.002 * bc); //Battles
        System.out.println(f);
        
        double g = 3900 * Math.asin(0.0015*xp)+1.4*radio+1.1*track; // XP
        System.out.println(g);
        
        double PersonalRating = a*Math.tanh(b*(c+d+3700*e+f*g));
        System.out.println(PersonalRating);
    
        return PersonalRating;
        
    }
    
    public double WinRate(int battles, int wins){
        
        //convert values to double format
        double d_battles = battles;        
        double d_wins = wins;        
                
        double percent = d_wins / d_battles;
        percent = percent*100;
   
    return percent;
    }
    public double DamagePerBattles(int battles, int damage){
    
        double damage_per_battles= damage/battles;
    
    return damage_per_battles;
    }
    
    public double avg(double val, double total){
        
        return val / total;
    }
    
    public int TOP_VEHICLES_BATTLES(String in_obj){
        
    JSONObject obj = new JSONObject(in_obj);
    
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    map.put("tank_id", 0);
    map.put("battles", 0);

    for (int a = 0; a < obj.length(); a++) {
    
        JSONObject main = obj.getJSONObject(String.valueOf(a));
        JSONObject random = main.getJSONObject("random");
        
        int battles = Integer.valueOf(random.getString("battles"));
        
            if (battles > map.get("battles")){
            
                map.put("tank_id",  Integer.valueOf(main.getString("vehicle_id")));
                map.put("battles", battles);
            
            }
        
    }
    return map.get("tank_id");
    }
}


