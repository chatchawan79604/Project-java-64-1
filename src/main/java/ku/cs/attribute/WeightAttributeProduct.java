package ku.cs.attribute;

public class WeightAttributeProduct implements AttributeProduct<Double>{

    private double weight;

    public WeightAttributeProduct(double weight) {
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Double getAttribute() {
        return weight;
    }

    @Override
    public String toString() {
        return String.format("%.2f", weight);
    }
}
