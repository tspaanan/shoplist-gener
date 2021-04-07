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
}
