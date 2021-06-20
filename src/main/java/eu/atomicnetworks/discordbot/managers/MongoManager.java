package eu.atomicnetworks.discordbot.managers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import eu.atomicnetworks.discordbot.DiscordBot;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class MongoManager {
    
    private final DiscordBot discord;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> guilds;

    public MongoManager(DiscordBot discord) {
        this.discord = discord;
        try {
            if(this.discord.getConfig().getMongoDB() == null) this.client = MongoClients.create(new ConnectionString("mongodb://127.0.0.1"));
            if(this.discord.getConfig().getMongoDB().getUser().isEmpty() || this.discord.getConfig().getMongoDB().getPassword().isEmpty()) {
                this.client = MongoClients.create(new ConnectionString(MessageFormat.format("mongodb://{0}:{1}", this.discord.getConfig().getMongoDB().getHost(), this.discord.getConfig().getMongoDB().getPort())));
            } else {
                this.client = MongoClients.create(new ConnectionString(MessageFormat.format("mongodb://{0}:{1}@{2}:{3}/?authSource={0}", this.discord.getConfig().getMongoDB().getUser(), this.discord.getConfig().getMongoDB().getPassword(), this.discord.getConfig().getMongoDB().getHost(), this.discord.getConfig().getMongoDB().getPort())));
            }
            this.database = client.getDatabase(this.discord.getConfig().getMongoDB().getDatabase());
            this.guilds = this.database.getCollection("guilds");
            this.discord.consoleInfo("The connection to the MongoDB database has been established.");
        } catch(MongoException ex) {
            discord.consoleError("The connection to the MongoDB database could not be established.");
            Logger.getLogger(MongoManager.class.getName()).log(Level.SEVERE, null, ex);
            Runtime.getRuntime().exit(0);
        }
    }

    public MongoCollection<Document> getCollection(String name) {
        return this.database.getCollection(name);
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoCollection<Document> getGuilds() {
        return guilds;
    }
    
}
