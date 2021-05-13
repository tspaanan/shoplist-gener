package shoplistgener.domain;

import java.util.ArrayList;
import java.util.List;

import shoplistgener.dao.ShoplistgenerDAO;

/**
 * Application logic residing in-between the UI and the DAO
 */
public class ShoplistgenerService {
    private ShoplistgenerDAO daoHandler;
    private List<Ingredient> shoppingList;
    private List<Recipe> recipeList;

    /**
     * Constructor
     * @param daoHandler injected DAO-object from the UI for accessing data on disk
     */
    public ShoplistgenerService(ShoplistgenerDAO daoHandler) {
        this.daoHandler = daoHandler;
        this.shoppingList = new ArrayList<Ingredient>();
    }

    /**
     * Adds new ingredient to the kitchen
     * @param name name of the ingredient
     * @throws Exception
     */
    public void addIngredientToKitchen(String name) throws Exception {
        if (name.equals("")) {
            throw new Exception("Ingredient has to have a name!");
        }
        this.daoHandler.addIngredientToKitchen(name);
    }

    /**
     * Encapsulates new recipe into a Recipe-object and calls for DAO
     * @param recipeParts name and instructions for the recipe
     * @param ingredientParts List of Ingredient-objects for the recipe
     * @see dao.ShoplistgenerDAO#addRecipe(Recipe)
     * @throws Exception
     */
    public void addRecipe(List<String> recipeParts, List<String> ingredientParts) throws Exception {
        if (recipeParts.get(0).isEmpty()) {
            throw new Exception("Recipe has to have a name!");
        }
        Recipe newRecipe = this.encapsulateRecipe(recipeParts, ingredientParts);
        this.daoHandler.addRecipe(newRecipe);
    }

    private String buildMenuIntoString() {
        this.shoppingList.clear(); //shoppingList is always built anew following changes in recipeList
        StringBuilder menuInString = new StringBuilder();
        for (Recipe rec : this.recipeList) {
            menuInString.append(rec.getName());
            menuInString.append("\n");
            List<Ingredient> ingredients = rec.getIngredients();
            for (Ingredient ing : ingredients) {
                this.shoppingList.add(ing);
            }
        }
        return menuInString.toString();
    }
    
