package com.julianusiv.whitelist.discord;

import java.io.File;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import com.julianusiv.whitelist.App;
import com.julianusiv.whitelist.discord.commands.WhitelistAdd;
import com.julianusiv.whitelist.discord.commands.WhitelistBan;

public class DiscordBot implements EventListener, Runnable {
    private FileConfiguration config;
    public static JDA jda;
    private File dataFolder;
    private App plugin;

    public DiscordBot(File dataFolder, App plugin, FileConfiguration config) {
        this.config = config;
        this.dataFolder = dataFolder;
        this.plugin = plugin;
    }

    @Override
    public void run(){		
        //start discordbot				
		try {
			jda = JDABuilder.createDefault(config.getString("botToken"))
			    .enableIntents(GatewayIntent.GUILD_MESSAGES)
			    .setMemberCachePolicy(MemberCachePolicy.DEFAULT)
			    .addEventListeners(this).build();
			jda.awaitReady();
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
			jda.getPresence().setActivity(Activity.playing("Minecraft. Usage: " + config.getString("botPrefix") + "whitelist <username>"));
			
			System.out.println("ID: "+jda.getSelfUser().getAsTag()+" / "+jda.getSelfUser().getIdLong());
			
			for(Guild x:jda.getGuilds()) {
				System.out.println("in Guild: "+x.getIdLong()+"|"+x.getName()+" / "+x.getOwnerIdLong());
			}
			
		} catch (LoginException e) {
			System.err.println("Error while logging in...");
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.err.println("Wrong token...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onEvent(GenericEvent event)
    {
        if (event instanceof ReadyEvent)
            System.out.println("API is ready!");
        else if (event instanceof MessageReceivedEvent)
        	incoming((MessageReceivedEvent)event);
    }

    private void incoming(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if (config.getBoolean("linkChannels") && event.getChannel().getId().equals(config.getString("channelLinkId"))){
            Bukkit.broadcastMessage((event.getMember().getNickname() == null ?
            event.getAuthor().getName() :
            event.getMember().getNickname()) + ": " + event.getMessage().getContentRaw());
        }

        String prefix = config.getString("botPrefix");
        String message = event.getMessage().getContentRaw();

        //if message doesnt start with bot prefix, ignore it
        if (!message.startsWith(prefix)) {
            return;
        }
        
        //execute whitelist command
        if (message.startsWith(prefix + "whitelist")){
            if (message.length() < prefix.length() + 11) {
                event.getChannel().sendMessage("Usage: " + prefix + "``whitelist <username>``").queue();
                return;
            }
            WhitelistAdd.whitelistUser(event, prefix, dataFolder, config);
            return;
        }
        //execute whiteban command
        if (message.startsWith(prefix + "whiteban")){
            if (message.length() < prefix.length() + 10) {
                event.getChannel().sendMessage("Usage: " + prefix + "``whiteban <username>``").queue();
                return;
            }
            WhitelistBan.whitelistBanUser(event, prefix, dataFolder, plugin, config);
            // return;
        }
    }
}
