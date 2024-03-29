package eu.atomicnetworks.discordbot.object;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class GuildData {
    
    private String id;
    private String prefix;
    private String channelId;
    private String music;
    private boolean playing;
    private boolean tag;
    private boolean musicCommands;
    private int volume;
    private int bassLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isMusicCommandsDenied() {
        return musicCommands;
    }

    public void setMusicCommands(boolean musicCommands) {
        this.musicCommands = musicCommands;
    }

    public int getBassLevel() {
        return bassLevel;
    }

    public void setBassLevel(int bassLevel) {
        this.bassLevel = bassLevel;
    }
    
}
