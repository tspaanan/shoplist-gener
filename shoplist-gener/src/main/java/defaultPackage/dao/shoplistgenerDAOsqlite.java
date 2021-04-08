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
        Integer recipesNo = Integer.parseInt(c.executeQuery().getString("COUNT(*)")); //TODO: use getInt() instead
        for (int i = 0; i < days; i++) {
            PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions FROM recipes WHERE id=?");
            //fetch recipes one at a time, maybe all at the same time instead?
            Integer randRecipeNo = rand.nextInt(recipesNo + 1);
            p.setString(1, String.valueOf(randRecipeNo));
            ResultSet r = p.executeQuery();
            PreparedStatement ings = this.db.prepareStatement("SELECT I.name,I.unit,IR.quantity FROM ingredients I,"
                                                            + "ingredientsInRecipes IR WHERE IR.recipe_id=? AND IR.ingredient_id=I.id");
            ings.setString(1, String.valueOf(randRecipeNo));
            ResultSet ingsResult = ings.executeQuery();
            List<Ingredient> ingsInList = new ArrayList<Ingredient>();
            while (ingsResult.next()) {
                //System.out.println(ingsResult.getString("name"));
                Ingredient ingObject = new Ingredient(ingsResult.getString("name"), Unit.valueOf(ingsResult.getString("unit").toUpperCase()), ingsResult.getInt("quantity"));
                //System.out.println(ingObject);
                ingsInList.add(ingObject);
            }
            // r.getString("name"); // mystic sqlite error only once with the line below: "ResultSet was closed", couldn't reproduce ever again: TODO more exception handling! 
            // "ResultSet was closed" spotted for 2nd time! need to investigate, happend very rarely
            Recipe rec = new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
            menu.add(rec);
        }
        return menu;
    }

    public Recipe fetchRecipe() throws SQLException {
        return new Recipe("debugName","debugInstructions",new ArrayList<Ingredient>());
    }
}
