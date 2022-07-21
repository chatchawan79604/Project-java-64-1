package ku.cs.attribute;

public class LengthAttributeProduct implements AttributeProduct<Double>{
    private double length;

    public LengthAttributeProduct(double length) {
        this.length = length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public Double getAttribute() {
        return length;
    }

    @Override
    public String toString() {
        return String.format("%.2f", length);
    }
}
