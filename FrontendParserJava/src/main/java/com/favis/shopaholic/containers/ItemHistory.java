package com.favis.shopaholic.containers;

import java.math.BigDecimal;
import java.util.Date;

public class ItemHistory {

    private String item_name;
    private String store_name;
    private BigDecimal item_price;
    private Date date;
    private String url;

    public ItemHistory(String item_name, String store_name, BigDecimal item_price, Date date) {
        this.item_name = item_name;
        this.store_name = store_name;
        this.item_price = item_price;
        this.date = date;
        this.url = null;
    }

    public ItemHistory(String item_name, String store_name, BigDecimal item_price, Date date, String url) {
        this.item_name = item_name;
        this.store_name = store_name;
        this.item_price = item_price;
        this.date = date;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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

    public BigDecimal getItem_price() {
        return item_price;
    }

    public void setItem_price(BigDecimal item_price) {
        this.item_price = item_price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
