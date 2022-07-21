package ku.cs.models;

import ku.cs.models.Account;
import ku.cs.models.Shop;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ShopList {
    private ArrayList<Shop> shopArrayList;

    public ShopList(){
        shopArrayList = new ArrayList<Shop>();
    }

    public void addShop(Shop shop){
        shopArrayList.add(shop);
    }

    public Shop getShop(String shopName){
        for (Shop shop:shopArrayList) {
            if(shopName.equals(shop.getName())) {return shop;}
        }
        return null;
    }

    public boolean isExist(String shopName){
        if (getShop(shopName)==null)return false;
        return true;
    }

    public int size(){
        return shopArrayList.size();
    }

    public Shop getIndexOfShop(int index){
        return shopArrayList.get(index);
    }
}