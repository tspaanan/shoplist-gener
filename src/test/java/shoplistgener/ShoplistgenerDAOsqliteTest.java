package shoplistgener;

import org.junit.Test;

import shoplistgener.dao.ShoplistgenerDAO;
import shoplistgener.dao.ShoplistgenerDAOsqlite;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;

public class ShoplistgenerDAOsqliteTest {
    ShoplistgenerDAO object;
    Connection db;
    Statement s;
    Recipe one;
    Recipe two;

    @Before
    public void setUp() throws Exception {
        object = new ShoplistgenerDAOsqlite("unittestDatabase.db");
        object.insertTestData();
        Connection db = DriverManager.getConnection("jdbc:sqlite:unittestDatabase.db");
        s = db.createStatement();
        one = new Recipe("name1", "instructions1", new ArrayList<Ingredient>());
        two = new Recipe("recipe#2", "changed instruction", new ArrayList<Ingredient>());
    }

    @Test
    public void addRecipeInsertsRecipeCorrectly() throws Exception {
        object.addRecipe(one);
        ResultSet r = s.executeQuery("SELECT name FROM recipes WHERE name='name1'");
        assertEquals("name1", r.getString("name"));
    }

    @Test
    public void modifyRecipeChangesRecipeInstructions() throws Exception {
        object.modifyRecipe(two);
        ResultSet r = s.executeQuery("SELECT instructions FROM recipes WHERE name='recipe#2'");
        assertEquals("changed instruction", r.getString("instructions"));
    }

    @After
    public void cleanUp() {
        File toBeDeleted = new File("unittestDatabase.db");
        toBeDeleted.delete();
    }
}
