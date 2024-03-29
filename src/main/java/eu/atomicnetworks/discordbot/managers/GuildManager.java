package eu.atomicnetworks.discordbot.managers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.object.GuildData;
import java.util.function.Consumer;
import org.bson.Document;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class GuildManager {
    
    private final DiscordBot discord;

    public GuildManager(DiscordBot discord) {
        this.discord = discord;
    }
    
    public void getGuild(String id, Consumer<GuildData> consumer) {
        this.discord.getMongoManager().getGuilds().find(Filters.eq("id", id)).first((Document t, Throwable thrwbl) -> {
            if(t == null) {
                GuildData guildData = new GuildData();
                guildData.setId(id);
                guildData.setPrefix(".");
                guildData.setPlaying(false);
                guildData.setMusic("");
                guildData.setTag(true);
                guildData.setChannelId("");
                guildData.setVolume(25);
                guildData.setBassLevel(0);
                guildData.setMusicCommands(false);
                t = this.discord.getGson().fromJson(this.discord.getGson().toJson(guildData), Document.class);
                this.discord.getMongoManager().getGuilds().insertOne(t, (Void t1, Throwable thrwbl1) -> {
                    consumer.accept(guildData);
                });
            } else {
                GuildData guildData = this.discord.getGson().fromJson(t.toJson(), GuildData.class);
                consumer.accept(guildData);
            }
        });
    }
    
    public void saveGuild(GuildData guildData) {
        Document document = this.discord.getGson().fromJson(this.discord.getGson().toJson(guildData), Document.class);
        this.discord.getMongoManager().getGuilds().replaceOne(Filters.eq("id", guildData.getId()), document, (UpdateResult t, Throwable thrwbl) -> {
        });
    }
    
}
