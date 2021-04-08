package defaultPackage.domain;

public class Ingredient implements Comparable<Ingredient> {
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
    public Unit getUnit() {return this.unit;}
    public Integer getRequestedQuantity() {return this.requestedQuantity;}

    public void setRequestedQuantity(Integer quantity) {
        this.requestedQuantity = quantity;
    }

    public static Ingredient combineIngredients(Ingredient first, Ingredient second) {
        return new Ingredient(first.name, first.unit, first.requestedQuantity + second.requestedQuantity);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {return true;}
        if (!(other instanceof Ingredient)) {return false;}
        Ingredient otherIngredient = (Ingredient) other;
        if (this.name.equals(otherIngredient.name)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name + ", " + String.valueOf(this.requestedQuantity) + " " + this.unit.toString().toLowerCase();
    }

    @Override
    public int compareTo(Ingredient other) {
        return this.name.compareTo(other.name);
    }
}
