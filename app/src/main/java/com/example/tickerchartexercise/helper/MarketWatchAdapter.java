package com.example.tickerchartexercise.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tickerchartexercise.R;
import com.example.tickerchartexercise.model.TradeObject;

import java.util.List;

public class MarketWatchAdapter extends RecyclerView.Adapter<MarketWatchAdapter.TradeViewHolder> {
    Context context;
    private List<TradeObject> tradeObjects;
    public MarketWatchAdapter(Context context, List<TradeObject> tradeobjects){
        this.context = context;
        this.tradeObjects = tradeobjects;
    }



    @NonNull
    @Override
    public TradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cell, parent, false);
        return new TradeViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull TradeViewHolder holder, int position) {
        if (tradeObjects != null && !tradeObjects.isEmpty()) {
            TradeObject tradeobject = tradeObjects.get(position);
            holder.topic.setText(tradeobject.getTopic());
            holder.askPrice.setText(String.valueOf(tradeobject.getAskPrice()));
            holder.lastPrice.setText(String.valueOf(tradeobject.getLastPrice()));
            holder.bidPrice.setText(String.valueOf(tradeobject.getBidPrice()));
            holder.highPrice.setText(String.valueOf(tradeobject.getHighPrice()));        }
    }



    @Override
    public int getItemCount() {
        return tradeObjects.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<TradeObject> tradeObjects) {
        this.tradeObjects = tradeObjects;
        notifyDataSetChanged();
    }
    public TradeObject getItemAt(int position) {
        return tradeObjects.get(position);
    }
    static class TradeViewHolder extends RecyclerView.ViewHolder {
        TextView askPrice, lastPrice, bidPrice, highPrice, topic;

        TradeViewHolder(View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.field_topic);
            askPrice = itemView.findViewById(R.id.field_askPrice);
            lastPrice = itemView.findViewById(R.id.field_lastPrice);
            bidPrice = itemView.findViewById(R.id.field_bidPrice);
            highPrice = itemView.findViewById(R.id.field_highPrice);
        }
    }
}
