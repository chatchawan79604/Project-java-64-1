package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductList {
    private ArrayList<Product> products;

    public ProductList(){
        products = new ArrayList<>();
    }

    public void addProduct(Product product){
        products.add(product);
        sortLastProducts();
    }

    // get object index i ใช้ในคลาสแลดงสินค้า
    public Product getProduct(int i){
        return products.get(i);
    }

    public int getSize(){
        return products.size();
    }

    // รับชื่อร้านแล้วคืนค่า ProductList ที่มีเฉพาะสินค้าในร้าน ใช้ตอนกดเข้าไปในร้านเพื่อดูสินค้า
    public ProductList getProductsInShop(String nameShop){
        // ไม่มีการเปลี่ยนแปลงข้อมูล
        ProductList productsInShop = new ProductList();
        for(Product product : this.products){
            if( product.isInShop(nameShop)){
                productsInShop.addProduct(product);
            }
        }
        return productsInShop;
    }

    // คืนค่า ProductList ที่เก็บสินค้าเรียงราคาจากน้อยไปมาก
    public ProductList sortProductsAscending(){
        // ไม่มีการเปลี่ยนแปลงข้อมูลใน products (type ArrayList)
        ArrayList<Product> productsAscending = new ArrayList<>(products);
        Collections.sort(productsAscending, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if(o1.getCost() < o2.getCost()) {return -1;}
                if(o1.getCost() > o2.getCost()) {return 1;}
                return 0;
            }
        });
        ProductList productListAscending = new ProductList();
        productListAscending.setProducts(productsAscending);
        return productListAscending;
    }

    // คืนค่า ProductList ที่เก็บสินค้าเรียงราคาจากมากไปน้อย
    public ProductList sortProductsDescending(){
        // ไม่มีการเปลี่ยนแปลงข้อมูล products (type ArrayList)
        ArrayList<Product> productsDescending = new ArrayList<>(products);
        Collections.sort(productsDescending, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if(o1.getCost() < o2.getCost()) {return 1;}
                if(o1.getCost() > o2.getCost()) {return -1;}
                return 0;
            }
        });
        ProductList productListDescending = new ProductList();
        productListDescending.setProducts(productsDescending);
        return productListDescending;
    }

    // คืนค่า ProductList ที่เก็บสินค้าที่มีหมวดหมู่ตาม input ที่รับเข้ามา
    public ProductList sortProductsByCategory(String category){
        ProductList productList = new ProductList();
        for(Product product : products){
            if(product.getProductCategory().getCategory().equals(category)){
                productList.addProduct(product);
            }
        }
        return productList;
    }

    // คืนค่า ProductList ที่เก็บสินค้าเรียงข้อมูลจากวันที่ล่าสุดขึ้นก่อน
    // ใช้ตอนอ่านข้อมูล (ProductDataSource)
    public void sortLastProducts(){
        // มีการเปลี่ยนแปลงข้อมูล products (type ArrayList)
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if(o1.getTimeAdded().isBefore(o2.getTimeAdded())) {return 1;}
                if(o1.getTimeAdded().isAfter(o2.getTimeAdded())) {return -1;}
                return 0;
            }
        });
    }

    // คืนค่า ProductList ที่เก็บสินค้าที่มีช่วงราคา max และ min เรียงข้อมูลจากวันที่ล่าสุดขึ้นก่อน
    public ProductList sortRangeCostProduct(double max, double min){
        // ไม่มีการเปลี่ยนแปลงข้อมูล products (type ArrayList)
        ArrayList<Product> productsSetRange = new ArrayList<>();
        for(int i=0; i< products.size(); i++){
            if(products.get(i).getCost() <= max && products.get(i).getCost() >= min){
                productsSetRange.add(products.get(i));
            }
        }
        ProductList productListSortRangeCost = new ProductList();
        productListSortRangeCost.setProducts(productsSetRange);
        return productListSortRangeCost;
    }

    // คืนค่าราคาสินค้าที่มีราคาสูงสุด
    public double maxCostProduct(){
        double max = 0;
        for(Product product : products){
            if(product.getCost() > max){
                max = product.getCost();
            }
        }
        return max;
    }

    // คืนค่า String สินค้าทั้งหมด shopName,productName,productDetail,picturePath,cost,quantity,time,serialNumber
    public String toCsv(){
        String result = "";
        for(int i = products.size()-1; i>=0; i--){
            result += products.get(i).toCsv() + "\n";
        }
        return result;
    }

    // copy ProductList เอาไว้ใช้ตอนแสดงสินค้า-->จะมี tmp ไว้เก็บ ProductList ที่จัดเรียงแบบต่างๆแล้วเอาแสดง แต่ไม่ copy object Product
    public ProductList copyProductList(){
        ProductList productListCopy = new ProductList();
        productListCopy.setProducts(new ArrayList<>(products));
        return productListCopy;
    }
    // ใช้ตอน copy ProductList
    private void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Product getProductBySerialNumber(String serialNumber){
        for (Product product:products){
            if(product.getSerialnumber().equals(serialNumber)){
                return product;
            }
        }
        return null;
    }
}
