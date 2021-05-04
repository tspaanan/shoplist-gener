package shoplistgener.dao;

import java.util.*;
import shoplistgener.domain.Recipe;

/**
 * DAO interface for accessing and storing data on disk
 */
public interface ShoplistgenerDAO {
    void addRecipe(Recipe recipe) throws Exception;
    List<String> fetchAllRecipes() throws Exception;
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRecipe(String name) throws Exception;
    void removeRecipe(String name) throws Exception;
    Recipe fetchRandomRecipe() throws Exception;
    int fetchRecipeId(String name) throws Exception;
    void modifyRecipe(Recipe modifiedRecipe) throws Exception;
    void insertTestData() throws Exception;
}
