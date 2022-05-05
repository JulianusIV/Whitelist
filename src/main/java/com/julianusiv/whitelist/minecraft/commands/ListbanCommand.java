package com.julianusiv.whitelist.minecraft.commands;

import java.io.File;

import java.util.ArrayList;

import org.json.simple.*;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversable;
import org.bukkit.command.CommandExecutor;

import com.julianusiv.whitelist.App;
import com.julianusiv.whitelist.shared.BanlistService;
import com.julianusiv.whitelist.shared.MinecraftService;
import com.julianusiv.whitelist.shared.WhitelistService;
import com.julianusiv.whitelist.shared.results.BoolCallResult;
import com.julianusiv.whitelist.shared.results.JSONArrCallResult;
import com.julianusiv.whitelist.shared.results.StringCallResult;

public class ListbanCommand implements CommandExecutor{
    private App plugin;
    private File dataFolder;
    private FileConfiguration config;

    public ListbanCommand(App plugin, File dataFolder, FileConfiguration config) {
        this.dataFolder = dataFolder;
        this.plugin = plugin;
        this.config = config;
        this.plugin.getCommand("listban").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            
        }
        
        // check if the player has permission to execute this command
        if(!sender.hasPermission("whitelist.listban")){
            sender.sendMessage("You don't have permission to use this command");
            return false;
        }

        // check that arguments are present
        if(args.length == 0){
            return false;
        }
        
        Conversable player = (Conversable)sender;
        String playerName = args[0];

        // check that the player is in the whitelist
        BoolCallResult result = WhitelistService.isWhitelisted(playerName, dataFolder);
        if(!result.isSuccess()){
            player.sendRawMessage("There was a problem in the files, dodo fix bug");
            return false;
        }
        if (!result.isValue()){
            player.sendRawMessage("Player " + playerName + " is not whitelisted");
            return false;
        }

        //get all players in whitelist
        JSONArrCallResult res = WhitelistService.getAll(dataFolder);
        if(!res.isSuccess()){
            player.sendRawMessage("There was a problem in the files, dodo fix bug");
            return false;
        }
        JSONArray array = res.getValue();

        // get discordID from whitelist
        StringCallResult ret = WhitelistService.getDiscordId(playerName, array);
        if(!ret.isSuccess()){
            player.sendRawMessage("There was a problem in the files, dodo fix bug");
            return false;
        }
        String id = ret.getValue();

        // add player to banlist
        BanlistService.addToBanlist(id, dataFolder);
        
        // get all players associated with the discordID
        ArrayList<String> toBan = new ArrayList<String>();
        for (Object object : array) {
            JSONObject jsonobject = (JSONObject) object;
            if (jsonobject.get("discordId").equals(id)){
                toBan.add(jsonobject.get("name").toString());
            }
        }

        array.removeIf(x -> ((JSONObject)x).get("discordId").equals(id));
        
        // add discordId to commandBlacklist
        for (String item : toBan) {
            WhitelistService.removeUser(dataFolder, item);
        }

        // kick all online players
        MinecraftService.kickPlayers(plugin, toBan, config.getString("banMessage"));

        //send back feedback
        for (String string : toBan) {
            player.sendRawMessage("Banned " + string);
        }

        player.sendRawMessage("Player " + playerName + " is now banned");

        return true;
    }
}
