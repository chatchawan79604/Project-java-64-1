package ku.cs.services;

import ku.cs.models.*;
import ku.cs.models.Report;
import ku.cs.models.ReportList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBox {
    private Account currentlyAccount;
    private Shop currentlyShop;
    private AccountList accountList;
    private ProductList productList;
    private ShopList shopList;
    private ReportList<Product> productReports;
    private ReportList<Review> reviewReports;
    private OrderList orderList;
    private Product selectedProduct;
    private HashMap<String , ArrayList> category;
    private Report<Review> reviewReport;
    private Report<Product> productReport;
    private BlacklistAccountList blacklistAccountList;

    public DataBox(){
        DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
        DataSource<ShopList> shopDataSource = new ShopDataSource("data","shop_data.csv");
        DataSource<ProductList> productsDataSource = new ProductsDataSource();
        DataSource<OrderList> orderDataSource = new OrderDataSource();
        DataSource<HashMap<String,ArrayList>> categoryDataSourceDataSource = new CategoryDataSource();
        DataSource<ReportList<Product>> productReportDataSource = new ProductReportDataSource("data","reported_product.csv");
        DataSource<ReportList<Review>> reviewReportDataSource = new ReviewReportDataSource("data","reported_review.csv");
        DataSource<BlacklistAccountList> blacklistAccountListDataSource = new BlacklistDataSource("data","blacklist_account.csv");
        orderList = orderDataSource.readData();
        accountList = accountDataSource.readData();
        shopList = shopDataSource.readData();
        productList = productsDataSource.readData();
        category = categoryDataSourceDataSource.readData();
        productReports = productReportDataSource.readData();
        reviewReports = reviewReportDataSource.readData();
        blacklistAccountList = blacklistAccountListDataSource.readData();
        currentlyAccount = null;
        selectedProduct = null;
        reviewReport = null;
        productReport = null;
        // set reviewList to product
        for(int i=0; i<productList.getSize(); i++){
            Product product = productList.getProduct(i);

            DataSource<ReviewList> reviewDataSource = new ReviewDataSource("data" + File.separator + "review", String.valueOf(product.getSerialnumber()) + ".csv");
            ReviewList reviewList = reviewDataSource.readData();

            product.setReviewList(reviewList);
        }
        // set productList to shop
        for(int i=0; i<shopList.size(); i++){
            Shop shop = shopList.getIndexOfShop(i);
            shop.setProducts(productList.getProductsInShop(shop.getName()));
        }
    }

    public Account getCurrentlyAccount() {
        return currentlyAccount;
    }

    public AccountList getAccountList() {
        return accountList;
    }

    public ProductList getProductList() {
        return productList;
    }

    public ProductList getProductListFiltered(User account) {
        ProductList filteredProductList = new ProductList();
        ArrayList<String> hiddenShopName = blacklistAccountList.getBlacklistShopName();
        if(account.getShop() != null)
            hiddenShopName.add(account.getShop().getName());
        for(int productIndex = 0 ;productIndex < productList.getSize();productIndex++ ){
            if(!hiddenShopName.contains(productList.getProduct(productIndex).getShop())){
                filteredProductList.addProduct(productList.getProduct(productIndex));
            }
        }
        return filteredProductList;
    }


    public ShopList getShopList() {
        return shopList;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public Shop getCurrentlyShop() {
        return currentlyShop;
    }

    public HashMap<String, ArrayList> getCategory() {
        return category;
    }

    public void setCurrentlyAccount(Account currentlyAccount) {
        this.currentlyAccount = currentlyAccount;
    }

    public void setCurrentlyShop(Shop shop){
        this.currentlyShop = shop;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public OrderList getOrderList() {
        return orderList;
    }

    public void updateOrderList() {
        this.orderList = new OrderDataSource().readData();
    }

    public ReportList<Product> getProductReports() {
        return productReports;
    }

    public ReportList<Review> getReviewReports() {
        return reviewReports;
    }

    public void setReviewReport(Report<Review> reviewReport) {
        this.reviewReport = reviewReport;
    }

    public void setProductReport(Report<Product> productReport) {
        this.productReport = productReport;
    }

    public Report<Review> getReviewReport() {
        return reviewReport;
    }

    public Report<Product> getProductReport() {
        return productReport;
    }

    public BlacklistAccountList getBlacklistAccountList() {
        return blacklistAccountList;
    }

    public void updateReportedReviewList(){
        DataSource<ReportList<Review>> reviewReportDataSource = new ReviewReportDataSource("data","reported_review.csv");
        this.reviewReports = reviewReportDataSource.readData();
    }

    public void updateBlacklistAccount(){
        DataSource<BlacklistAccountList> blacklistAccountListDataSource = new BlacklistDataSource("data","blacklist_account.csv");
        this.blacklistAccountList = blacklistAccountListDataSource.readData();
    }
}
