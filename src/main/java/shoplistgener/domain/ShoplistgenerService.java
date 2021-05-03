package shoplistgener.domain;

import java.util.ArrayList;
import java.util.List;

import shoplistgener.dao.ShoplistgenerDAO;

public class ShoplistgenerService {
    private ShoplistgenerDAO daoHandler;
    private List<Ingredient> shoppingList;
    private List<Recipe> recipeList;

    public ShoplistgenerService(ShoplistgenerDAO daoHandler) {
        this.daoHandler = daoHandler;
        this.shoppingList = new ArrayList<Ingredient>();
    }

    public boolean addRecipe(List<String> recipeParts) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (int i = 2; i < recipeParts.size(); i++) {
            String[] ingParts = recipeParts.get(i).split(";");
            ingredients.add(new Ingredient(ingParts[0], Unit.valueOf(ingParts[1].toUpperCase()), Integer.valueOf(ingParts[2])));
        }
        Recipe newRecipe = new Recipe(recipeParts.get(0), recipeParts.get(1), ingredients);
        try {
            this.daoHandler.addRecipe(newRecipe);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //TODO: refactor with addRecipe above
    public void modifyRecipe(List<String> recipeParts) throws Exception {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (int i = 2; i < recipeParts.size(); i++) {
            String[] ingParts = recipeParts.get(i).split(";");
            ingredients.add(new Ingredient(ingParts[0], Unit.valueOf(ingParts[1].toUpperCase()), Integer.valueOf(ingParts[2])));
        }
        Recipe modifiedRecipe = new Recipe(recipeParts.get(0), recipeParts.get(1), ingredients);
        this.daoHandler.modifyRecipe(modifiedRecipe);
    }

    public String fetchRecipe(String name) throws Exception {
        if (name.equals("")) {
            List<String> recipeNames = this.daoHandler.fetchAllRecipes();
            StringBuilder recipeNamesInString = new StringBuilder("All Recipes:\n\n");
            for (String recName : recipeNames) {
                recipeNamesInString.append(recName);
                recipeNamesInString.append("\n");
            }
            return recipeNamesInString.toString();
        } else {
            name = name.trim();
            //.toLowerCase(); IDEA: maybe everything in the database should be lowercase, and domain could change them to be grammatically correct before passing them to UI
            Recipe rec = this.daoHandler.fetchRecipe(name);
            StringBuilder recInString = new StringBuilder();
            recInString.append("\n***" + rec.getName() + "***\n\n");
            recInString.append(rec.getInstructions());
            recInString.append("\n\n");
            recInString.append("Ingredients:\n\n");
            for (Ingredient ing : rec.getIngredients()) {
                recInString.append(ing.toString() + "\n");
            }
            return recInString.toString();
        }
    }

    public List<String> fetchRecipeList(String name) throws Exception {
        List<String> recipeInList = new ArrayList<String>();
        Recipe rec = this.daoHandler.fetchRecipe(name);
        recipeInList.add(rec.getName());
        recipeInList.add(rec.getInstructions());
        StringBuilder ingsInString = new StringBuilder();
        for (Ingredient ing : rec.getIngredients()) {
            ingsInString.append(ing.toString());
            ingsInString.append("\n");
        }
        recipeInList.add(ingsInString.toString());
        return recipeInList;
    }

    public int fetchRecipeId(String name) {
        return this.daoHandler.fetchRecipeId(name);
    }

    public Recipe fetchRecipeObject(String name) throws Exception {
        return this.daoHandler.fetchRecipe(name);
    }
    
    public String fetchCourses() throws Exception {
        this.recipeList = this.daoHandler.fetchMenu(7);
        return this.buildMenuIntoString();
    }

    private String buildMenuIntoString() {
        this.shoppingList.clear(); //shoppingList is always built anew following changes in recipeList
        StringBuilder menuInString = new StringBuilder();
        for (Recipe rec : this.recipeList) {
            menuInString.append(rec.getName());
            menuInString.append("\n");
            //recipe instructions are not included at this point
            //recToString.add(rec.getInstructions());
            List<Ingredient> ingredients = rec.getIngredients(); //TODO: move this ingredients-related functionality somewhere else, maybe?
            for (Ingredient ing : ingredients) {
                this.shoppingList.add(ing);
            }
        }
        return menuInString.toString();
    }
    
    public String changeCourse(String originalName, boolean randomized, String replacedName) throws Exception {
        Recipe newRecipe = new Recipe("", "", new ArrayList<Ingredient>());
        if (randomized) {
            newRecipe = this.daoHandler.fetchRandomRecipe();
        } else {
            newRecipe = this.daoHandler.fetchRecipe(replacedName);
        }
        for (Recipe recipe : this.recipeList) {
            if (recipe.getName().equals(originalName)) {
                recipe.setName(newRecipe.getName());
                recipe.setInstructions(newRecipe.getInstructions());
                recipe.setIngredients(newRecipe.getIngredients());
            }
        }
        return this.buildMenuIntoString();
    }

    public String fetchShoppingList() {
        if (this.shoppingList.isEmpty()) {
            return "";
        }
        List<Ingredient> shoppingListSorted = Ingredient.sortIngredients(this.shoppingList);
        StringBuilder shoppingListinString = new StringBuilder();
        for (Ingredient ing : shoppingListSorted) {
            shoppingListinString.append(ing.toString());
            shoppingListinString.append("\n");
        }
        return shoppingListinString.toString();
    }
    
    public void removeRecipe(String name) throws Exception {
        this.daoHandler.removeRecipe(name);
    }
}