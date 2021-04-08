package defaultPackage.dao;

import java.util.*;
import defaultPackage.domain.Recipe;

public interface shoplistgenerDAO {
    List<Recipe> fetchMenu(int days) throws Exception;
    Recipe fetchRecipe() throws Exception;
}
