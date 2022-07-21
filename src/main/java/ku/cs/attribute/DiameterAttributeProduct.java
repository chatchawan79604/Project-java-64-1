package ku.cs.attribute;

public class DiameterAttributeProduct implements AttributeProduct<Double>{
    private double diameter;

    public DiameterAttributeProduct(double diameter) {
        this.diameter = diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    @Override
    public Double getAttribute() {
        return diameter;
    }

    @Override
    public String toString() {
        return String.format("%.2f", diameter);
    }
}
