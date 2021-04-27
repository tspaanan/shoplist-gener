package shoplistgener.dao;

import shoplistgener.domain.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoplistgenerDAOsqlite implements ShoplistgenerDAO {
    private Connection db;

    public ShoplistgenerDAOsqlite(String databaseName) {
        if (!databaseName.isEmpty()) {
            String schemaInString = "";
            try {
                this.db = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
                this.db.setAutoCommit(false);
                Path filePath = Path.of("./dokumentaatio/schema.sql"); //traditional schema.sql format was too hard to parse correctly, so atm all
                //CREATE TABLE commands are on their own lines
                schemaInString = Files.readString(filePath);
            } catch (SQLException e) {
                System.out.println("SQLException:" + e.getMessage());
            } catch (IOException i) {
                System.out.println("IOException:" + i.getMessage());
            }
            try {
                Statement s = this.db.createStatement();
                for (String tableCreation : schemaInString.split(";")) {
                    s.execute(tableCreation);
                    this.db.commit(); //jdbc driver fails to commit all changes at once (line 36 below), so every change is committed here as its own transaction instead
                }
            //this.db.commit();
            } catch (SQLException e) {

            }
        }
    }

    public void addRecipe(Recipe newRecipe) throws Exception {
        //check for duplicate recipe names, TODO: clashes with removed recipe names
        //TODO: also, remember parametrization of SQL-quories
        PreparedStatement cs = this.db.prepareStatement("SELECT 1 FROM recipes WHERE name='" + newRecipe.getName() + "'");
        ResultSet ch = cs.executeQuery();
        if (ch.next()) {
            //throw new RecipeNameExists();
            //IDEA: maybe code my own Exception class?
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
        String recipeId = r.getString("id");
        for (Ingredient ing : newRecipe.getIngredients()) {
            PreparedStatement pIng = this.db.prepareStatement("SELECT id FROM ingredients WHERE name=?");
            pIng.setString(1, ing.getName());
            ResultSet rIng = pIng.executeQuery();
            String ingredientId = rIng.getString("id");
            String ingInRecipeInsert = "INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) VALUES ("
                                    + recipeId + "," + ingredientId + "," + ing.getRequestedQuantity().toString() + ")";
            s.executeUpdate(ingInRecipeInsert);
        }
        this.db.commit();
    }

    public void modifyRecipe(Recipe modifiedRecipe) throws Exception {
        //update instructions, TODO: parametrization!
        //TODO: refactor with duplicate code from addRecipe above
        PreparedStatement p = this.db.prepareStatement("UPDATE recipes SET instructions=? WHERE name='" + modifiedRecipe.getName() + "'");
        p.setString(1, modifiedRecipe.getInstructions());
        p.executeUpdate();
        this.db.commit();
        
        //remove all old ingredients from ingredientsInRecipes table
        int recipeId = this.fetchRecipeId(modifiedRecipe.getName());
        PreparedStatement rm = this.db.prepareStatement("DELETE FROM ingredientsInRecipes WHERE recipe_id=?");
        rm.setString(1, String.valueOf(recipeId));
        rm.executeUpdate();
        this.db.commit();

        //add all ingredients
        Statement s = this.db.createStatement();
        for (Ingredient ing : modifiedRecipe.getIngredients()) {
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
        for (Ingredient ing : modifiedRecipe.getIngredients()) {
            PreparedStatement pIng = this.db.prepareStatement("SELECT id FROM ingredients WHERE name=?");
            pIng.setString(1, ing.getName());
            ResultSet rIng = pIng.executeQuery();
            String ingredientId = rIng.getString("id");
            String ingInRecipeInsert = "INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) VALUES ("
                                    + String.valueOf(recipeId) + "," + ingredientId + "," + ing.getRequestedQuantity().toString() + ")";
            s.executeUpdate(ingInRecipeInsert);
        }
        this.db.commit();
    }

    public List<Recipe> fetchMenu(int days) throws Exception {
        List<Recipe> menu = new ArrayList<Recipe>();
        for (int i = 0; i < days; i++) {
            PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions,visible FROM recipes WHERE id=?");
            //fetch recipes one at a time, maybe all could be fetched at the same time instead?
            int randRecipeNo = this.randomNumberForRecipes();
            if (randRecipeNo == -1) {
                break;
            }
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

    private int randomNumberForRecipes() throws SQLException {
        Random rand = new Random();
        PreparedStatement c = this.db.prepareStatement("SELECT COUNT(*) FROM recipes");
        int recipesNo = c.executeQuery().getInt("COUNT(*)");
        //edge case check: if 0 rows in recipes table
        if (recipesNo == 0) {
            return -1;
        }
        //edge case check: if 0 rows in recipes table with visible=TRUE
        c = this.db.prepareStatement("SELECT COUNT(*) FROM recipes WHERE visible=TRUE");
        if (c.executeQuery().getInt("COUNT(*)") == 0) {
            return -1;
        }
        return rand.nextInt(recipesNo) + 1;
    }

    public Recipe fetchRecipe(String name) throws SQLException {
        PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions FROM recipes WHERE name=? AND visible=TRUE");
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        List<Ingredient> ingsInList = this.fetchIngredients(r.getInt("id"));
        return new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
    }

    public int fetchRecipeId(String name) {
        try {
            PreparedStatement p = this.db.prepareStatement("SELECT id FROM recipes WHERE name=? AND visible=TRUE");
            p.setString(1, name);
            ResultSet r = p.executeQuery();
            return r.getInt("id");
        } catch (Exception e) {
            return -1;
        }
    }

    public Recipe fetchRandomRecipe() throws SQLException {
        PreparedStatement p = this.db.prepareStatement("SELECT name FROM recipes WHERE id=?");
        p.setString(1, String.valueOf(randomNumberForRecipes()));
        ResultSet r = p.executeQuery();
        return this.fetchRecipe(r.getString("name"));
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

    public void insertTestData() {
        try {
            Statement s = this.db.createStatement();
            for (int i = 1; i <= 100; i++) {
                String insert = "INSERT INTO recipes (name,instructions,visible) VALUES ('recipe#" + String.valueOf(i) 
                                + "','default_instructions_for_this_recipe',TRUE)";
                s.execute(insert);
            }
            Random r = new Random();
            String[] units = {"dl", "ml", "g", "pcs", "cl", "kg"};
            for (int i = 1; i <= 50; i++) {
                String unit = units[r.nextInt(6)];
                String insert = "INSERT INTO ingredients (name,unit) VALUES ('ingredient#" + String.valueOf(i)
                                + "','" + unit + "')";
                s.execute(insert);
            }
            for (int i = 1; i <= 100; i++) {
                for (int j = 1; j <= 5; j++) {
                    String insert = "INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) "
                                    + "VALUES (" + String.valueOf(i) + "," + String.valueOf(r.nextInt(50) + 1)
                                    + "," + String.valueOf(r.nextInt(9) + 1) + ")";
                    s.execute(insert);
                }
            }
            for (int i = 1; i <= 20; i++) {
                String insert = "INSERT INTO ingredientsInKitchen (ingredient_id,quantity) VALUES "
                                + "(" + String.valueOf(r.nextInt(50) + 1) + ","
                                + String.valueOf(r.nextInt(9) + 1) + ")";
                s.execute(insert);
            }
            db.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
