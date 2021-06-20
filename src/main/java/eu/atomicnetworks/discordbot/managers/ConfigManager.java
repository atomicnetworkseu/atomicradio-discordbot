package eu.atomicnetworks.discordbot.managers;

import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.object.BotConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Kacper Mura
 * 2021 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ConfigManager {
    
    private final DiscordBot discordBot;
    private final BotConfig config;

    public ConfigManager(DiscordBot discordBot) {
        this.discordBot = discordBot;
        this.config = loadConfig();
    }
    
    public BotConfig loadConfig() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("config.json"));
            BotConfig botConfig = new BotConfig();
            botConfig.setToken((String) jsonObject.get("token"));
            
            BotConfig.MongoDB mongoDB = new BotConfig.MongoDB();
            mongoDB.setHost((String) ((JSONObject) jsonObject.get("mongodb")).get("host"));
            mongoDB.setPort((String) ((JSONObject) jsonObject.get("mongodb")).get("port"));
            mongoDB.setUser((String) ((JSONObject) jsonObject.get("mongodb")).get("user"));
            mongoDB.setPassword((String) ((JSONObject) jsonObject.get("mongodb")).get("password"));
            mongoDB.setDatabase((String) ((JSONObject) jsonObject.get("mongodb")).get("database"));
            botConfig.setMongoDB(mongoDB);
            
            BotConfig.ServerLists serverLists = new BotConfig.ServerLists();
            serverLists.setTopGGToken((String) ((JSONObject) jsonObject.get("serverLists")).get("topggToken"));
            serverLists.setDiscordBoats((String) ((JSONObject) jsonObject.get("serverLists")).get("discordBoats"));
            serverLists.setDblToken((String) ((JSONObject) jsonObject.get("serverLists")).get("dblToken"));
            botConfig.setServerLists(serverLists);
            
            return botConfig;
        } catch (FileNotFoundException ex) {
            this.createConfig();
            return null;
        } catch (IOException | ParseException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void createConfig() {
        BotConfig botConfig = new BotConfig();
        botConfig.setToken("");

        BotConfig.MongoDB mongoDB = new BotConfig.MongoDB();
        mongoDB.setHost("127.0.0.1");
        mongoDB.setPort("27017");
        mongoDB.setUser("");
        mongoDB.setPassword("");
        mongoDB.setDatabase("atomicradio-discordbot");
        botConfig.setMongoDB(mongoDB);

        BotConfig.ServerLists serverLists = new BotConfig.ServerLists();
        serverLists.setTopGGToken("");
        serverLists.setDiscordBoats("");
        serverLists.setDblToken("");
        botConfig.setServerLists(serverLists);
        
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("config.json");
            fileWriter.write(this.discordBot.getGson().toJson(botConfig));
        } catch (IOException ex1) {
            Logger.getLogger(DiscordBot.class.getName()).log(Level.SEVERE, null, ex1);
            System.exit(0);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                this.discordBot.consoleInfo("Please configure your bot in the config.json.");
                System.exit(0);
            } catch (IOException ex1) {
                Logger.getLogger(DiscordBot.class.getName()).log(Level.SEVERE, null, ex1);
                System.exit(0);
            }
        }
    }

    public BotConfig getConfig() {
        return config;
    }
    
}
