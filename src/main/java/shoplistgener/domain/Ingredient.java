package shoplistgener.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getName() {
        return this.name;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public Integer getRequestedQuantity() {
        return this.requestedQuantity;
    }

    public void setRequestedQuantity(Integer quantity) {
        this.requestedQuantity = quantity;
    }

    public static Ingredient combineIngredients(Ingredient first, Ingredient second) {
        return new Ingredient(first.name, first.unit, first.requestedQuantity + second.requestedQuantity);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Ingredient)) {
            return false;
        }
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
    
    public static List<Ingredient> sortIngredients(List<Ingredient> ingredients) {
        //solve edge case that came up with unit testing:
        if (ingredients.size() <= 1) {
            return ingredients;
        }
        //TODO: change this stream into one-line sorted()
        List<Ingredient> ingredientsSorted = ingredients.stream()
                                                //.distinct() not useful here
                                                .sorted()
                                                .collect(Collectors.toCollection(ArrayList::new));
        List<Ingredient> combinedIngredients = new ArrayList<Ingredient>();
        Ingredient previous = new Ingredient("", Unit.CL, 0);
        for (int i = 1; i < ingredientsSorted.size(); i++) {
            if (ingredientsSorted.get(i).equals(ingredientsSorted.get(i - 1))) {
                if (ingredientsSorted.get(i).equals(previous)) {
                    combinedIngredients.get(combinedIngredients.size() - 1).setRequestedQuantity(ingredientsSorted.get(i).getRequestedQuantity() + combinedIngredients.get(combinedIngredients.size() - 1).getRequestedQuantity());
                    //TODO: fix the above line so that it can be understood by non-machines
                    continue;
                }
                combinedIngredients.add(Ingredient.combineIngredients(ingredientsSorted.get(i), ingredientsSorted.get(i - 1)));
                previous = ingredientsSorted.get(i - 1);
            } else {
                if (!combinedIngredients.contains(ingredientsSorted.get(i - 1))) {
                    combinedIngredients.add(ingredientsSorted.get(i - 1));
                }
            }
        }
        //add the last ingredient only if not duplicate
        if (!ingredientsSorted.get(ingredientsSorted.size() - 1).equals(combinedIngredients.get(combinedIngredients.size() - 1))) {
            combinedIngredients.add(ingredientsSorted.get(ingredientsSorted.size() - 1));
        }
        return combinedIngredients;
    }
}
