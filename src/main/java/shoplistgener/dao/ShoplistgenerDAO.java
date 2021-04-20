package shoplistgener.dao;

import java.util.*;
import shoplistgener.domain.Recipe;

public interface ShoplistgenerDAO {
    void addRecipe(Recipe recipe) throws Exception;
    List<String> fetchAllRecipes() throws Exception;
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRecipe(String name) throws Exception;
    void removeRecipe(String name) throws Exception;
}
