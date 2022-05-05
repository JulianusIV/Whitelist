package com.julianusiv.whitelist.minecraft.listeners;

import com.julianusiv.whitelist.discord.DiscordBot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.dv8tion.jda.api.entities.TextChannel;

public class ChatListener implements Listener{
    FileConfiguration config;
    public ChatListener(FileConfiguration config){
        super();
        this.config = config;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
       TextChannel channel =  DiscordBot.jda.getTextChannelById(config.getString("channelLinkId"));
       if(channel != null) {
           channel.sendMessage(event.getPlayer().getName() + ": " + event.getMessage()).queue();
       }
    }
}
