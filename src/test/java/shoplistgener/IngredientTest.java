package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Unit;

import static org.junit.Assert.*;

public class IngredientTest {
    Ingredient one;
    Ingredient two;

    @Before
    public void setUp() {
        one = new Ingredient("name1", Unit.CL, 2);
        two = new Ingredient("name1", Unit.CL, 3);
    }

    @Test
    public void combineIngredientsCombinesTwoIngredients() throws Exception {
        Ingredient combinedIngredient = Ingredient.combineIngredients(one, two);
        assertEquals((Integer) 5, combinedIngredient.getRequestedQuantity());
    }
}
