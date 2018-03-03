package com.favis.shopaholic.containers;

import java.util.List;

public class ItemURL {

    private String item_name;
    private String store_name;
    private String url;

    public ItemURL(String item_name, String store_name, String url) {
        this.item_name = item_name;
        this.store_name = store_name;
        this.url = url;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
