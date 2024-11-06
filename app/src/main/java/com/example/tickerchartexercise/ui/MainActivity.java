package com.example.tickerchartexercise.ui;

import android.os.Bundle;

import com.example.tickerchartexercise.R;
import com.example.tickerchartexercise.datasource.WebSocketService;
import com.example.tickerchartexercise.helper.MarketWatchAdapter;
import com.example.tickerchartexercise.model.TradeObject;
import com.example.tickerchartexercise.datasource.TradeObjectService;
import com.example.tickerchartexercise.helper.UpdateMarketWatchEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MarketWatchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recycler_view);
        recyclerView = findViewById(R.id.recycler_view);
        initMarketWatch();
        //fetchTradeObjects(); - related to 1st assessment-requirement
        connectToStreamer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateMarketWatchEvent event) {
        updateMarketWatch(event.getData());
    }

    private void updateMarketWatch(Map<String, Object> data) {
        String topic = (String) data.get("topic");
        for(int i = 0; i< adapter.getItemCount();i++) {
        TradeObject item = adapter.getItemAt(i);
        if(item.getTopic().equals(topic)) {
                if (data.containsKey("askprice")) {
                item.setAskPrice((String) data.get("askprice"));
                }
                if(data.containsKey("lasttradeprice")) {
                item.setLastPrice((String) data.get("lasttradeprice"));
                }
                if(data.containsKey("high")) {
                item.setHighPrice((String) data.get("high"));
                }
                if(data.containsKey("bidprice")){
                item.setBidPrice((String) data.get("bidprice"));
                }
            adapter.notifyItemChanged(i);
            break;
        }

        }
    }

    private void initMarketWatch() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TradeObject> tradeObjects = new ArrayList<>();
        for(String symbol: WebSocketService.getInstance().symbols) {
            tradeObjects.add(new TradeObject("QO."+symbol));
        }
        adapter = new MarketWatchAdapter(this, tradeObjects);
        recyclerView.setAdapter(adapter);
        /* *The below two lines is for the 1st assessment req**/
//        adapter = new TradeObjectAdapter(this, new ArrayList<>());
//        recyclerView.setAdapter(adapter);
    }
    private void fetchTradeObjects() {
        TradeObjectService.getInstance().fetchTradeObjects(new TradeObjectService.OnTradeObjectsReceivedListener() {
            @Override
            public void onTradeObjectsReceived(List<TradeObject> tradeObjects) {
                adapter.updateData(tradeObjects);
            }
        });
    }

    private void connectToStreamer() {
        WebSocketService.getInstance().connectToWebSocket();
    }
}