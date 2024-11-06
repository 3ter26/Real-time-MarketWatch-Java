package com.example.tickerchartexercise.helper;

import java.util.Map;

public class UpdateMarketWatchEvent {
    private final Map<String, Object> data;

    public UpdateMarketWatchEvent(Map<String, Object> data){
        this.data =  data;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
