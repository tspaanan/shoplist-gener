package shoplistgener.dao;

import java.util.ArrayList;
import java.util.List;

import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.Unit;

/**
 * Implementation of ShoplistgenerDAO interface for unit tests
 */
public class ShoplistgenerDAOFake implements ShoplistgenerDAO {

    public ShoplistgenerDAOFake() {

    }

    @Override
    public void addRecipe(Recipe recipe) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> fetchAllRecipes() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns an empty list of Recipe-objects for testing
     * @param days not used
     * @throws Exception
     */
    @Override
    public List<Recipe> fetchMenu(int days) throws Exception {
        return new ArrayList<Recipe>();
    }

    /**
     * Returns a Recipe-object for testing
     * @param name not used
     * @throws Exception
     */
    @Override
    public Recipe fetchRecipe(String name) throws Exception {
        List<Ingredient> ings = new ArrayList<Ingredient>();
        Ingredient one = new Ingredient("ing1", Unit.CL, 1);
        Ingredient two = new Ingredient("ing2", Unit.CL, 2);
        Ingredient three = new Ingredient("ing3", Unit.CL, 3);
        ings.add(one);
        ings.add(two);
        ings.add(three);
        return new Recipe("name1", "instructions1", ings);
    }

    @Override
    public void removeRecipe(String name) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Recipe fetchRandomRecipe() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int fetchRecipeId(String name) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void modifyRecipe(Recipe modifiedRecipe) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insertTestData() throws Exception {

    }

}
