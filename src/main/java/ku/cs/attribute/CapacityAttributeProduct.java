package ku.cs.attribute;

public class CapacityAttributeProduct implements AttributeProduct<String>{
    private String capacity;

    public CapacityAttributeProduct(String capacity) {
        this.capacity = capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String getAttribute() {
        return capacity;
    }

    @Override
    public String toString() {
        return capacity;
    }
}
