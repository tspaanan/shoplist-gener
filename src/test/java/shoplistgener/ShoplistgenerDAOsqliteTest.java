package shoplistgener;

import org.junit.Test;

import shoplistgener.dao.ShoplistgenerDAO;
import shoplistgener.dao.ShoplistgenerDAOsqlite;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.Unit;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

public class ShoplistgenerDAOsqliteTest {
    ShoplistgenerDAO object;
    Connection db;
    Statement s;
    Recipe one;
    Recipe two;
    Ingredient ingOne;
    Ingredient ingTwo;
    List<Ingredient> ings;

    @Before
    public void setUp() throws Exception {
        object = new ShoplistgenerDAOsqlite("unittestDatabase.db");
        object.insertTestData();
        Connection db = DriverManager.getConnection("jdbc:sqlite:unittestDatabase.db");
        s = db.createStatement();
        one = new Recipe("name1", "instructions1", new ArrayList<Ingredient>());
        two = new Recipe("recipe#2", "changed instruction", new ArrayList<Ingredient>());
        ingOne = new Ingredient("ingName1", Unit.CL, 11);
        ingTwo = new Ingredient("ingName2", Unit.DL, 22);
        ings = new ArrayList<Ingredient>();
        ings.add(ingOne);
        ings.add(ingTwo);
    }

    @Test
    public void addRecipeInsertsRecipeCorrectly() throws Exception {
        object.addRecipe(one);
        ResultSet r = s.executeQuery("SELECT name FROM recipes WHERE name='name1'");
        assertEquals("name1", r.getString("name"));
    }

    @Test
    public void addRecipeInsertsAllIngredients() throws Exception {
        one.setIngredients(ings);
        object.addRecipe(one);
        Recipe oneFetchedFromDatabase = object.fetchRecipe("name1");
        assertEquals(oneFetchedFromDatabase.getIngredients().get(0).getName(), ingOne.getName());
        assertEquals(oneFetchedFromDatabase.getIngredients().get(1).getName(), ingTwo.getName());
    }

    @Test
    public void modifyRecipeChangesRecipeInstructions() throws Exception {
        object.modifyRecipe(two);
        ResultSet r = s.executeQuery("SELECT instructions FROM recipes WHERE name='recipe#2'");
        assertEquals("changed instruction", r.getString("instructions"));
    }

    @Test
    public void modifyRecipeInsertsNewIngredients() throws Exception {
        object.addRecipe(one);
        one.setIngredients(ings);
        object.modifyRecipe(one);
        Recipe modifiedOneFetchedFromDatabase = object.fetchRecipe("name1");
        assertEquals(modifiedOneFetchedFromDatabase.getIngredients().get(0).getName(), ingOne.getName());
        assertEquals(modifiedOneFetchedFromDatabase.getIngredients().get(1).getName(), ingTwo.getName());
    }

    @Test
    public void fetchMenuReturnsSixRecipes() throws Exception {
        List<Recipe> fetchedMenu = object.fetchMenu(6);
        assertTrue(fetchedMenu.size() == 6);
    }

    @Test
    public void fetchMenuReturnsZeroRecipes() throws Exception {
        List<Recipe> fetchedMenu = object.fetchMenu(0);
        assertTrue(fetchedMenu.size() == 0);
    }

    @Test
    public void fetchKitchenIngredientsReturnsAllInitialKitchenIngredients() throws Exception {
        List<Ingredient> fetchedIngredients = object.fetchKitchenIngredients();
        assertTrue(fetchedIngredients.size() == 20);
    }

    @Test
    public void fetchKitchenIngredientsReturnsAllModifiedKitchenIngredients() {
        try {
            String insert = "INSERT INTO ingredientsInKitchen (ingredient_id,quantity) VALUES (1,1)";
            Statement t = this.db.createStatement();
            t.execute(insert);
            this.db.commit();
            List<Ingredient> fetchedIngredients = object.fetchKitchenIngredients();
            assertTrue(fetchedIngredients.size() == 21);
        } catch (Exception e) {
        }
    }

    @Test
    public void fetchAllIngredientsReturnsAllIngredients() throws Exception {
        List<Ingredient> fetchedIngredients = object.fetchAllIngredients();
        assertTrue(fetchedIngredients.size() == 50);
    }

    @Test
    public void fetchAllIngredientsReturnsAllModifiedKitchenIngredients() {
        try {
            String insert = "INSERT INTO ingredients (name,unit) VALUES ('extraName','cl')";
            Statement t = this.db.createStatement();
            t.execute(insert);
            this.db.commit();
            List<Ingredient> fetchedIngredients = object.fetchAllIngredients();
            assertTrue(fetchedIngredients.size() == 51);
        } catch (Exception e) {
        }
    }

    @Test
    public void fetchAllRecipesReturnsHundrethRecipeAsHundreth() throws Exception {
        List<String> fetchedRecipes = object.fetchAllRecipes();
        assertEquals("recipe#100", fetchedRecipes.get(99));
    }
    
    @After
    public void cleanUp() {
        File toBeDeleted = new File("unittestDatabase.db");
        toBeDeleted.delete();
    }
}
