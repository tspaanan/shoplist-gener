package shoplistgener;

//import java.util.Random;

import shoplistgener.dao.ShoplistgenerDAO;

//import java.sql.*;

public class CreateTestData {
    public static void createRandomTestData(ShoplistgenerDAO sqliteHander) {
        sqliteHander.insertTestData();
        //to solve sqlite locking problem, using the same connection as established by ShoplistgenerDAO
        //try {
            //Connection db = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
            //db.setAutoCommit(false);
            //Statement s = db.createStatement();
            
            //for (int i = 1; i <= 100; i++) {
                //String insert = "INSERT INTO recipes (name,instructions,visible) VALUES ('recipe#" + String.valueOf(i) 
                                //+ "','default_instructions_for_this_recipe',TRUE)";
                //s.execute(insert);
            //}
            //db.commit();

            //Random r = new Random();
            //String[] units = {"dl","ml","g","pcs","cl","kg"};
            //for (int i = 1; i <= 50; i++) {
                //String unit = units[r.nextInt(6)];
                //String insert = "INSERT INTO ingredients (name,unit) VALUES ('ingredient#" + String.valueOf(i)
                                //+ "','" + unit + "')";
                //s.execute(insert);
            //}
            //db.commit();

            //for (int i = 1; i <= 100; i++) {
                //for (int j = 1; j <= 5; j++) {
                    //String insert = "INSERT INTO ingredientsInRecipes (recipe_id,ingredient_id,quantity) "
                                    //+ "VALUES (" + String.valueOf(i) + "," + String.valueOf(r.nextInt(50)+1)
                                    //+ "," + String.valueOf(r.nextInt(9)+1) + ")";
                    //s.execute(insert);
                //}
            //}
            //db.commit();

            //for (int i = 1; i <= 20; i++) {
                //String insert = "INSERT INTO ingredientsInKitchen (ingredient_id,quantity) VALUES "
                                //+ "(" + String.valueOf(r.nextInt(50)+1) + ","
                                //+ String.valueOf(r.nextInt(9)+1) + ")";
                //s.execute(insert);
            //}
            //db.commit();

        //} catch (SQLException e) {
            //System.out.println(e.getMessage());
        //}
    }    
}
