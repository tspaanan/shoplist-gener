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
    ShoplistgenerDAOFake fakeDAO;

    @Before
    public void setUp() {
        fakeDAO = new ShoplistgenerDAOFake();
        object = new ShoplistgenerService(fakeDAO);
        fakeRecipeList = new ArrayList<Recipe>();
        fakeRecipeList.add(new Recipe("fakeRecipe1", "fakeRecipe1Instructions", new ArrayList<Ingredient>()));
    }

    @Test
    public void addRecipeCallsDAOwithRightRecipe() throws Exception {
        Recipe rec = fakeRecipeList.get(0);
        List<String> recipeParts = new ArrayList<String>();
        recipeParts.add(rec.getName());
        recipeParts.add(rec.getInstructions());
        List<String> ingParts = new ArrayList<String>();
        ingParts.add("ing1 1 cl");
        object.addRecipe(recipeParts, ingParts);
        assertEquals("fakeRecipe1", fakeDAO.oneRecipe.getName());
        assertEquals("fakeRecipe1Instructions", fakeDAO.oneRecipe.getInstructions());
    }

    @Test
    public void modifyRecipeCallsDAOwithRightRecipe() throws Exception {
        Recipe rec = fakeRecipeList.get(0);
        List<String> recipeParts = new ArrayList<String>();
        recipeParts.add(rec.getName());
        recipeParts.add(rec.getInstructions());
        List<String> ingParts = new ArrayList<String>();
        ingParts.add("ing1 1 cl");
        object.modifyRecipe(recipeParts, ingParts);
        assertEquals("fakeRecipe1", fakeDAO.oneRecipe.getName());
        assertEquals("fakeRecipe1Instructions", fakeDAO.oneRecipe.getInstructions());
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
    public void fetchAllIngredientsRetainsLastIngredient() throws Exception {
        String allIngredients = object.fetchAllIngredients();
        assertEquals("ing3 [CL]", allIngredients.split("\n")[4]);
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
        assertEquals("ing1 1 cl", fetched.split("\n")[7]);
        assertEquals("ing2 2 cl", fetched.split("\n")[8]);
        assertEquals("ing3 3 cl", fetched.split("\n")[9]);
    }

    @Test
    public void fetchRecipeListSortsRecipePartsAtRightIndexNumbers() throws Exception {
        List<String> recipeInList = object.fetchRecipeList("name1");
        assertEquals("name1", recipeInList.get(0));
        assertEquals("instructions1", recipeInList.get(1));
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
        assertEquals("abcIng 12 cl", sortedShoppingList.split("\n")[0]);
    }
}
