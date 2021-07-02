package eu.atomicnetworks.discordbot.enums;

/**
 *
 * @author Kacper Mura
 * 2021 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public enum StationChannnel {

    ONE("original", "https://reyfm-stream07.radiohost.de/reyfm-original_mp3-320"),
    DANCE("nightlife", "https://reyfm-stream07.radiohost.de/reyfm-nightlife_mp3-320"),
    TRAP("raproyal", "https://reyfm-stream06.radiohost.de/reyfm-raproyal_mp3-320");
    
    private String name;
    private String url;

    private StationChannnel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
