package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.util.Debugger;

import java.util.List;

public class MultiShopaholic implements Runnable {
    private Thread t;
    private String threadName;
    private Item item;
    private List<ItemHistory> itemHistories;

    MultiShopaholic(String name, Item item) {
        this.item = item;
        threadName = name;
        Debugger.log("Creating " + threadName);
    }

    public void run() {
        Debugger.log("Getting prices for " + threadName + "...");
        WebCrawler webCrawler = new WebCrawler();
        itemHistories =  webCrawler.getItemPrices(item);
        webCrawler.cleanUp();
        Debugger.log("Thread " +  threadName + " exiting.");
    }

    public void start() {
        Debugger.log("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start();
        }
    }

    public List<ItemHistory> getItemHistories(){
        return itemHistories;
    }

    public Thread getThread(){
        return t;
    }
}
