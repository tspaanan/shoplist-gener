package defaultPackage.domain;

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

    public String getName() {return this.name;}
    public String getInstructions() {return this.instructions;}
    public List<Ingredient> getIngredients() {return this.ingredients;}

    @Override
    public String toString() {
        return "***" + this.name + "***\n\n" + this.instructions;
    }
}
