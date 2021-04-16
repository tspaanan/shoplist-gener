package shoplistgener.dao;

import java.util.*;
import shoplistgener.domain.Recipe;

public interface ShoplistgenerDAO {
    List<String> fetchAllRecipes() throws Exception;
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRecipe(String name) throws Exception;
}
