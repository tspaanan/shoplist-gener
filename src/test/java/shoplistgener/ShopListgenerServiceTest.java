package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.dao.ShoplistgenerDAOFake;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.ShoplistgenerService;
import shoplistgener.domain.Unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ShopListgenerServiceTest {
    ShoplistgenerService object;
    List<Recipe> fakeRecipeList;

    @Before
    public void setUp() {
        object = new ShoplistgenerService(new ShoplistgenerDAOFake());
        fakeRecipeList = new ArrayList<Recipe>();
        fakeRecipeList.add(new Recipe("fakeRecipe1", "fakeRecipe1Instructions", new ArrayList<Ingredient>()));
    }

    @Test
    public void changeCourseFetchesRandomRecipe() throws Exception {
        object.setRecipeList(fakeRecipeList);
        String randomRecipe = object.changeCourse(true, "fakeRecipe1", "");
        assertEquals("random1", randomRecipe.split("\n")[0]);
    }

    @Test
    public void chanceCourseFetchesNamedRecipe() throws Exception {
        object.setRecipeList(fakeRecipeList);
        String namedRecipe = object.changeCourse(false, "fakeRecipe1", "fakeRecipe1");
        assertEquals("name1", namedRecipe.split("\n")[0]);
    }

    @Test
    public void fetchCoursesReturnsString() throws Exception {
        assertTrue(object.fetchCourses() instanceof String);
        //TODO: more useful tests
    }

    @Test
    public void fetchRecipeReturnsCorrectlyStylizedRecipeInString() throws Exception {
        String fetched = object.fetchRecipe("name1");
        assertEquals("***name1***", fetched.split("\n")[1]);
    }

    @Test
    public void fetchRecipeRetainsAllIngredients() throws Exception {
        String fetched = object.fetchRecipe("name1");
        assertEquals("ing1;1;cl", fetched.split("\n")[7]);
        assertEquals("ing2;2;cl", fetched.split("\n")[8]);
        assertEquals("ing3;3;cl", fetched.split("\n")[9]);
    }

    @Test
    public void fetchShoppingListReturnEmptyIfEmpty() throws Exception {
        assertTrue(object.fetchShoppingList().equals(""));
    }

    @Test
    public void fetchShoppingListSortsIngredients() throws Exception {
        Ingredient ing1 = new Ingredient("abcIng", Unit.CL, 12);
        Ingredient ing2 = new Ingredient("bcdIng", Unit.DL, 22);
        List<Ingredient> ingList = new ArrayList<Ingredient>();
        ingList.add(ing2);
        ingList.add(ing1);
        object.setShoppingList(ingList);
        String sortedShoppingList = object.fetchShoppingList();
        assertEquals("abcIng;12;cl", sortedShoppingList.split("\n")[0]);
    }
}
