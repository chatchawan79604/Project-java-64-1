package ku.cs.attribute;

public class ModelAttributeProduct implements AttributeProduct<String>{
    private String model;

    public ModelAttributeProduct(String model) {
        this.model = model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getAttribute() {
        return model;
    }

    @Override
    public String toString() {
        return model;
    }
}
