package eu.atomicnetworks.discordbot.object;

/**
 *
 * @author kacpe
 */
public class GuildData {
    
    private String id;
    private String prefix;
    private String channelId;
    private String music;
    private boolean playing;
    private boolean restart;
    private int volume;

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

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    
}
