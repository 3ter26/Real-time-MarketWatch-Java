package com.example.tickerchartexercise.datasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tickerchartexercise.helper.UpdateMarketWatchEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketService {
    public static WebSocketService instance;

    private WebSocketService() {
        client = new OkHttpClient();
    }

    public static synchronized WebSocketService getInstance() {
        if (instance == null) {
            instance = new WebSocketService();
        }
        return instance;
    }

    private final OkHttpClient client;
    private WebSocket webSocket;
     public String[] symbols = {
            "2010.TAD",
            "4180.TAD",
            "4061.TAD",
            "2140.TAD",
            "4130.TAD",
            "6070.TAD",
            "1120.TAD",
            "2170.TAD",
            "1080.TAD",
            "3010.TAD",
            "2210.TAD"
    };

    public void connectToWebSocket() {
        String wsUrl = LoaderLinksService.getStreamerLink();
        Request webSocket_request = new Request.Builder().url(wsUrl).build();

        webSocket = client.newWebSocket(webSocket_request, new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("WebSocket closed: "+reason);
            }
            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("webSocket Failure"+ t.getMessage());
            }
            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                try {
                    parseFromJSONToMap(text);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                System.out.println("--------WebSocket connected--------");
                try {
                    passUUID();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void parseFromJSONToMap(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            map.put(key, value);
        }
        onMessageReceived(map);
    }
    private void onMessageReceived(Map<String, Object> map) {
        EventBus.getDefault().post(new UpdateMarketWatchEvent(map));
    }

    private void passUUID() throws IOException {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = ("uid="+uuid.toString());
        try {
            webSocket.send(uuidAsString);
            System.out.println("--------UUID Sent successfully--------");
            subscribeToTopics();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void subscribeToTopics() {
        String topic, unSubscribeMessage, subscribeMessage;
        for (String symbol : symbols) {
            topic = "QO."+ symbol;
            unSubscribeMessage = "unsubscribe=" + topic;
            subscribeMessage = "subscribe=" + topic;
            unSubscribeToTopic(unSubscribeMessage);
            subscribeToTopic(subscribeMessage);
        }
    }
    private void unSubscribeToTopic(String message){
            webSocket.send(message);
        }
    private void subscribeToTopic(String message){
           webSocket.send(message);
    }

}


