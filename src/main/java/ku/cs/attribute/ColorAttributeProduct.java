package ku.cs.attribute;

public class ColorAttributeProduct implements AttributeProduct<String > {
    private String color;

    public ColorAttributeProduct(String color) {
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getAttribute() {
        return color;
    }


    @Override
    public String toString() {
        return color;
    }
}
