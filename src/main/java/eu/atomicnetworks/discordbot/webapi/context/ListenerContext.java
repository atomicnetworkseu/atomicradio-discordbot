package eu.atomicnetworks.discordbot.webapi.context;

import com.sun.net.httpserver.HttpServer;
import eu.atomicnetworks.discordbot.webapi.ApiServer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ListenerContext {
    
    private final ApiServer apiServer;
    private final HttpServer httpServer;

    public ListenerContext(ApiServer apiServer, HttpServer httpServer) {
        this.apiServer = apiServer;
        this.httpServer = httpServer;
        this.httpServer.createContext("/api/listeners", (he) -> {
            if (he.getRequestMethod().equals("OPTIONS")) {
                he.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                he.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                he.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
                he.sendResponseHeaders(204, -1);
                return;
            }
            
            if (!he.getRequestMethod().equals("GET")) {
                he.getResponseBody().close();
                return;
            }
            
            JSONArray array = new JSONArray();
            this.apiServer.getDiscordBot().getBackendManager().getListeners().getListener().entrySet().forEach(x -> {
                JSONObject json = new JSONObject(this.apiServer.getDiscordBot().getGson().toJson(x.getValue()));
                array.put(json);
            });
            
            JSONObject json = new JSONObject(this.apiServer.getDiscordBot().getGson().toJson(this.apiServer.getDiscordBot().getBackendManager().getListeners()));
            int listener = this.apiServer.getDiscordBot().getBackendManager().getListeners().getListener().size();
            json.put("listeners", listener);
            json.put("listener", array);
            this.apiServer.sendResponseJson(he, 200, json);
        });
    }
    
}
