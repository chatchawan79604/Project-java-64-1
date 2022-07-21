package ku.cs.models;

import ku.cs.attribute.AttributeProduct;

import java.util.ArrayList;

public class ProductCategory {
    private String category;
    private ArrayList<AttributeProduct> attributeProducts;

    public ProductCategory(String category) {
        this.category = category;
        attributeProducts = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void addAttributeList(AttributeProduct attributeProduct){
        this.attributeProducts.add(attributeProduct);
    }

    public AttributeProduct getAttributeProductListByIndex(int i){
        return attributeProducts.get(i);
    }

    public int getSizeAttributeProducts(){
        return attributeProducts.size();
    }

    public String toCsv(){
        String result = "," + category;
        for(AttributeProduct attributeProduct : this.attributeProducts){
            result += "," + attributeProduct.getAttribute();
        }
        return result;
    }


}
