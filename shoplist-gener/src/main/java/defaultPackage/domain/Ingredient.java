package defaultPackage.domain;

public class Ingredient {
    private String name;
    private Unit unit;
    private Integer requestedQuantity;
    
    public Ingredient(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
        this.requestedQuantity = null;
    }

    public Ingredient(String name, Unit unit, Integer quantity) {
        this(name, unit);
        this.setRequestedQuantity(quantity);
    }

    public String getName() {return this.name;}

    public void setRequestedQuantity(Integer quantity) {
        this.requestedQuantity = quantity;
    }

    @Override
    public String toString() {
        return this.name + ", " + String.valueOf(this.requestedQuantity) + " " + this.unit.toString().toLowerCase();
    }
}
