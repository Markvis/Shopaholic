package com.favis.shopaholic.containers;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Item implements Comparable<Item>, Comparator<Item> {

    private String item_name;
    private List<ItemURL> item_urls;
    private String item_desc;
    private BigDecimal item_msrp;
    private BigDecimal item_min_price;
    private BigDecimal item_max_price;
    private BigDecimal item_latest_price;
    private String item_min_store_name;

    public Item(String item_name) {
        this.item_name = item_name;
        this.item_urls = null;
    }

    public Item(String item_name, List<ItemURL> item_urls) {
        this.item_name = item_name;
        this.item_urls = item_urls;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public BigDecimal getItem_msrp() {
        return item_msrp;
    }

    public void setItem_msrp(BigDecimal item_msrp) {
        this.item_msrp = item_msrp;
    }

    public BigDecimal getItem_min_price() {
        return item_min_price;
    }

    public void setItem_min_price(BigDecimal item_min_price) {
        this.item_min_price = item_min_price;
    }

    public BigDecimal getItem_max_price() {
        return item_max_price;
    }

    public void setItem_max_price(BigDecimal item_max_price) {
        this.item_max_price = item_max_price;
    }

    public BigDecimal getItem_latest_price() {
        return item_latest_price;
    }

    public void setItem_latest_price(BigDecimal item_latest_price) {
        this.item_latest_price = item_latest_price;
    }

    public String getItem_min_store_name() {
        return item_min_store_name;
    }

    public void setItem_min_store_name(String item_min_store_name) {
        this.item_min_store_name = item_min_store_name;
    }

    public List<ItemURL> getItem_urls() {
        return item_urls;
    }

    public void setItem_urls(List<ItemURL> item_urls) {
        this.item_urls = item_urls;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    @Override
    public int compare(Item o1, Item o2) {
        return o1.getItem_name().compareTo(o2.getItem_name());
    }

    @Override
    public int compareTo(Item o) {
        return item_name.compareTo(o.getItem_name());
    }
}
