package eu.atomicnetworks.discordbot.enums;

/**
 *
 * @author Kacper Mura
 * 2021 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public enum StationChannnel {

    ONE("one", "https://listen.atomicradio.eu/one/highquality"),
    GAMING("gaming", "https://listen.atomicradio.eu/gaming/highquality"),
    RAP("rap", "https://listen.atomicradio.eu/rap/highquality");
    
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