    /**
     * Replaces a single recipe in a previously fetched Menu (of recipes)
     * @param randomized boolean whether to pick new recipe randomly or not
     * @param originalName name of the recipe to be replaced
     * @param replacedName name of the recipe that replaces the old recipe
     * @see dao.ShoplistgenerDAO#fetchRandomRecipe()
     * @see dao.ShoplistgenerDAO#fetchRecipe(String)
     * @return previously fetched Menu with the chosen recipe replaced
     * @throws Exception
     */
    public String changeCourse(boolean randomized, String originalName, String replacedName) throws Exception {
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

    private Recipe encapsulateRecipe(List<String> recipeParts, List<String> ingredientParts) throws Exception {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (String singleIngPart : ingredientParts) {
            String[] ingParts = singleIngPart.split(" ");
            try {
                Integer.valueOf(ingParts[1]);
            } catch (Exception e) {
                throw new Exception("All ingredient quantities have to be numbers!");
            }
            if (ingParts[0].isEmpty()) {
                throw new Exception("All ingredients have to have a name!");
            }
            ingredients.add(new Ingredient(ingParts[0], Unit.valueOf(ingParts[2].toUpperCase()), Integer.valueOf(ingParts[1])));
        }
        return new Recipe(recipeParts.get(0), recipeParts.get(1), ingredients);
    }

    /**
     * Return all ingredients in the database
     * @return ingredients in a String separated by '\n'
     * @throws Exception
     */
    public String fetchAllIngredients() throws Exception {
        List<Ingredient> allIngredients = this.daoHandler.fetchAllIngredients();
        StringBuilder allIngredientsInString = new StringBuilder("All Ingredients:\n\n");
        for (Ingredient ing : allIngredients) {
            allIngredientsInString.append(ing.getName() + " [" + ing.getUnit() + "]");
            allIngredientsInString.append("\n");
        }
        return allIngredientsInString.toString();
    }

    private String fetchAllRecipes() throws Exception {
        List<String> recipeNames = this.daoHandler.fetchAllRecipes();
        StringBuilder recipeNamesInString = new StringBuilder("All Recipes:\n\n");
        for (String recName : recipeNames) {
            recipeNamesInString.append(recName);
            recipeNamesInString.append("\n");
        }
        return recipeNamesInString.toString();
    }

    /**
     * Returns Menu for the week (7 days)
     * @see dao.ShoplistgenerDAO#fetchMenu(int)
     * @return Menu in String-object
     * @throws Exception
     */
    public String fetchCourses() throws Exception {
        this.recipeList = this.daoHandler.fetchMenu(7);
        return this.buildMenuIntoString();
    }

    /**
     * Returns all ingredients in the kitchen
     * @return ingredients in a String separated by '\n'
     * @throws Exception
     */
    public String fetchKitchenIngredients() throws Exception {
        List<Ingredient> kitchenIngredients = this.daoHandler.fetchKitchenIngredients();
        StringBuilder kitchenIngredientsInString = new StringBuilder("Ingredients in Kitchen:\n\n");
        for (Ingredient ing : kitchenIngredients) {
            kitchenIngredientsInString.append(ing.toString().replace(";", " "));
            kitchenIngredientsInString.append("\n");
        }
        return kitchenIngredientsInString.toString();
    }

    /**
     * Returns a single recipe or all recipes from DAO
     * @param name name of the recipe, "" for all recipes
     * @see dao.ShoplistgenerDAO#fetchAllRecipes()
     * @see dao.ShoplistgenerDAO#fetchRecipe(String)
     * @return recipe(s) in a single String-object
     * @throws Exception
     */
    public String fetchRecipe(String name) throws Exception {
        if (name.equals("")) {
            return this.fetchAllRecipes();
        } else {
            name = name.trim();
            Recipe rec = this.daoHandler.fetchRecipe(name);
            StringBuilder recInString = new StringBuilder();
            recInString.append("\n***" + rec.getName() + "***\n\n");
            recInString.append(rec.getInstructions());
            recInString.append("\n\n");
            recInString.append("Ingredients:\n\n");
            for (Ingredient ing : rec.getIngredients()) {
                recInString.append(ing.toString().replace(";", " ") + "\n");
            }
            return recInString.toString();
        }
    }

    /**
     * Returns id of recipe
     * @param name name of the recipe
     * @see dao.ShoplistgenerDAO#fetchRecipeId(String)
     * @return index number
     * @throws Exception
     */
    public int fetchRecipeId(String name) throws Exception {
        return this.daoHandler.fetchRecipeId(name);
    }

    /**
     * Returns a single recipe in List of Strings
     * @param name name of the recipe
     * @see dao.ShoplistgenerDAO#fetchRecipeList(String)
     * @return List of Strings with recipe name at index 0, recipe instructions at index 1,
     * and ingredients separated by "\n" at index 2
     * @throws Exception
     */
    public List<String> fetchRecipeList(String name) throws Exception {
        List<String> recipeInList = new ArrayList<String>();
        Recipe rec = new Recipe("", "", new ArrayList<Ingredient>());
        if (name.equals("Menu:")) {
            rec = new Recipe("Menu: detected", "Please do not select menu heading!", new ArrayList<Ingredient>());
        } else {
            rec = this.daoHandler.fetchRecipe(name);
        }
        recipeInList.add(rec.getName());
        recipeInList.add(rec.getInstructions());
        StringBuilder ingsInString = new StringBuilder();
        for (Ingredient ing : rec.getIngredients()) {
            ingsInString.append(ing.toString().replace(";", " "));
            ingsInString.append("\n");
        }
        recipeInList.add(ingsInString.toString());
        return recipeInList;
    }

    /**
     * Returns shopping list generated from previously fetched Menu
     * @return shopping list in String-object
     */
    public String fetchShoppingList() {
        if (this.shoppingList.isEmpty()) {
            return "";
        }
        List<Ingredient> shoppingListSorted = Ingredient.sortIngredients(this.shoppingList);
        StringBuilder shoppingListinString = new StringBuilder();
        for (Ingredient ing : shoppingListSorted) {
            shoppingListinString.append(ing.toString().replace(";", " "));
            shoppingListinString.append("\n");
        }
        return shoppingListinString.toString();
    }

    /**
     * Encapsulates modified recipe into a Recipe-object and calls for DAO
     * @param recipeParts name and instructions of modified recipe
     * @param ingredientParts List of Ingredient-objects for modified recipe
     * @see dao.ShoplistgenerDAO#modifyRecipe(Recipe)
     * @throws Exception
     */
    public void modifyRecipe(List<String> recipeParts, List<String> ingredientParts) throws Exception {
        Recipe modifiedRecipe = this.encapsulateRecipe(recipeParts, ingredientParts);
        this.daoHandler.modifyRecipe(modifiedRecipe);
    }

    /**
     * Removes ingredient for the kitchen
     * @param name name of the ingredient to be removed
     * @throws Exception
     */
    public void removeIngredientFromKitchen(String name) throws Exception {
        this.daoHandler.removeIngredient(name);
    }

    /**
     * Removes recipe, calls for DAO
     * @param name name of the recipe
     * @see dao.ShoplistgenerDAO#removeRecipe(String)
     * @throws Exception
     */
    public void removeRecipe(String name) throws Exception {
        this.daoHandler.removeRecipe(name.trim());
    }

    public void setRecipeList(List<Recipe> newList) {
        this.recipeList = newList;
    }

    public void setShoppingList(List<Ingredient> newList) {
        this.shoppingList = newList;
    }
    /**
     * Removes ingredients already in the kitchen from the shopping list
     * @return ingredients sans those already in the kitchen as a String-object
     * @throws Exception
     */
    public String subtractKitchenIngredients() throws Exception {
        List<Ingredient> kitchenIngredients = this.daoHandler.fetchKitchenIngredients();
        List<Ingredient> subtractedList = Ingredient.subtractIngredients(Ingredient.sortIngredients(this.shoppingList), kitchenIngredients);
        StringBuilder subtractedListinString = new StringBuilder();
        for (Ingredient ing : subtractedList) {
            subtractedListinString.append(ing.toString().replace(";", " "));
            subtractedListinString.append("\n");
        }
        return subtractedListinString.toString();
    }
    
    /**
     * Updates the quantity of an ingredient in the kitchen
     * @param name name of the ingredient to be updated
     * @param quantity new quantity
     * @throws Exception
     */
    public void updateIngredientQuantityInKitchen(String name, Integer quantity) throws Exception {
        this.daoHandler.updateIngredientQuantityInKitchen(name, quantity);
    }
}