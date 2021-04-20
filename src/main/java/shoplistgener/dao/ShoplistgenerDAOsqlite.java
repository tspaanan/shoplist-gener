package shoplistgener.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shoplistgener.domain.*;
import java.util.Random;

public class ShoplistgenerDAOsqlite implements ShoplistgenerDAO {
    private Connection db;

    public ShoplistgenerDAOsqlite() {
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:Test.db"); //TODO: database name is still hardcoded
            this.db.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("SQLException:" + e.getMessage());
        }
    }

    public void addRecipe(Recipe newRecipe) throws Exception {
        //check for duplicate recipe names, TODO: clashes with removed recipe names
        PreparedStatement cs = this.db.prepareStatement("SELECT 1 FROM recipes WHERE name='" + newRecipe.getName() + "'");
        ResultSet ch = cs.executeQuery();
        if (ch.next()) {
            //throw new RecipeNameExists();
            //maybe code my own Exception class?
            throw new UnsupportedOperationException();
        }
  
        //add information for recipes table
        String insert = "INSERT INTO recipes (name,instructions,visible) VALUES ('" + newRecipe.getName() + "','" 
                        + newRecipe.getInstructions() + "',TRUE)";
        Statement s = this.db.createStatement();
        s.executeUpdate(insert);
        this.db.commit();
        
        //add information for ingredients table
        for (Ingredient ing : newRecipe.getIngredients()) {
            //before adding, check if ingredient already exists
            PreparedStatement ci = this.db.prepareStatement("SELECT 1 FROM ingredients WHERE name='" + ing.getName() + "'");
            ResultSet ciResults = ci.executeQuery();
            if (ciResults.next()) {
                continue;
            }
            String insertIng = "INSERT INTO ingredients (name, unit) VALUES ('" + ing.getName() + "','"
                            + ing.getUnit().toString().toLowerCase() + "')";
            s.executeUpdate(insertIng);
        }
        this.db.commit();
        
        //add information for ingredientsInRecipes table
        PreparedStatement p = this.db.prepareStatement("SELECT id FROM recipes WHERE name=?");
        p.setString(1, newRecipe.getName());
        ResultSet r = p.executeQuery();
        String recipe_id = r.getString("id");
        for (Ingredient ing : newRecipe.getIngredients()) {
            PreparedStatement pIng = this.db.prepareStatement("SELECT id FROM ingredients WHERE name=?");
            pIng.setString(1, ing.getName());
            ResultSet rIng = pIng.executeQuery();
            String ingredient_id = rIng.getString("id");
            String ingInRecipeInsert = "INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) VALUES ("
                                    + recipe_id + "," + ingredient_id + "," + ing.getRequestedQuantity().toString() + ")";
            s.executeUpdate(ingInRecipeInsert);
        }
        this.db.commit();
    }

    public List<Recipe> fetchMenu(int days) throws SQLException {
        Random rand = new Random();
        List<Recipe> menu = new ArrayList<Recipe>();
        PreparedStatement c = this.db.prepareStatement("SELECT COUNT(*) FROM recipes");
        Integer recipesNo = Integer.parseInt(c.executeQuery().getString("COUNT(*)")); //TODO: use getInt() instead
        for (int i = 0; i < days; i++) {
            PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions,visible FROM recipes WHERE id=?");
            //fetch recipes one at a time, maybe all at the same time instead?
            Integer randRecipeNo = rand.nextInt(recipesNo + 1);
            p.setString(1, String.valueOf(randRecipeNo));
            ResultSet r = p.executeQuery();
            //if randomly selected removed recipe, simply make another random selection instead
            if (r.getString("visible").equals("0")) {
                i--;
                continue;
            }
            List<Ingredient> ingsInList = this.fetchIngredients(randRecipeNo);
            Recipe rec = new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
            menu.add(rec);
        }
        return menu;
    }

    public Recipe fetchRecipe(String name) throws SQLException {
        PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions FROM recipes WHERE name=? AND visible=TRUE");
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        List<Ingredient> ingsInList = this.fetchIngredients(r.getInt("id"));
        return new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
    }

    public List<String> fetchAllRecipes() throws SQLException {
        PreparedStatement p = this.db.prepareStatement("SELECT name FROM recipes WHERE visible=TRUE");
        ResultSet r = p.executeQuery();
        List<String> recipeNames = new ArrayList<String>();
        while (r.next()) {
            recipeNames.add(r.getString("name"));
        }
        return recipeNames;
    }

    private List<Ingredient> fetchIngredients(int id) throws SQLException {
        PreparedStatement ings = this.db.prepareStatement("SELECT I.name,I.unit,IR.quantity FROM ingredients I,"
                                                        + "ingredientsInRecipes IR WHERE IR.recipe_id=? AND IR.ingredient_id=I.id");
        ings.setString(1, String.valueOf(id));
        ResultSet ingsResult = ings.executeQuery();
        List<Ingredient> ingsInList = new ArrayList<Ingredient>();
        while (ingsResult.next()) {
            Ingredient ingObject = new Ingredient(ingsResult.getString("name"), Unit.valueOf(ingsResult.getString("unit").toUpperCase()), ingsResult.getInt("quantity"));
            ingsInList.add(ingObject);
        }
        return ingsInList;
    }

    public void removeRecipe(String name) throws Exception {
        PreparedStatement p = this.db.prepareStatement("UPDATE recipes SET visible=FALSE WHERE name=?");
        p.setString(1, name);
        p.executeUpdate();
        this.db.commit();
    }
}
