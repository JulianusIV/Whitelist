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

import com.julianusiv.whitelist.shared.results.BoolCallResult;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BanlistService {
    private BanlistService() {
    }

    public static void addToBanlist(String discordId, File dataFolder){
        // get the files content
        String path = dataFolder.getPath() + "/commandBlacklist.json";
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

        //update json
        HashMap<String, Object> object = new HashMap<String, Object>();
        object.put("discordId", discordId);
        array.add(new JSONObject(object));

        //write json back to file
        String json = array.toJSONString();
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(json);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public static BoolCallResult isInBlacklist(String id, File dataFolder) {
        String path = dataFolder.getPath() + "/commandBlacklist.json";
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
        //parse the json
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
            if (((JSONObject)object).get("discordId").equals(id)) {
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
}
