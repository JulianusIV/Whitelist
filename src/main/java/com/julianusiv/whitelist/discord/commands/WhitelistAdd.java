package com.julianusiv.whitelist.discord.commands;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.julianusiv.whitelist.shared.BanlistService;
import com.julianusiv.whitelist.shared.MinecraftService;
import com.julianusiv.whitelist.shared.WhitelistService;
import com.julianusiv.whitelist.shared.DiscordObjectService;
import com.julianusiv.whitelist.shared.results.BoolCallResult;
import com.julianusiv.whitelist.shared.results.UserVerificationResult;

public class WhitelistAdd {
    private WhitelistAdd() {
    }

    public static void whitelistUser(MessageReceivedEvent event, String prefix, File dataFolder, FileConfiguration config){
        //make sure user has any of allowed roles
        if(!DiscordObjectService.hasAllowedRole(event.getMember().getRoles(), event.getGuild(), config).isValue()) {
            event.getMessage().getChannel().sendMessage("You do not have the required role(s) to use this command!").queue();
            return;
        }

        //Check if user is in blacklist
        BoolCallResult res = BanlistService.isInBlacklist(event.getAuthor().getId(), dataFolder);
        if(!res.isSuccess()){
            event.getMessage().getChannel().sendMessage("There was some problem in the files, dodo <:FixBug:860965445442863125>!").queue();
            return;
        }
        if (res.isValue()) {
            event.getMessage().getChannel().sendMessage("You are blacklisted from using this command!").queue();
            return;
        }

        //Check if user is already verified
        BoolCallResult ret = WhitelistService.isWhitelisted(event.getMessage().getContentRaw().substring(prefix.length() + "whitelist".length()).trim(), dataFolder);
        if(!ret.isSuccess()){
            event.getMessage().getChannel().sendMessage("There was some problem in the files, dodo <:FixBug:860965445442863125>!").queue();
            return;
        }
        if (ret.isValue()){
            event.getMessage().getChannel().sendMessage("That user is already whitelisted!").queue();
            return;
        }

        //Check if user exists and get the uuid
        UserVerificationResult result = MinecraftService.verifyUserName(event.getMessage().getContentRaw().substring(prefix.length() + "whitelist".length()).trim());
        if (!result.isSuccess()) {
            event.getMessage().getChannel().sendMessage("There was an error validating that user.").queue();
            return;
        }
        if (!result.isValid()) {
            event.getChannel().sendMessage("Account not found").queue();
            return;
        }

        //Add user to the whitelist        
        WhitelistService.addUser(dataFolder, result, event.getAuthor().getId());

        event.getChannel().sendMessage("Whitelist entry added.").queue();
    }
}
