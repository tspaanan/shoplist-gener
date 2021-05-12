package shoplistgener.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ingredient encapsulates a single ingredient used in a recipe, and
 * offers methods for manipulating them
 */
public class Ingredient implements Comparable<Ingredient> {
    private String name;
    private Unit unit;
    private Integer requestedQuantity;
    
    /**
     * Constructor
     * @param name name of the ingredient
     * @param unit Enum Type associated with an ingredient (e.g., KG)
     */
    public Ingredient(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
        this.requestedQuantity = null;
    }

    /**
     * Alternative constructor
     * @param name name of the ingredient
     * @param unit Enum Type associated with an ingredient (e.g., KG)
     * @param quantity how much of a given unit the recipe calls for
     */
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

    /**
     * Combines two Ingredient-objects into a single Ingredient-object
     * @param first the first Ingredient-object to be merged
     * @param second the second Ingredient-object to be merged
     * @return Ingredient-object that retains the name and unit of the first, but
     * combines the quantities of both
     */
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
        return this.name + ";" + String.valueOf(this.requestedQuantity) + ";" + this.unit.toString().toLowerCase();
    }

    @Override
    public int compareTo(Ingredient other) {
        return this.name.compareTo(other.name);
    }
    
    /**
     * Returns an alphabetically sorted list of ingredients, in which duplicates have been combined
     * @param ingredients List of Ingredient-objects
     * @return sorted and duplicate-free List of Ingredient-objects
     */
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

    public static List<Ingredient> subtractIngredients(List<Ingredient> original, List<Ingredient> subtract) {
        //O(n^2) algorithm, no optimization required :)
        List<Ingredient> removed = new ArrayList<Ingredient>();
        for (Ingredient ing : subtract) {
            for (Ingredient orig : original) {
                if (ing.equals(orig)) {
                    if (ing.getRequestedQuantity() > orig.getRequestedQuantity()) {
                        removed.add(orig);
                    } else {
                        System.out.println("täällä");
                        System.out.println(orig.getName() + " : " + ing.getName());
                        System.out.println(orig.getRequestedQuantity() + " : " + ing.getRequestedQuantity());
                        int leftoverQuantity = orig.getRequestedQuantity() - ing.getRequestedQuantity();
                        System.out.println(leftoverQuantity);
                        orig.setRequestedQuantity(leftoverQuantity);
                        System.out.println("tulos: " + orig.getRequestedQuantity());
                    }
                }
            }
        }
        for (Ingredient ing : removed) {
            original.remove(ing);
        }
        return Ingredient.sortIngredients(original);
    }
}
