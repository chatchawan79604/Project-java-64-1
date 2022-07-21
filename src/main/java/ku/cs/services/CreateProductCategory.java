package ku.cs.services;

import ku.cs.attribute.*;
import ku.cs.models.ProductCategory;
import ku.cs.utility.Number;

import java.io.IOException;

public class CreateProductCategory {

    ProductCategory productCategory;

    public CreateProductCategory(String category) {
        productCategory = new ProductCategory(category);
    }

    public void addAttribute(String attribute, String data) throws NumberFormatException {

        if(attribute.equals("ความจุ")){
            productCategory.addAttributeList( new CapacityAttributeProduct(data) );
        }
        else if(attribute.equals("สี")){
            productCategory.addAttributeList( new ColorAttributeProduct(data) );
        }
        else if(attribute.equals("เส้นผ่านศูนย์กลาง(ซม.)")){
            productCategory.addAttributeList( new DiameterAttributeProduct(Double.parseDouble(data)) );
        }
        else if(attribute.equals("ความสูง(ซม.)")){
            productCategory.addAttributeList( new HeightAttributeProduct(Double.parseDouble(data)) );
        }
        else if(attribute.equals("ความยาว(ซม.)")){
            productCategory.addAttributeList( new LengthAttributeProduct(Double.parseDouble(data)) );
        }
        else if (attribute.equals("ชื่อรุ่น")){
            productCategory.addAttributeList( new ModelAttributeProduct(data) );
        }
        else if(attribute.equals("ไซต์")){
            productCategory.addAttributeList( new SizeAttributeProduct(data) );
        }
        else if(attribute.equals("น้ำหนัก(กรัม)")){
            productCategory.addAttributeList( new WeightAttributeProduct(Double.parseDouble(data)) );
        }
        else if(attribute.equals("ความกว้าง(ซม.)")){
            productCategory.addAttributeList( new WidthAttributeProduct(Double.parseDouble(data)) );
        }
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }
}
