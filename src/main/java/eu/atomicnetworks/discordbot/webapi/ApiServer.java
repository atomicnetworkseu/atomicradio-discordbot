package eu.atomicnetworks.discordbot.webapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import eu.atomicnetworks.discordbot.DiscordBot;
import eu.atomicnetworks.discordbot.webapi.context.ListenerContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kacper Mura
 * 2020 Copyright (c) by atomicradio.eu to present.
 * All rights reserved. https://github.com/VocalZero
 *
 */
public class ApiServer {
    
    private final DiscordBot discordBot;
    private final int port;
    private HttpServer httpServer;
    private final ListenerContext listenerContext;

    public ApiServer(DiscordBot discordBot, int port) {
        this.discordBot = discordBot;
        this.port = port;
        this.start();
        this.listenerContext = new ListenerContext(this, this.httpServer);
    }
    
    private void start() {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            this.httpServer.start();
            this.discordBot.consoleInfo("Starting Webapi on 0.0.0.0:" + port + ".");
        } catch (IOException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public int getPort() {
        return port;
    }

    public ListenerContext getListenerContext() {
        return listenerContext;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }
    
    public void sendResponseMessage(HttpExchange httpExchange, int status, String message) {
        try {
            JSONObject result = new JSONObject("{code: " + status + ", message: '" + message + "'}");
            byte[] bs = result.toString().getBytes("UTF-8");
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
            httpExchange.sendResponseHeaders(status, bs.length);
            httpExchange.getResponseBody().write(bs);
            httpExchange.getResponseBody().close();
        } catch (IOException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendResponseJson(HttpExchange httpExchange, int status, JSONObject message) {
        try {
            byte[] bs = message.toString().getBytes("UTF-8");
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
            httpExchange.sendResponseHeaders(status, bs.length);
            httpExchange.getResponseBody().write(bs);
            httpExchange.getResponseBody().close();
        } catch (IOException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendResponseJson(HttpExchange httpExchange, int status, JSONArray message) {
        try {
            byte[] bs = message.toString().getBytes("UTF-8");
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
            httpExchange.sendResponseHeaders(status, bs.length);
            httpExchange.getResponseBody().write(bs);
            httpExchange.getResponseBody().close();
        } catch (IOException ex) {
            Logger.getLogger(ApiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
