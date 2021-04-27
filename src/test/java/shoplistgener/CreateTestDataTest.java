package shoplistgener;

import org.junit.Test;
import org.junit.After;

import shoplistgener.dao.ShoplistgenerDAOFake;
import shoplistgener.dao.ShoplistgenerDAOsqlite;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.Recipe;
import shoplistgener.domain.ShoplistgenerService;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CreateTestDataTest {

    @Test
    public void createRandomTestDataCreatesDataAccordingly() throws Exception {
        new ShoplistgenerDAOsqlite("unittestDatabase.db"); //for the rest of this test, we simply need a properly initialized database file to exist
        CreateTestData.createRandomTestData("unittestDatabase.db");
        Connection db = DriverManager.getConnection("jdbc:sqlite:unittestDatabase.db");
        PreparedStatement p = db.prepareStatement("SELECT name FROM recipes WHERE name='recipe#1' AND visible=TRUE");
        ResultSet r = p.executeQuery();
        assertEquals("recipe#1", r.getString("name"));
    }

    @After
    public void cleanUp() {
        File toBeDeleted = new File("unittestDatabase.db");
        toBeDeleted.delete();
    }
}
