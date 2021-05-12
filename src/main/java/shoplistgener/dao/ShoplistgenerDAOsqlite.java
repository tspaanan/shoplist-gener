package shoplistgener.dao;

import shoplistgener.domain.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of ShoplistgenerDAO interface for SQLITE3
 */
public class ShoplistgenerDAOsqlite implements ShoplistgenerDAO {
    private Connection db;
    public static boolean databaseIsEmpty;

    /**
     * Constructor, which also initializes the database, if one is not already present
     * @param databaseName User-defined name of the SQLITE-database, read from config.properties
     * @throws Exception
     */
    public ShoplistgenerDAOsqlite(String databaseName) throws Exception {
        if (!databaseName.isEmpty() && !new File(databaseName).isFile()) {
            this.db = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            this.db.setAutoCommit(false);
            Statement s = this.db.createStatement();
            s.execute("CREATE TABLE recipes (id INTEGER PRIMARY KEY,name TEXT UNIQUE,instructions TEXT,visible BOOLEAN,tags TEXT)");
            s.execute("CREATE TABLE ingredients (id INTEGER PRIMARY KEY,name TEXT UNIQUE,unit TEXT)");
            s.execute("CREATE TABLE ingredientsInRecipes (recipe_id INTEGER REFERENCES recipes,ingredient_id INTEGER REFERENCES ingredients,quantity INTEGER)");
            s.execute("CREATE TABLE ingredientsInKitchen (ingredient_id INTEGER REFERENCES ingredients,quantity INTEGER)");
            this.db.commit();
            databaseIsEmpty = true;
        } else if (!databaseName.isEmpty() && new File(databaseName).isFile()) {
            this.db = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            this.db.setAutoCommit(false);
            databaseIsEmpty = false;
        }
    }

    private void addAllIngredientsForRecipe(Recipe rec, int recId) throws Exception {
        for (Ingredient ing : rec.getIngredients()) {
            PreparedStatement ci = this.db.prepareStatement("SELECT 1 FROM ingredients WHERE name=?");
            ci.setString(1, ing.getName());
            ResultSet ciResults = ci.executeQuery();
            if (!ciResults.next()) {
                PreparedStatement insertIng = this.db.prepareStatement("INSERT INTO ingredients (name, unit) VALUES (?,'"
                                + ing.getUnit().toString().toLowerCase() + "')");
                insertIng.setString(1, ing.getName());
                insertIng.executeUpdate();
            }
            int ingId = this.fetchIngredientId(ing.getName());
            PreparedStatement ingIn = this.db.prepareStatement("INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) VALUES ("
                                    + Integer.toString(recId) + "," + Integer.toString(ingId) + ",?)");
            ingIn.setString(1, Integer.toString(ing.getRequestedQuantity()));
            ingIn.executeUpdate();
        }
        this.db.commit();
    }

    /**
     * Writes new ingredient to the database tracking the contents of kitchen; quantity always starts at zero
     * @param name name of the ingredient
     * @throws Exception
     */
    public void addIngredientToKitchen(String name) throws Exception {
        int id = this.fetchIngredientId(name);
        String insert = "INSERT INTO ingredientsInKitchen (ingredient_id,quantity) VALUES (" + String.valueOf(id) + ",0)";
        Statement s = this.db.createStatement();
        s.executeUpdate(insert);
        this.db.commit();
    }

    /**
     * Writes new recipe to the database
     * @param newRecipe Recipe-object
     * @throws Exception
     */
    public void addRecipe(Recipe newRecipe) throws Exception {
        PreparedStatement cs = this.db.prepareStatement("SELECT 1 FROM recipes WHERE name=?");
        cs.setString(1, newRecipe.getName());
        ResultSet ch = cs.executeQuery();
        if (ch.next()) {
            throw new Exception("Database already contains a recipe by that name!");
        }
        PreparedStatement ct = this.db.prepareStatement("INSERT INTO recipes (name,visible) VALUES (?,TRUE)");
        ct.setString(1, newRecipe.getName());
        ct.executeUpdate();
        int recId = fetchRecipeId(newRecipe.getName());
        PreparedStatement cu = this.db.prepareStatement("UPDATE recipes SET instructions=? WHERE id=" + Integer.toString(recId));
        cu.setString(1, newRecipe.getInstructions());
        cu.executeUpdate();
        this.db.commit();
        this.addAllIngredientsForRecipe(newRecipe, recId);
    }

