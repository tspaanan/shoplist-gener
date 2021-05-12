package shoplistgener.dao;

import java.util.List;
import shoplistgener.domain.*;

/**
 * DAO interface for accessing and storing data on disk
 */
public interface ShoplistgenerDAO {
    void addIngredientToKitchen(String name) throws Exception;
    void addRecipe(Recipe recipe) throws Exception;
    List<Ingredient> fetchAllIngredients() throws Exception;
    List<String> fetchAllRecipes() throws Exception;
    List<Ingredient> fetchKitchenIngredients() throws Exception;
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRandomRecipe() throws Exception;
    Recipe fetchRecipe(String name) throws Exception;
    int fetchRecipeId(String name) throws Exception;
    void insertTestData() throws Exception;
    void modifyRecipe(Recipe modifiedRecipe) throws Exception;
    void removeIngredient(String name) throws Exception;
    void removeRecipe(String name) throws Exception;
    void updateIngredientQuantityInKitchen(String name, Integer quantity) throws Exception;
}
