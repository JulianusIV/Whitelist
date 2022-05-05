package com.julianusiv.whitelist.minecraft.listeners;

import java.io.File;

import com.julianusiv.whitelist.shared.WhitelistService;
import com.julianusiv.whitelist.shared.results.BoolCallResult;

import org.bukkit.event.Listener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class JoinListener implements Listener {
    private File dataFolder;
    private FileConfiguration config;

    public JoinListener(File dataFolder, FileConfiguration config) {
        super();
        this.dataFolder = dataFolder;
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event){
        //if user is not whitelisted disallow login
        BoolCallResult result = WhitelistService.isWhitelisted(event.getName(), dataFolder);
        if(!result.isSuccess()){
            event.disallow(Result.KICK_WHITELIST, "There was a problem in the whitelist files, dodo fix bug.");
            return;
        }
        if (!result.isValue()){
            event.disallow(Result.KICK_WHITELIST, config.getString("connectionFailMessage"));
        }
    }
}
