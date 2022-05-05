package com.julianusiv.whitelist.shared;

import java.util.List;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.julianusiv.whitelist.App;
import com.julianusiv.whitelist.shared.results.UserVerificationResult;

public class MinecraftService {
    private MinecraftService() {
    }

    public static void kickPlayers(App plugin, List<String> toKick, String message){
        if (!plugin.getServer().getOnlinePlayers().isEmpty()){
            for (Player p : plugin.getServer().getOnlinePlayers()){
                if (toKick.contains(p.getName())) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            p.kickPlayer(message);
                        }
                    });
                }
            }
        }
    }

    public static UserVerificationResult verifyUserName(String username){
        UserVerificationResult result = new UserVerificationResult();
        HttpURLConnection connection = null;
        
    	try {
            //Create connection
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+username);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
            
			connection.setUseCaches(false);
			
			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
			}
			
			rd.close();
			connection.disconnect();
			
			System.out.println("RESPONSE: "+response.toString());
			//Parse Response
			JsonStreamParser jp=new JsonStreamParser(response.toString().trim());
			JsonObject jo=jp.next().getAsJsonObject();
			//create response object and return
			if(jo.has("id")) {
                String uuid=jo.get("id").getAsString();
				String name=jo.get("name").getAsString();
				
				result.setUsername(name);
                result.setUUID(uuid);
                result.setValid(true);
                result.setSuccess(true);
                return result;
			}else {
                result.setValid(false);
                result.setSuccess(true);
				return result;
			}
		} catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
		    return result;
		} finally {
            if (connection != null)
                connection.disconnect();
		}
    }
}
