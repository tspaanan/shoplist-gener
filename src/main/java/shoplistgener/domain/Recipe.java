package shoplistgener.domain;

import java.util.List;

/**
 * Recipe encapsulates a single recipe, and
 * offers methods for manipulating them
 */
public class Recipe {
    private String name;
    private String instructions;
    private List<Ingredient> ingredients;
    
    /**
     * Constructor
     * @param name name of the recipe
     * @param instructions instructions for the recipe
     * @param ingredients List of Ingredient-objects required by the recipe
     */
    public Recipe(String name, String instructions, List<Ingredient> ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getName() {
        return this.name;
    }
    
    public void setIngredients(List<Ingredient> changedIngredients) {
        this.ingredients = changedIngredients;
    }

    public void setInstructions(String changedInstructions) {
        this.instructions = changedInstructions;
    }

    public void setName(String changedName) {
        this.name = changedName;
    }

    @Override
    public String toString() {
        return "***" + this.name + "***\n\n" + this.instructions;
    }
}
