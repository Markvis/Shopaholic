package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;

public class ShopaholicController {

    public void checkCurrentValue(List<Item> items, List<ItemHistory> itemHistories) {
        Collections.sort(items);

        for (ItemHistory itemHistory : itemHistories) {
//            Item item = getItem(items, itemHistory.getItem_name());
            Integer index = Collections.binarySearch(items, new Item(itemHistory.getItem_name()));
            Item item = items.get(index);

            // set latest price
            item.setItem_latest_price(itemHistory.getItem_price());

            // set min and storename
            if(itemHistory.getItem_price().compareTo(item.getItem_min_price()) < 0){
                item.setItem_min_price(itemHistory.getItem_price());
                item.setItem_min_store_name(itemHistory.getStore_name());
            }
            // set max price
            if(itemHistory.getItem_price().compareTo(item.getItem_max_price()) > 0) {
                item.setItem_max_price(itemHistory.getItem_price());
            }
            // Last if statement
            if(itemHistory.getItem_price().compareTo(item.getItem_msrp()) < 0){
                sendMail(itemHistory);
            }
        }

    }

    private Item getItem(List<Item> items, String item_name) {

        for (Item item : items) {
            if (item.getItem_name().equals(item_name))
                return item;
        }

        return null;
    }

    private void sendMail(ItemHistory itemHistory){
        try {
            GoogleMail.Send(PropertyReader.getProperty("email.username"),
                    PropertyReader.getProperty("email.app.password"),
                    "favismark@gmail.com",
                    "Shopaholic: " + itemHistory.getItem_name() + " $" + itemHistory.getItem_price() + " @ " + itemHistory.getStore_name(),
                    itemHistory.getUrl());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendTestMail() {
        try {
            GoogleMail.Send(PropertyReader.getProperty("email.username"),
                    PropertyReader.getProperty("email.app.password"),
                    "favismark@gmail.com",
                    "TEST MAIL",
                    "TEST MAIL");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
