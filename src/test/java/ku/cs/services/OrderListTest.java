package ku.cs.services;

import ku.cs.models.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderListTest {
    @Test
    void testAddTrackingNumber(){
        DataBox dataBox = new DataBox();
        System.out.println(dataBox.getOrderList().toCsv());
        Order order = dataBox.getOrderList().getOrderByIndex(1);
        order.changeStatus();
        order.setTrackingNumber("ABC");
        dataBox.getOrderList().addTracking(order);
        assertEquals(order, dataBox.getOrderList().getOrderByIndex(1));
        System.out.println(dataBox.getOrderList().getOrderByIndex(1));
    }

}