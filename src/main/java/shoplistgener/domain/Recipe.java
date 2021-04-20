package shoplistgener.domain;

import java.util.List;

public class Recipe {
    private String name;
    private String instructions;
    private List<Ingredient> ingredients;
    
    public Recipe(String name, String instructions, List<Ingredient> ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public String getName() {
        return this.name;
    }
    
    public String getInstructions() {
        return this.instructions;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setName(String changedName) {
        this.name = changedName;
    }

    public void setInstructions(String changedInstructions) {
        this.instructions = changedInstructions;
    }

    public void setIngredients(List<Ingredient> changedIngredients) {
        this.ingredients = changedIngredients;
    }

    @Override
    public String toString() {
        return "***" + this.name + "***\n\n" + this.instructions;
    }
}
