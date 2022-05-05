package com.julianusiv.whitelist.shared;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.julianusiv.whitelist.shared.results.BoolCallResult;
import com.julianusiv.whitelist.shared.results.JSONArrCallResult;
import com.julianusiv.whitelist.shared.results.StringCallResult;
import com.julianusiv.whitelist.shared.results.UserVerificationResult;

public class WhitelistService {
    private WhitelistService(){
    }

    public static BoolCallResult isWhitelisted(String playerName, File dataFolder) {
        String path = dataFolder.getPath() + "/whitelist.json";
        File file = new File(path);
        String content = "";
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            BoolCallResult result = new BoolCallResult();
            result.setSuccess(false);
            result.setMessage(x.getMessage());
            return result;
        }
        JSONParser parser = new JSONParser();
        JSONArray array = null;
        try {
            array = (JSONArray) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
            BoolCallResult result = new BoolCallResult();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
        for (Object object : array) {
            String name = ((JSONObject)object).get("name").toString();
            if (name.equals(playerName)) {
                BoolCallResult result = new BoolCallResult();
                result.setSuccess(true);
                result.setValue(true);
                return result;
            }
        }
        BoolCallResult result = new BoolCallResult();
        result.setSuccess(true);
        result.setValue(false);
        return result;
    }

    public static JSONArrCallResult getAll(File dataFolder) {
        String path = dataFolder.getPath() + "/whitelist.json";
        File file = new File(path);
        String content = "";
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            JSONArrCallResult result = new JSONArrCallResult();
            result.setSuccess(false);
            result.setMessage(x.getMessage());
            return result;
        }
        //parse the json
        JSONParser parser = new JSONParser();
        JSONArray array = null;
        try {
            array = (JSONArray) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
            JSONArrCallResult result = new JSONArrCallResult();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }
        JSONArrCallResult result = new JSONArrCallResult();
        result.setSuccess(true);
        result.setValue(array);
        return result;
    }

    public static StringCallResult getDiscordId(String name, JSONArray array){
        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            if (jsonObject.get("name").equals(name)){
                StringCallResult result = new StringCallResult();
                result.setSuccess(true);
                result.setValue(jsonObject.get("discordId").toString());
                return result;
            }
        }
        StringCallResult result = new StringCallResult();
        result.setSuccess(false);
        result.setMessage("Player not found");
        return result;
    }

    public static void addUser(File dataFolder, UserVerificationResult result, String discordId){
        String path = dataFolder.getPath() + "/whitelist.json";
        File file = new File(path);
        String content = "";
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        //parse the json
        JSONParser parser = new JSONParser();
        JSONArray array = null;
        try {
            array = (JSONArray) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("name", result.getUsername());
        user.put("uuid", result.getUUID());
        user.put("discordId", discordId);
        JSONObject userJson = new JSONObject(user);

        array.add(userJson);

        //write to the file
        String json = array.toJSONString();
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset, StandardOpenOption.WRITE)) {
            writer.write(json);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public static void removeUser(File dataFolder, String name){
        String path = dataFolder.getPath() + "/whitelist.json";
        File file = new File(path);
        String content = "";
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        //parse the json
        JSONParser parser = new JSONParser();
        JSONArray array = null;
        try {
            array = (JSONArray) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray toRemove = new JSONArray();
        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            if (jsonObject.get("name").equals(name)){
                toRemove.add(object);
            }
        }
        for (Object object : toRemove) {
            array.remove(object);
        }
        //write to the file
        String json = array.toJSONString();
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(json);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
