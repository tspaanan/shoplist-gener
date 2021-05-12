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
    private List<Ingredient> ings;
    private Ingredient one;
    private Ingredient two;
    private Ingredient three;
    public Recipe oneRecipe;

    public ShoplistgenerDAOFake() {
        this.ings = new ArrayList<Ingredient>();
        this.one = new Ingredient("ing1", Unit.CL, 1);
        this.two = new Ingredient("ing2", Unit.CL, 2);
        this.three = new Ingredient("ing3", Unit.CL, 3);
        this.ings.add(one);
        this.ings.add(two);
        this.ings.add(three);
    }

    @Override
    public void addRecipe(Recipe recipe) throws Exception {
        this.oneRecipe = recipe;
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
        return new Recipe("name1", "instructions1", ings);
    }

    @Override
    public void removeRecipe(String name) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Recipe fetchRandomRecipe() throws Exception {
        return new Recipe("random1", "instructions1", ings);
    }

    @Override
    public int fetchRecipeId(String name) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void modifyRecipe(Recipe modifiedRecipe) throws Exception {
        this.oneRecipe = modifiedRecipe;
        
    }

    @Override
    public void insertTestData() throws Exception {

    }

    @Override
    public List<Ingredient> fetchAllIngredients() throws Exception {
        return this.ings;
    }

    @Override
    public List<Ingredient> fetchKitchenIngredients() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addIngredientToKitchen(String name) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeIngredient(String name) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateIngredientQuantityInKitchen(String name, Integer quantity) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
