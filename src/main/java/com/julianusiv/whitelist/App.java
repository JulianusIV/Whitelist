package com.julianusiv.whitelist;

import com.julianusiv.whitelist.discord.DiscordBot;
import com.julianusiv.whitelist.shared.startup.Initializer;
import com.julianusiv.whitelist.minecraft.commands.ListbanCommand;
import com.julianusiv.whitelist.minecraft.listeners.ChatListener;
import com.julianusiv.whitelist.minecraft.listeners.JoinListener;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    boolean isDcConfigured;

    @Override
    public void onEnable() {
        // Generate neccessary files
        Initializer.initWhitelist(getDataFolder(), getLogger());
        Initializer.initCommandBlacklist(getDataFolder(), getLogger());
        // generate and/or load default config file
        saveDefaultConfig();
        reloadConfig();
        isDcConfigured = getConfig().getBoolean("enableDiscord");

        // Register listeners
        getLogger().info("Starting listeners.");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(getDataFolder(), getConfig()), this);
        if (getConfig().getBoolean("linkChannels"))
            pm.registerEvents(new ChatListener(getConfig()), this);

        // Register mc commands
        getLogger().info("Registering minecraft commands.");
        new ListbanCommand(this, getDataFolder(), getConfig());

        // Start discord bot if enabled
        if (isDcConfigured) {
            if (getConfig().getString("botToken") == null) {
                isDcConfigured = false;
                getLogger().severe("Discord token cannot be null. Disabling Discord support.");
            } else {
                getLogger().info("Starting discord bot.");
                reloadConfig();
                Thread t = new Thread(new DiscordBot(getDataFolder(), this, getConfig()));
                t.start();
            }
        }
        getLogger().info("Whitelist fully started up");
    }
    @Override
    public void onDisable() {
        // Stop discord bot if enabled
        if(isDcConfigured) {
            getLogger().info("Stopping discord bot.");
            DiscordBot.jda.shutdown();
        }
        getLogger().info("Unloaded Whitelist!");
    }
}
