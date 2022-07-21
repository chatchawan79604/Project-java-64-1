package ku.cs.attribute;

public class HeightAttributeProduct implements AttributeProduct<Double>{
    private double height;

    public HeightAttributeProduct(double height) {
        this.height = height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public Double getAttribute() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("%.2f", height);
    }
}
