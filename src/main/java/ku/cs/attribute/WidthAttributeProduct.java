package ku.cs.attribute;

public class WidthAttributeProduct implements AttributeProduct<Double>{
    private double width;

    public WidthAttributeProduct(double width) {
        this.width = width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public Double getAttribute() {
        return width;
    }

    @Override
    public String toString() {
        return String.format("%.2f", width);
    }
}
