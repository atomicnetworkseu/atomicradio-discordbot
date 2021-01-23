package eu.atomicnetworks.discordbot.object;

import java.util.HashMap;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class Listeners {
    
    private String guildId;
    private int listenerCount;
    private HashMap<String, Listener> listener;
    
    public static class Listener {
        
        private String username;
        private String avatar;
        private String id;
        private String channelId;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
        
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public int getListenerCount() {
        return listenerCount;
    }

    public void setListenerCount(int listenerCount) {
        this.listenerCount = listenerCount;
    }

    public HashMap<String, Listener> getListener() {
        return listener;
    }

    public void setListener(HashMap<String, Listener> listener) {
        this.listener = listener;
    }
    
}
