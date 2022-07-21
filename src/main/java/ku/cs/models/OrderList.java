package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderList {


    private ArrayList<Order> orders;

    public OrderList() {
        orders = new ArrayList<>();
    }

    public void addOrder(Order order){
        orders.add(order);
        sortLastOrders();
    }

    //Method เรียงออเดอร์ จากออเดอร์ล่าสุดก่อน
    //ใช้อ่านข้อมูล OrderDataSource
    public  void sortLastOrders(){
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if(o1.getTime().isBefore(o2.getTime())) {return 1;}
                if(o1.getTime().isAfter(o2.getTime())) {return -1;}
                return  0;
            }
        });
    }
    public Order getOrderByIndex(int index ){
        return orders.get(index);
    }

    //เปรียบเทียบชื่อร้าน และเอาข้อมูลในร้านนั้นออกมา
    public OrderList orderListInShop(String name){
        OrderList ordersInShop = new OrderList();
        for(Order order : orders){
            if(name.equals(order.getShop())){
                ordersInShop.addOrder(order);
            }
        }
        return ordersInShop;
    }

    public ArrayList<Order> orderListOfUser(String username){
        ArrayList<Order> ordersInShop = new ArrayList<>();
        for(Order order : orders){
            if(username.equals(order.getBuyerUsername())){
                ordersInShop.add(order);
            }
        }
        return ordersInShop;
    }

    public int getSize(){
        return orders.size();
    }

    public String toCsv(){
        String result = "";
        for(int i = orders.size()-1; i>=0; i--){
            result += orders.get(i).toString() + "\n";
        }
        return result;
    }


    public void addTracking(Order orderAddTracking){
        for(Order order : orders ){
            if(order.getTime().equals(orderAddTracking.getTime()) && order.getShop().equals(orderAddTracking.getShop()) && order.getProduct().equals(orderAddTracking.getProduct())){
                orders.set(orders.indexOf(order),orderAddTracking);

            }
        }

    }
}
