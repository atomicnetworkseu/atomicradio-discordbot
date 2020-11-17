package eu.atomicnetworks.discordbot.managers;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kacper Mura
 * 2019 - 2020 Copyright (c) by MusikBots.net to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ApiManager {
    
    private final DiscordBot discord;
    private Timer timer;
    private JSONObject jsonObject;

    public ApiManager(DiscordBot discord) {
        this.discord = discord;
        this.timer = new Timer(10000, (e) -> {
            this.jsonObject = this.readJsonFromApi();
        });
        this.timer.setInitialDelay(0);
        this.timer.setRepeats(true);
        this.timer.start();
    }
    
    private JSONObject readJsonFromApi() {
        try {
            InputStream inputStream = new URL("http://88.198.175.187:9000/channels").openStream();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder stringBuilder = new StringBuilder();
                int element;
                while((element = bufferedReader.read()) != -1) {
                    stringBuilder.append((char) element);
                }
                return new JSONObject(stringBuilder.toString());
            } finally {
                inputStream.close();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public ArrayList<JSONObject> getJsonArrayList(JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();
        if(jsonArray != null) {
            for(int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.optJSONObject(i));
            }
        }
        return list;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
    
}
