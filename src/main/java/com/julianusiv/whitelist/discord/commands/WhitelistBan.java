package com.julianusiv.whitelist.discord.commands;

import java.io.File;

import java.util.ArrayList;

import org.json.simple.*;

import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.julianusiv.whitelist.App;
import com.julianusiv.whitelist.shared.BanlistService;
import com.julianusiv.whitelist.shared.MinecraftService;
import com.julianusiv.whitelist.shared.WhitelistService;
import com.julianusiv.whitelist.shared.results.BoolCallResult;
import com.julianusiv.whitelist.shared.results.JSONArrCallResult;
import com.julianusiv.whitelist.shared.results.StringCallResult;
import com.julianusiv.whitelist.shared.DiscordObjectService;

public class WhitelistBan {
    private WhitelistBan() {
        super();
    }

    public static void whitelistBanUser(MessageReceivedEvent event, String prefix, File dataFolder, App plugin, FileConfiguration config) {
        //make sure user has any of allowed roles
        if(!DiscordObjectService.hasModRole(event.getMember().getRoles(), event.getGuild(), config).isValue()) {
            event.getMessage().getChannel().sendMessage("You do not have the required role(s) to use this command!").queue();
            return;
        }

        //Check if user is verified
        BoolCallResult result = WhitelistService.isWhitelisted(event.getMessage().getContentRaw().substring(prefix.length() + "whitelist".length()).trim(), dataFolder);
        if (!result.isSuccess()){
            event.getMessage().getChannel().sendMessage("There was a problem with the files, dodo <:FixBug:860965445442863125>").queue();
            return;
        }
        if (!result.isValue()){
            event.getChannel().sendMessage("That user is not whitelisted!").queue();
            return;
        }

        //Get all whitelisted users
        JSONArrCallResult res = WhitelistService.getAll(dataFolder);
        if (!res.isSuccess()){
           event.getMessage().getChannel().sendMessage("There was a problem with the files, dodo <:FixBug:860965445442863125>").queue();
           return;
        }
        JSONArray array = res.getValue();

        //Get the user's discord id
        StringCallResult re = WhitelistService.getDiscordId(event.getMessage().getContentRaw().substring(prefix.length() + "whitelist".length()).trim(), array);
        if (!re.isSuccess()){
            event.getMessage().getChannel().sendMessage("There was a problem with the files, dodo <:FixBug:860965445442863125>").queue();
            return;
        }
        String id = re.getValue();

        //add the discordId to the banlist
        BanlistService.addToBanlist(id, dataFolder);

        //determine usernames of entries associated with the discordId
        ArrayList<String> toBan = new ArrayList<String>();
        for (Object object : array) {
            JSONObject jsonobject = (JSONObject) object;
            if (jsonobject.get("discordId").equals(id)){
                toBan.add(jsonobject.get("name").toString());
            }
        }

        //remove users to ban from whitelist
        array.removeIf(x -> ((JSONObject)x).get("discordId").equals(id));

        // add users to command blacklist
        for (String item : toBan) {
            WhitelistService.removeUser(dataFolder, item);
        }

        //kick players if online
        MinecraftService.kickPlayers(plugin, toBan, config.getString("banMessage"));
        
        //send back info to user
        for (String string : toBan) {
            event.getChannel().sendMessage("Banned " + string).queue();
        }

        event.getChannel().sendMessage("Player " + event.getMessage().getContentRaw().substring(prefix.length() + "whitelist".length()).trim() + " is now banned").queue();
    }
}
