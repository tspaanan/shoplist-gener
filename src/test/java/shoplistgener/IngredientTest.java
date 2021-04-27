package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class IngredientTest {
    Ingredient one;
    Ingredient two;
    Ingredient three;
    Ingredient four;
    List<Ingredient> listOne;
    List<Ingredient> listTwo;

    @Before
    public void setUp() {
        one = new Ingredient("name1", Unit.CL, 2);
        two = new Ingredient("name1", Unit.CL, 3);
        three = new Ingredient("name1", Unit.CL, 2);
        four = new Ingredient("name2", Unit.DL, 3);
        listOne = new ArrayList<Ingredient>();
        listOne.add(one); listOne.add(two);
        listTwo = new ArrayList<Ingredient>();
        listTwo.add(three); listTwo.add(four);
    }

    @Test
    public void combineIngredientsCombinesTwoIngredients() {
        Ingredient combinedIngredient = Ingredient.combineIngredients(one, two);
        assertEquals((Integer) 5, combinedIngredient.getRequestedQuantity());
    }
    
    @Test
    public void sortIngredientsRetainsAllIngredients() {
        List<Ingredient> listThree = Ingredient.sortIngredients(listTwo);
        assertTrue(listThree.size() == 2);
    }
    
    @Test
    public void sortIngredientsWorksWithDuplicateIngredients() {
        List<Ingredient> listThree = Ingredient.sortIngredients(listOne);
        assertEquals((Integer) 5, listThree.get(0).getRequestedQuantity());
        assertEquals("name1", listThree.get(0).getName());
    }

    @Test
    public void sortIngredientsWorksWithTriplicateIngredients() {
        Ingredient triplicate = new Ingredient("name1", Unit.CL, 8);
        listOne.add(triplicate);
        List<Ingredient> listThree = Ingredient.sortIngredients(listOne);
        assertEquals((Integer) 13, listThree.get(0).getRequestedQuantity());
        assertEquals("name1", listTwo.get(0).getName());
    }

    @Test
    public void sortIngredientsWorksWithQuadripleIngredients() {
        Ingredient triplicate = new Ingredient("name1", Unit.CL, 8);
        Ingredient extra = new Ingredient("name3", Unit.G, 11);
        Ingredient quadriple = new Ingredient("name1", Unit.CL, 6);
        listOne.add(triplicate); listOne.add(extra); listOne.add(quadriple);
        List<Ingredient> listThree = Ingredient.sortIngredients(listOne);
        assertEquals((Integer) 19, listThree.get(0).getRequestedQuantity());
        assertEquals("name3", listThree.get(1).getName());
    }

    @Test
    public void sortIngredientsWorksWithSingleIngredient() {
        listOne.remove(1);
        List<Ingredient> listThree = Ingredient.sortIngredients(listOne);
        assertTrue(listThree.size() == 1);
    }
}
