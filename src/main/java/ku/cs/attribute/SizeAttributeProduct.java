package ku.cs.attribute;

public class SizeAttributeProduct implements AttributeProduct<String>{
    private String size;

    public SizeAttributeProduct(String size) {
        this.size = size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String getAttribute() {
        return size;
    }

    @Override
    public String toString() {
        return size;
    }
}
