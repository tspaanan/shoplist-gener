package defaultPackage.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import defaultPackage.domain.*;
import java.util.Random;

public class shoplistgenerDAOsqlite implements shoplistgenerDAO {
    private Connection db;

    public shoplistgenerDAOsqlite() {
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:Test.db"); //TODO: database name is still hardcoded
        } catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
        }
    }

    public List<Recipe> fetchMenu(int days) throws SQLException {
        //return new ArrayList<Recipe>();
        Random rand = new Random();
        List<Recipe> menu = new ArrayList<Recipe>();
        PreparedStatement c = this.db.prepareStatement("SELECT COUNT(*) FROM recipes");
        Integer recipesNo = Integer.parseInt(c.executeQuery().getString("COUNT(*)"));
        for (int i = 0; i < days; i++) {
            PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions FROM recipes WHERE id=?");
            //fetch recipes one at a time, maybe all at the same time instead?
            p.setString(1, String.valueOf(rand.nextInt(recipesNo + 1)));
            ResultSet r = p.executeQuery();
            Recipe rec = new Recipe(r.getString("name"),r.getString("instructions"),new ArrayList<Ingredient>());
            menu.add(rec);
        }
        return menu;
    }

    public Recipe fetchRecipe() throws SQLException {
        return new Recipe("debugName","debugInstructions",new ArrayList<Ingredient>());
    }
}
