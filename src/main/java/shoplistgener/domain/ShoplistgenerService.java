package shoplistgener.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.sqlite.SQLiteException;

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
        for (int i = 2; i < recipeParts.size(); i++ ) {
            String[] ingParts = recipeParts.get(i).split(";");
            ingredients.add(new Ingredient(ingParts[0], Unit.valueOf(ingParts[1].toUpperCase()), Integer.valueOf(ingParts[2])));
        }
        Recipe newRecipe = new Recipe(recipeParts.get(0), recipeParts.get(1), ingredients);
        try {
            this.daoHandler.addRecipe(newRecipe);
            return true;
        } catch (SQLiteException el) { //TODO: tämä on hoidettu toisella tavalla DAOn toimesta, voinee poistaa kokonaan
            if (el.getErrorCode() == 19) {
                // errorCode 19 refers to UNIQUE-constraint violation, which is expected with ingredients
                return true;
            }
            return false;
        } catch (UnsupportedOperationException uoe) {
            //this exception is raised if a recipe by that name already exists
            return false;
        } catch (Exception e) {
            System.out.println("tämä exc laukesi!");
            e.printStackTrace();
            return false;
        }
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
            //.toLowerCase();
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
    
    public List<String> fetchRecipeTUI(String name) throws Exception {
        if (name.equals("")) {
            List<String> recipeNames = this.daoHandler.fetchAllRecipes();
            return recipeNames;
        } else {
            name = name.trim().toLowerCase();
            Recipe rec = this.daoHandler.fetchRecipe(name);
            List<String> recipeInList = new ArrayList<String>();
            recipeInList.add("\n***" + rec.getName() + "***\n");
            recipeInList.add(rec.getInstructions());
            recipeInList.add("");
            for (Ingredient ing : rec.getIngredients()) {
                recipeInList.add(ing.toString());
            }
            return recipeInList;
        }
    }

    public String fetchCourses() throws Exception {
        //StringBuilder menuInString = new StringBuilder();
        this.recipeList = this.daoHandler.fetchMenu(7);
        //this.shoppingList.clear(); //empty private variable between menu fetches
        //for (Recipe rec : this.recipeList) {
            //menuInString.append(rec.getName());
            //menuInString.append("\n"); //recipe instructions are not included at this point
            ////recToString.add(rec.getInstructions());
            //List<Ingredient> ingredients = rec.getIngredients();
            //for (Ingredient ing : ingredients) {
                //this.shoppingList.add(ing);
            //}
        //}
        //return menuInString.toString();
        return this.buildMenuIntoString();
    }

    private String buildMenuIntoString() {
        this.shoppingList.clear(); //shoppingList is always built anew following changes in recipeList
        StringBuilder menuInString = new StringBuilder();
        for (Recipe rec : this.recipeList) {
            menuInString.append(rec.getName());
            menuInString.append("\n"); //recipe instructions are not included at this point
            //recToString.add(rec.getInstructions());
            List<Ingredient> ingredients = rec.getIngredients();
            for (Ingredient ing : ingredients) {
                this.shoppingList.add(ing);
            }
        }
        return menuInString.toString();
    }
    
    public String changeCourse(String name) throws Exception {
        Recipe newRecipe = this.daoHandler.fetchRandomRecipe();
        for (Recipe recipe : this.recipeList) {
            if (recipe.getName().equals(name)) {
                recipe.setName(newRecipe.getName());
                recipe.setInstructions(newRecipe.getInstructions());
                recipe.setIngredients(newRecipe.getIngredients());
            }
        }
        return this.buildMenuIntoString();
    }

    public String fetchShoppingList() {
        List<Ingredient> shoppingListSorted = this.sortIngredients(this.shoppingList);
        StringBuilder shoppingListinString = new StringBuilder();
        for (Ingredient ing : shoppingListSorted) {
            shoppingListinString.append(ing.toString());
            shoppingListinString.append("\n");
        }
        return shoppingListinString.toString();
    }
    
    public List<String> fetchMenuTUI() throws Exception {
        List<String> menuToString = new ArrayList<String>();
        List<Recipe> menu = this.daoHandler.fetchMenu(7);
        List<Ingredient> ingredientsAll = new ArrayList<Ingredient>();
        for (Recipe rec : menu) {
            menuToString.add(rec.getName()); //recipe instructions are not included at this point
            //recToString.add(rec.getInstructions());
            List<Ingredient> ingredients = rec.getIngredients();
            for (Ingredient ing : ingredients) {
                ingredientsAll.add(ing);
            }
        }
        List<Ingredient> ingredientsSorted = this.sortIngredients(ingredientsAll);
        menuToString.add("");
        for (Ingredient ing : ingredientsSorted) {
            menuToString.add(ing.toString());
        }
        return menuToString;
    }

    public List<Ingredient> sortIngredients(List<Ingredient> ingredients) {
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
                    //maybe fix the above line so more readable
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
        // add the last ingredient only if not duplicate
        if (!ingredientsSorted.get(ingredientsSorted.size() - 1).equals(combinedIngredients.get(combinedIngredients.size() - 1))) {
            combinedIngredients.add(ingredientsSorted.get(ingredientsSorted.size() - 1));
        }
        return combinedIngredients;
    }

    public void removeRecipe(String name) throws Exception {
        this.daoHandler.removeRecipe(name);
    }
}