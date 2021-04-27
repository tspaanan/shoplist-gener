package shoplistgener;

import org.junit.Before;
import org.junit.Test;

import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.Unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeTest {
    Recipe one;

    @Before
    public void setUp() {
        one = new Recipe("name1", "instructions1", new ArrayList<Ingredient>());
    }

    @Test
    public void setNameSetsRecipeName() {
        one.setName("changedName");
        assertEquals("changedName", one.getName());
    }

    @Test
    public void setInstructionsSetsRecipeInstructions() {
        one.setInstructions("changedInstructions");
        assertEquals("changedInstructions", one.getInstructions());
    }
}