package eu.atomicnetworks.discordbot.handler;

import com.google.gson.JsonObject;
import eu.atomicnetworks.discordbot.DiscordBot;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.discordbots.api.client.DiscordBotListAPI;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ServerListHandler {

    private final DiscordBot discord;
    private DiscordBotListAPI discordBotListAPI;

    public ServerListHandler(DiscordBot discord) {
        this.discord = discord;
        this.discordBotListAPI = new DiscordBotListAPI.Builder().token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY5NzUxNzEwNjI4NzM0NTczNyIsImJvdCI6dHJ1ZSwiaWF0IjoxNjA1OTg5OTM2fQ.hNdZ62yqdjDDHHhIXXftbNfFrLUbRiaGJKlxwa14kEk").botId("697517106287345737").build();
        Timer timer = new Timer(3600000, (ActionEvent e) -> {
            sendTopGGUpdate();
            sendDiscordBotList();
        });
        timer.setInitialDelay(60000);
        timer.setRepeats(true);
        timer.start();
    }

    private void sendTopGGUpdate() {
        this.discordBotListAPI.setStats(this.discord.getJda().getGuilds().size());
    }

    private void sendDiscordBotList() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("guilds", this.discord.getJda().getGuilds().size());
            jsonObject.addProperty("users", this.discord.getBackendManager().getUserCount());
            jsonObject.addProperty("voice_connections", this.discord.getBackendManager().getConnectionCount());

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
            Request request = new Request.Builder()
                    .url("https://discordbotlist.com/api/v1/bots/697517106287345737/stats")
                    .method("POST", body)
                    .addHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0IjoxLCJpZCI6IjY5NzUxNzEwNjI4NzM0NTczNyIsImlhdCI6MTYwNTk5MjEyOX0.E0OgUw6YZxZDs-UEZaF3EmIeXVR8xl7-pDHONwJxIxg")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ServerListHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerListHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
