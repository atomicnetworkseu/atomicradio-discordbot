package eu.atomicnetworks.discordbot.webapi.context;

import com.sun.net.httpserver.HttpServer;
import eu.atomicnetworks.discordbot.object.Listeners.Listener;
import eu.atomicnetworks.discordbot.webapi.ApiServer;
import java.util.Map;
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
            
            JSONObject json = new JSONObject(this.apiServer.getDiscordBot().getGson().toJson(this.apiServer.getDiscordBot().getBackendManager().getListeners()));
            int listener = this.apiServer.getDiscordBot().getBackendManager().getListeners().getListener().size();
            int oneListener = 0;
            int danceListener = 0;
            int trapListener = 0;
            
            for(Map.Entry<String, Listener> listenerEntry : this.apiServer.getDiscordBot().getBackendManager().getListeners().getListener().entrySet()) {
                switch(listenerEntry.getValue().getStation()) {
                    case "one":
                        oneListener = oneListener+1;
                        break;
                    case "dance":
                        danceListener = danceListener+1;
                        break;
                    case "trap":
                        trapListener = trapListener+1;
                        break;
                    default:
                        oneListener = oneListener+1;
                        break;
                }
            }
            json.put("listeners", listener);
            json.put("one_listeners", oneListener);
            json.put("dance_listeners", danceListener);
            json.put("trap_listeners", trapListener);
            this.apiServer.sendResponseJson(he, 200, json);
        });
    }
    
}