    /**
     * Returns all ingredients in the database
     * @return List of Ingredient-objects
     * @throws Exception
     */
    public List<Ingredient> fetchAllIngredients() throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT name,unit FROM ingredients");
        ResultSet r = p.executeQuery();
        List<Ingredient> allIngredients = new ArrayList<Ingredient>();
        while (r.next()) {
            allIngredients.add(new Ingredient(r.getString("name"), Unit.valueOf(r.getString("unit").toUpperCase())));
        }
        return allIngredients;
    }

    /**
     * Returns a list of all recipe names in the database
     * @return List of Strings (names of all the recipes)
     * @throws Exception
     */
    public List<String> fetchAllRecipes() throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT name FROM recipes WHERE visible=TRUE");
        ResultSet r = p.executeQuery();
        List<String> recipeNames = new ArrayList<String>();
        while (r.next()) {
            recipeNames.add(r.getString("name"));
        }
        return recipeNames;
    }

    private int fetchIngredientId(String name) throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT id FROM ingredients WHERE name=?");
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        return r.getInt("id");
    }

    /**
     * Return all ingredients in the kitchen
     * @return List of Ingredient-objects
     * @throws Exception
     */
    public List<Ingredient> fetchKitchenIngredients() throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT I.name,I.unit,IK.quantity FROM ingredients I,"
                                                    + "ingredientsInKitchen IK WHERE IK.ingredient_id=I.id");
        ResultSet r = p.executeQuery();
        List<Ingredient> kitchenIngredients = new ArrayList<Ingredient>();
        while (r.next()) {
            kitchenIngredients.add(new Ingredient(r.getString("name"), Unit.valueOf(r.getString("unit").toUpperCase()), Integer.valueOf(r.getString("quantity"))));
        }
        return kitchenIngredients;
    }

    private List<Ingredient> fetchIngredients(int id) throws Exception {
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

    /**
     * Returns a randomized menu of recipes
     * @param days number of recipes to be returned
     * @return List of recipe-objects
     * @throws Exception
     */
    public List<Recipe> fetchMenu(int days) throws Exception {
        List<Recipe> menu = new ArrayList<Recipe>();
        for (int i = 0; i < days; i++) {
            PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions,visible FROM recipes WHERE id=?");
            int randRecipeNo = this.randomNumberForRecipes();
            if (randRecipeNo == -1) {
                break;
            }
            p.setString(1, String.valueOf(randRecipeNo));
            ResultSet r = p.executeQuery();
            if (r.getString("visible").equals("0")) {
                i--;
                continue; //if randomly selected removed recipe, simply make another random selection instead
            }
            List<Ingredient> ingsInList = this.fetchIngredients(randRecipeNo);
            Recipe rec = new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
            menu.add(rec);
        }
        return menu;
    }

    /**
     * Returns a random recipe
     * @return Recipe-object
     * @throws Exception
     */
    public Recipe fetchRandomRecipe() throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT name FROM recipes WHERE id=?");
        p.setString(1, String.valueOf(this.randomNumberForRecipes()));
        ResultSet r = p.executeQuery();
        return this.fetchRecipe(r.getString("name"));
    }

    /**
     * Returns a single recipe
     * @param name name of the recipe to be returned
     * @return Recipe-object
     * @throws Exception
     */
    public Recipe fetchRecipe(String name) throws Exception {
        PreparedStatement p = this.db.prepareStatement("SELECT id,name,instructions FROM recipes WHERE name LIKE ? AND visible=TRUE LIMIT 1");
        p.setString(1, "%" + name + "%");
        ResultSet r = p.executeQuery();
        List<Ingredient> ingsInList = this.fetchIngredients(r.getInt("id"));
        return new Recipe(r.getString("name"), r.getString("instructions"), ingsInList);
    }

    /**
     * Returns id number of a single recipe
     * @param name name of the recipe to be looked for
     * @return index number
     * @throws Exception
     */
    public int fetchRecipeId(String name) throws Exception {
        try {
            PreparedStatement p = this.db.prepareStatement("SELECT id FROM recipes WHERE name=? AND visible=TRUE");
            p.setString(1, name);
            ResultSet r = p.executeQuery();
            return r.getInt("id");
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Inserts data meant for testing the program into the database
     * @throws Exception 
     */
    public void insertTestData() throws Exception {
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
        this.db.commit();
    }
    /**
     * Modifies data related to an existing recipe in the database
     * @param modifiedRecipe Recipe-object
     * @throws Exception
     */
    public void modifyRecipe(Recipe modifiedRecipe) throws Exception {
        PreparedStatement p = this.db.prepareStatement("UPDATE recipes SET instructions=? WHERE name='" + modifiedRecipe.getName() + "'");
        p.setString(1, modifiedRecipe.getInstructions());
        p.executeUpdate();
        this.db.commit();
        int recipeId = this.fetchRecipeId(modifiedRecipe.getName());
        PreparedStatement rm = this.db.prepareStatement("DELETE FROM ingredientsInRecipes WHERE recipe_id=?");
        rm.setString(1, String.valueOf(recipeId));
        rm.executeUpdate();
        this.db.commit();
        this.addAllIngredientsForRecipe(modifiedRecipe, recipeId);
    }

    private int randomNumberForRecipes() throws Exception {
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

    /**
     * Removes an ingredient from the kitchen
     * @param name name of the ingredient to be removed
     * @throws Exception
     */
    public void removeIngredient(String name) throws Exception {
        int id = this.fetchIngredientId(name);
        String insert = "DELETE FROM ingredientsInKitchen WHERE ingredient_id=" + Integer.toString(id);
        Statement s = this.db.createStatement();
        s.executeUpdate(insert);
        this.db.commit();
    }

    /**
     * Marks recipe as removed from the database
     * @param name name of the recipe to be removed
     * @throws Exception
     */
    public void removeRecipe(String name) throws Exception {
        PreparedStatement p = this.db.prepareStatement("UPDATE recipes SET visible=FALSE WHERE name=?");
        p.setString(1, name);
        p.executeUpdate();
        this.db.commit();
    }

    /**
     * Updates the quantity of an ingredient in the kitchen
     * @param name name of the ingredient
     * @param quantity new quantity of the ingredient
     * @throws Exception
     */
    public void updateIngredientQuantityInKitchen(String name, Integer quantity) throws Exception {
        int id = this.fetchIngredientId(name);
        PreparedStatement p = this.db.prepareStatement("UPDATE ingredientsInKitchen SET quantity=? WHERE ingredient_id=" + Integer.toString(id));
        p.setString(1, Integer.toString(quantity));
        p.executeUpdate();
        this.db.commit();
    }
}
