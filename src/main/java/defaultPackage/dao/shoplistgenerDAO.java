package defaultPackage.dao;

import java.util.*;
import defaultPackage.domain.Recipe;

public interface shoplistgenerDAO {
    List<String> fetchAllRecipes() throws Exception;
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRecipe(String name) throws Exception;
}
