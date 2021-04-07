package defaultPackage.domain;

import java.util.ArrayList;
import java.util.List;
import defaultPackage.dao.shoplistgenerDAO;

public class shoplistgenerService {
    private shoplistgenerDAO daoHandler;

    public shoplistgenerService(shoplistgenerDAO daoHandler) {
        this.daoHandler = daoHandler;
    }

    public List<ArrayList<String>> fetchMenu() throws Exception {
        List<ArrayList<String>> menuToString = new ArrayList<ArrayList<String>>();
        List<Recipe> menu = this.daoHandler.fetchMenu(7);
        for (Recipe rec : menu) {
            //System.out.println(rec);
            ArrayList<String> recToString = new ArrayList<String>();
            recToString.add(rec.getName());
            recToString.add(rec.getInstructions());
            List<Ingredient> ingredients = rec.getIngredients();
            for (Ingredient ing : ingredients) {
                recToString.add(ing.toString());
            }
            menuToString.add(recToString);
        }
        return menuToString;
    }
}