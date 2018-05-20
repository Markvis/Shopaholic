package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.util.DatabaseUtil;
import com.favis.shopaholic.util.Debugger;
import com.favis.shopaholic.util.PropertyReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Shopaholic {

    public static void main(String... args) {
//        PropertyReader.localRun();

        setDebugger();

        DatabaseUtil databaseUtil = new DatabaseUtil();

        System.out.println("Getting items...");
        List<Item> items = databaseUtil.getItems();

        List<ItemHistory> itemHistories;

        if (System.getProperty("threaded").equals("true")) {
            // multi-threading implementation
            System.out.println("Getting prices(multi-thread)...");
            itemHistories = getItemHistories(items);
        } else {
            // Single thread
            System.out.println("Getting prices(single-thread)...");
            WebCrawler webCrawler = new WebCrawler();
            itemHistories = webCrawler.getItemPrices(items);
        }

        System.out.println("Inserting items...");
        long startTime = System.nanoTime();
        databaseUtil.insertItemHistories(itemHistories);
        long endTime = System.nanoTime();
        double totalTime = (double)(endTime - startTime) / 1000000000.0;
        System.out.println("DB insert took " + totalTime + " seconds to complete");

        System.out.println("Comparing current prices...");
        ShopaholicController shopaholicController = new ShopaholicController();
        shopaholicController.checkCurrentValue(items, itemHistories);
    }

    private static List<ItemHistory> getItemHistories(List<Item> items){
        Integer maxThreadCount = Integer.valueOf(System.getProperty("selenium.grid.node.size"));
        ArrayList<ItemHistory> itemHistories = new ArrayList<ItemHistory>();
        CopyOnWriteArrayList<MultiShopaholic> newThreads = new CopyOnWriteArrayList<MultiShopaholic>();
        CopyOnWriteArrayList<MultiShopaholic> startedThreads = new CopyOnWriteArrayList<MultiShopaholic>();
        CopyOnWriteArrayList<MultiShopaholic> completedThreads = new CopyOnWriteArrayList<MultiShopaholic>();

        // create new threads
        for(Integer i = 0; i < items.size(); i++){
            MultiShopaholic multiShopaholic = new MultiShopaholic(items.get(i).getItem_name()+"_thread", items.get(i));
            newThreads.add(multiShopaholic);
        }

        try {
            while(completedThreads.size() != items.size()){
                // start threads and move to started
                while(startedThreads.size() < maxThreadCount && newThreads.size() > 0) {
                    MultiShopaholic multiShopaholic = newThreads.get(0);
                    multiShopaholic.start();
                    startedThreads.add(multiShopaholic);
                    newThreads.remove(multiShopaholic);

                    Debugger.log("newThreads size = " + newThreads.size());
                    Debugger.log("startedThreads size = " + startedThreads.size());
                    Debugger.log("completedThreads size = " + completedThreads.size());
                }

                // move completed threads to completedThread
                for(MultiShopaholic multiShopaholic : startedThreads){
                    if(!multiShopaholic.getThread().isAlive()) {
                        completedThreads.add(multiShopaholic);
                        startedThreads.remove(multiShopaholic);
                    }
                }

                Thread.sleep(1000);
            }

            Debugger.log("newThreads size = " + newThreads.size());
            Debugger.log("startedThreads size = " + startedThreads.size());
            Debugger.log("completedThreads size = " + completedThreads.size());

            assert(completedThreads.size() == items.size());

            // save items
            for(MultiShopaholic multiShopaholic : completedThreads) {
                itemHistories.addAll(multiShopaholic.getItemHistories());
            }

            return itemHistories;
        } catch ( Exception e) {
            e.printStackTrace();
            Debugger.log("Interrupted");
        }

        return null;
    }

    private static void setDebugger(){
        if(System.getProperty("debug.enabled").equals("true")) {
            Debugger.enable();
        }
    }

}
