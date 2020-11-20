package eu.atomicnetworks.discordbot.managers;

import eu.atomicnetworks.discordbot.DiscordBot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TimeZone;
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
        this.timer = new Timer(5000, (e) -> {
            this.jsonObject = this.readJsonFromApi();
        });
        this.timer.setInitialDelay(0);
        this.timer.setRepeats(true);
        this.timer.start();
    }
    
    private JSONObject readJsonFromApi() {
        try {
            InputStream inputStream = new URL("https://api.atomicradio.eu/channels").openStream();
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
    
    public String getTitle(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("title");
    }
    
    public String getArtist(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("artist");
    }
    
    public String getPlaylist(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getString("playlist");
    }
    
    public Long getStart_at(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getLong("start_at");
    }
    
    public Long getEnd_at(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getLong("end_at");
    }
    
    public String get500Artwork(String channel) {
        return this.getJsonObject().getJSONObject(channel).getJSONObject("song").getJSONObject("artworks").getString("500");
    }
    
    public String getDurationTimestamp(String channel) {
        long time = ((this.getEnd_at(channel)*1000)-(this.getStart_at(channel)*1000));
        String minutes = String.valueOf((time / 1000) / 60);
        String seconds = String.valueOf((time / 1000) % 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getDurationTimestampNow(String channel) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli((this.getStart_at(channel)*1000)), TimeZone.getDefault().toZoneId());
        long diff = Math.abs(Duration.between(now, start).getSeconds());
        String seconds = String.valueOf(diff % 60);
        String minutes = String.valueOf(diff / 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getLastTitle(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("title");
    }
    
    public String getLastArtist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("artist");
    }
    
    public String getLastPlaylist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getString("playlist");
    }
    
    public Long getLastStart_at(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getLong("start_at");
    }
    
    public Long getLastEnd_at(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getLong("end_at");
    }
    
    public String getLast500Artwork(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("history")).stream().findFirst().orElse(null);
        return last.getJSONObject("artworks").getString("500");
    }
    
    public String getLastDurationTimestamp(String channel) {
        long time = ((this.getLastEnd_at(channel)*1000)-(this.getLastStart_at(channel)*1000));
        String minutes = String.valueOf((time / 1000) / 60);
        String seconds = String.valueOf((time / 1000) % 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getLastDurationTimestampNow(String channel) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli((this.getLastStart_at(channel)*1000)), TimeZone.getDefault().toZoneId());
        long diff = Math.abs(Duration.between(now, start).getSeconds());
        String seconds = String.valueOf(diff % 60);
        String minutes = String.valueOf(diff / 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getNextTitle(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getString("title");
    }
    
    public String getNextArtist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getString("artist");
    }
    
    public String getNextPlaylist(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getString("playlist");
    }
    
    public Long getNextStart_at(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getLong("start_at");
    }
    
    public Long getNextEnd_at(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getLong("end_at");
    }
    
    public String getNext500Artwork(String channel) {
        JSONObject last = this.getJsonArrayList(jsonObject.getJSONObject(channel).getJSONArray("schedule")).stream().findFirst().orElse(null);
        return last.getJSONObject("artworks").getString("500");
    }
    
    public String getNextDurationTimestamp(String channel) {
        long time = ((this.getNextEnd_at(channel)*1000)-(this.getNextStart_at(channel)*1000));
        String minutes = String.valueOf((time / 1000) / 60);
        String seconds = String.valueOf((time / 1000) % 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }
    
    public String getNextDurationTimestampNow(String channel) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli((this.getNextStart_at(channel)*1000)), TimeZone.getDefault().toZoneId());
        long diff = Math.abs(Duration.between(now, start).getSeconds());
        String seconds = String.valueOf(diff % 60);
        String minutes = String.valueOf(diff / 60);
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
    
}
