package eu.atomicnetworks.discordbot.managers;

import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import eu.atomicnetworks.discordbot.DiscordBot;
import org.bson.Document;

/**
 *
 * @author kacpe
 */
public class MongoManager {
    
    private final DiscordBot discord;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> guilds;

    public MongoManager(DiscordBot discord) {
        this.discord = discord;
        this.client = MongoClients.create(new ConnectionString("mongodb://127.0.0.1"));
        this.database = client.getDatabase("discordbot");
        this.guilds = this.database.getCollection("guilds");
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
