package com.julianusiv.whitelist.shared.startup;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;

import java.nio.file.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.logging.Logger;

public class Initializer {
    private Initializer() {
        super();
    }

    public static void initWhitelist(File dataFolder, Logger logger){
        // create Plugin folder if it does not exist
        if (!dataFolder.exists()){
            dataFolder.mkdir();
            logger.info("Created Whitelist folder");
        }

        // create whitelist.json if it does not exist
        String whitelistPath = dataFolder.getPath() + "/whitelist.json";
        File whitelistFile = new File(whitelistPath);
        if (!whitelistFile.exists()) {
            Charset charset = StandardCharsets.UTF_8;
            String s = "[]";
            try (BufferedWriter writer = Files.newBufferedWriter(whitelistFile.toPath(), charset, StandardOpenOption.CREATE)) {
                writer.write(s, 0, s.length());
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
            logger.info("Created empty whitelist file");
        }
        else {
            logger.info("whitelist.json already existing");
        }
        logger.info("Whitelist enabled!");
    }

    public static void initCommandBlacklist(File dataFolder, Logger logger) {
        // create commandBlacklist.json if it does not exist
        File blacklistFile = new File(dataFolder.getPath() + "/commandBlacklist.json");
        if (!blacklistFile.exists()) {
            Charset charset = StandardCharsets.UTF_8;
            String s = "[]";
            try (BufferedWriter writer = Files.newBufferedWriter(blacklistFile.toPath(), charset, StandardOpenOption.CREATE)) {
                writer.write(s, 0, s.length());
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
            logger.info("Created empty blacklist file");
        }
        else {
            logger.info("commandBlacklist.json already existing");
        }
        logger.info("Command blacklist enabled!");
    }
}
