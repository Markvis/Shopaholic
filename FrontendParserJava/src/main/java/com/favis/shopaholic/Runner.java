package com.favis.shopaholic;

import java.util.HashMap;

public class Runner {

    public static void main(String... args){
        GetPrices getPrices = new GetPrices();
        HashMap<String, String> pricesMap = getPrices.getAllPrices();
        System.out.println(pricesMap);

//        DatabaseStore databaseStore = new DatabaseStore();
//        databaseStore.storeData();
    }
}