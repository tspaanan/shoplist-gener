package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.dao.ShoplistgenerDAOFake;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.ShoplistgenerService;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ShopListgenerServiceTest {
    ShoplistgenerService object;

    @Before
    public void setUp() {
        object = new ShoplistgenerService(new ShoplistgenerDAOFake());
    }

    @Test
    public void fetchCoursesReturnsString() throws Exception {
        assertTrue(object.fetchCourses() instanceof String);
        //TODO: more useful tests
    }

    @Test
    public void buildMenuIntoStringRetainsAllCourses() {
        Recipe one = new Recipe("name1", "instructions1", new ArrayList<Ingredient>());
        Recipe two = new Recipe("name2", "instructions2", new ArrayList<Ingredient>());
        Recipe three = new Recipe("name3", "instructions3", new ArrayList<Ingredient>());
        List<Recipe> listOne = new ArrayList<Recipe>();
        listOne.add(one); listOne.add(two); listOne.add(three);
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
}
