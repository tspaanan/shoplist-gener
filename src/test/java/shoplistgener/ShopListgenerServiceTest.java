package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.dao.ShoplistgenerDAOsqlite;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Unit;
import shoplistgener.domain.ShoplistgenerService;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ShopListgenerServiceTest {
    ShoplistgenerService object;
    Ingredient one;
    Ingredient two;
    List<Ingredient> listOne;

    @Before
    public void setUp() {
        object = new ShoplistgenerService(new ShoplistgenerDAOsqlite());
        //TODO: change parameter to DAOFake
        one = new Ingredient("name1", Unit.CL, 2);
        two = new Ingredient("name2", Unit.DL, 3);
        listOne = new ArrayList<Ingredient>();
        listOne.add(one); listOne.add(two);
    }

    @Test
    public void fetchCoursesReturnsString() throws Exception {
        assertTrue(object.fetchCourses() instanceof String);
        //TODO: more useful tests once DAOFake is up and running
    }

    @Test
    public void sortIngredientsRetainsAllIngredients() {
        assertTrue(object.sortIngredients(listOne).size() == 2);
    }

    @Test
    public void sortIngredientsWorksWithDuplicateIngredients() {
        Ingredient duplicate = new Ingredient("name1", Unit.CL, 4);
        listOne.add(duplicate);
        List<Ingredient> listTwo = object.sortIngredients(listOne);
        assertEquals((Integer) 6, listTwo.get(0).getRequestedQuantity());
        assertEquals("name2", listTwo.get(1).getName());
    }

    @Test
    public void sortIngredientsWorksWithTriplicateIngredients() {
        Ingredient duplicate = new Ingredient("name1", Unit.CL, 4);
        Ingredient triplicate = new Ingredient("name1", Unit.CL, 8);
        listOne.add(duplicate); listOne.add(triplicate);
        List<Ingredient> listTwo = object.sortIngredients(listOne);
        assertEquals((Integer) 14, listTwo.get(0).getRequestedQuantity());
        assertEquals("name2", listTwo.get(1).getName());
    }

    @Test
    public void sortIngredientsWorksWithQuadripleIngredients() {
        Ingredient duplicate = new Ingredient("name1", Unit.CL, 4);
        Ingredient triplicate = new Ingredient("name1", Unit.CL, 8);
        Ingredient extra = new Ingredient("name3", Unit.G, 11);
        Ingredient quadriple = new Ingredient("name1", Unit.CL, 6);
        listOne.add(duplicate); listOne.add(triplicate); listOne.add(extra); listOne.add(quadriple);
        List<Ingredient> listTwo = object.sortIngredients(listOne);
        assertEquals((Integer) 20, listTwo.get(0).getRequestedQuantity());
        assertEquals("name2", listTwo.get(1).getName());
        assertEquals("name3", listTwo.get(2).getName());
    }

    @Test
    public void sortIngredientsWorksWithSingleIngredient() {
        listOne.remove(listOne.size() - 1);
        listOne.add(new Ingredient("name1", Unit.CL,4));
        assertTrue(object.sortIngredients(listOne).size() == 1);
    }
}