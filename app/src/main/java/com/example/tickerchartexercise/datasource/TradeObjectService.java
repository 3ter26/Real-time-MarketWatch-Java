package com.example.tickerchartexercise.datasource;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.tickerchartexercise.model.TradeObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TradeObjectService{
    public interface OnTradeObjectsReceivedListener {
        void onTradeObjectsReceived(List<TradeObject> tradeObjects);
    }
    private static TradeObjectService instance;
    private final OkHttpClient client;
    private TradeObjectService(){
        client = new OkHttpClient();
    }
    public static synchronized TradeObjectService getInstance(){
        if(instance == null){
            instance = new TradeObjectService();
        }
        return instance;
    }
    public void fetchTradeObjects(OnTradeObjectsReceivedListener listener){
        okhttp3.Request get = new Request.Builder().url(LoaderLinksService.getTradeObjectsLink()).build();
        client.newCall(get).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String responseBody = response.body().string();
                    System.out.println("HTTP CONNECTION SUCCEEDED: "+responseBody);
                    try {
                        List<TradeObject> tradeObjects = parseTradeObjectsFromJSON(responseBody);
                        if(listener!=null){
                            new Handler(Looper.getMainLooper()).post(() -> {
                                listener.onTradeObjectsReceived(tradeObjects);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("HTTP CONNECTION failed: " + e.getMessage());
                e.printStackTrace();
            }

        });
    }
    private List<TradeObject> parseTradeObjectsFromJSON(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<TradeObject> tradeObjects = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String askPrice = jsonObject.getString("ask-price");
            String lastPrice = jsonObject.getString("last-price");
            String bidPrice = jsonObject.getString("bid-price");
            String highPrice = jsonObject.getString("high-price");

            TradeObject tradeObject = new TradeObject(name, askPrice, lastPrice, bidPrice, highPrice);
            tradeObjects.add(tradeObject);
        }
        return tradeObjects;
    }
}
