package com.example.tickerchartexercise.model;

public class TradeObject {
 private String name;
 private String askPrice;
 private String lastPrice;
 private String bidPrice;
 private String highPrice;
 private String topic;
 public TradeObject(String name, String askPrice, String lastPrice, String bidPrice, String highPrice){
    this.name = name;
    this.askPrice = askPrice;
    this.lastPrice = lastPrice;
    this.bidPrice = bidPrice;
    this.highPrice = highPrice;
 }
 public TradeObject(String topic){
     //this.name = "-";
     this.topic = topic;
     this.askPrice = "-";
     this.lastPrice = "-";
     this.bidPrice = "-";
     this.highPrice = "-";
 }

    public void setName(String name) {
        this.name = name;
    }
    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }
    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }
    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }
    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }
    public String getAskPrice() {
        return askPrice;
    }
    public String getLastPrice() {
        return lastPrice;
    }
    public String getBidPrice() {
        return bidPrice;
    }
    public String getHighPrice() {
        return highPrice;
    }
    public String getTopic() {return topic;}
}
