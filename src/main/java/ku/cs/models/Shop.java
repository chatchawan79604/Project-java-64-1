package ku.cs.models;

import ku.cs.services.ProductsDataSource;
import ku.cs.utility.Number;

public class Shop {
    private String name;
    private ProductList products = new ProductList();
    private OrderList orderList;
    private int lowValue;
    private ProductsDataSource productsDataSource = new ProductsDataSource();

    public Shop(String shopName){ //Constructor รับชื่อร้าน โดยมีค่าแจ้งเตือนระดับสินค้าต่ำตั้งไว้ที่ 10
        this.name = shopName;
        lowValue = 10; //Default value
    }

    public String getName() {
        return name;
    }

    public int getLowValue() {
        return lowValue;
    }

    public void setProducts(ProductList products) {
        this.products = products;
    }

    public Product getProductOfIndex(int i){
        return products.getProduct(i);
    }

    public int getSizeOfProductList(){
        return products.getSize();
    }

    public void sellProduct(String product,int quantity){
        for(int index = 0;index < products.getSize();index++){
            if(products.getProduct(index).getName().equals(product)){
                products.getProduct(index).setQuantity(products.getProduct(index).getQuantity()-quantity);
            }
        }
    }

    public void addProduct(Product productAdd){ //สำหรับเพิ่มสินค้าในร้าน
        this.products.addProduct(productAdd);
    }

    public void setLowValue(int lowValue) {
        this.lowValue = lowValue;
    }

    public Order getOrderOfIndex(int index){
        return orderList.getOrderByIndex(index);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int checkProduct(String nameProduct, String detail, String cost, String quantity){
        // check empty fields
        if(nameProduct.trim().isEmpty() || detail.trim().isEmpty() || cost.trim().isEmpty() || quantity.trim().isEmpty() ) {
            return 1;
        }
        else if(!Number.checkDoubleNumber(cost) || Double.parseDouble(cost) <=0 ){
            return 2;
        }
        else if(!Number.checkIntegerNumber(quantity) || Integer.parseInt(quantity )<=0 ){
            return 3;
        }

        return 0;
    }

    public int checkAttributeProduct(String[] inputAttribute){
        if(inputAttribute.length > 0){
            for(String attribute : inputAttribute){
                if(attribute == null || attribute.trim().isEmpty()) return 1;
            }
        }
        return 0;
    }

    public void setOrderList(OrderList orderList){
        this.orderList = orderList;
    }

    public int getSizeOrderList(){
        return orderList.getSize();
    }
}