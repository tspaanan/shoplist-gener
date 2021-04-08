package defaultPackage.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import defaultPackage.dao.shoplistgenerDAO;

public class shoplistgenerService {
    private shoplistgenerDAO daoHandler;

    public shoplistgenerService(shoplistgenerDAO daoHandler) {
        this.daoHandler = daoHandler;
    }

    public List<String> fetchMenu() throws Exception {
        List<String> menuToString = new ArrayList<String>();
        List<Recipe> menu = this.daoHandler.fetchMenu(7);
        List<Ingredient> ingredientsAll = new ArrayList<Ingredient>();
        for (Recipe rec : menu) {
            //System.out.println(rec);
            //ArrayList<String> recToString = new ArrayList<String>();
            menuToString.add(rec.getName()); //recipe instructions are not included at this point
            //recToString.add(rec.getInstructions());
            List<Ingredient> ingredients = rec.getIngredients();
            for (Ingredient ing : ingredients) {
                ingredientsAll.add(ing);
            }
            //menuToString.add(recToString);
        }
        List<Ingredient> ingredientsSorted = this.sortIngredients(ingredientsAll);
        menuToString.add("");
        for (Ingredient ing : ingredientsSorted) {menuToString.add(ing.toString());}
        return menuToString;
    }

    public List<Ingredient> sortIngredients(List<Ingredient> ingredients) {
        List<Ingredient> ingredientsSorted = ingredients.stream()
                                                //.distinct() not useful here
                                                .sorted()
                                                .collect(Collectors.toCollection(ArrayList::new));
        //System.out.println(ingredientsSorted.size());
        List<Ingredient> combinedIngredients = new ArrayList<Ingredient>();
        Ingredient previous = new Ingredient("", Unit.CL, 0);
        for (int i = 1; i < ingredientsSorted.size(); i++) {
            if (ingredientsSorted.get(i).equals(ingredientsSorted.get(i-1))) {
                if (ingredientsSorted.get(i).equals(previous)) {
                    combinedIngredients.get(combinedIngredients.size() - 1).setRequestedQuantity(ingredientsSorted.get(i).getRequestedQuantity() + combinedIngredients.get(combinedIngredients.size() - 1).getRequestedQuantity());
                    //maybe fix the above line so more readable
                    continue;
                }
                combinedIngredients.add(Ingredient.combineIngredients(ingredientsSorted.get(i), ingredientsSorted.get(i-1)));
                previous = ingredientsSorted.get(i-1);
            }
            else {
                if (!combinedIngredients.contains(ingredientsSorted.get(i - 1))) {
                    combinedIngredients.add(ingredientsSorted.get(i - 1));}
                }
            }
        // add the last ingredient only if not duplicate
        if (!ingredientsSorted.get(ingredientsSorted.size() - 1).equals(combinedIngredients.get(combinedIngredients.size() - 1))) {
            combinedIngredients.add(ingredientsSorted.get(ingredientsSorted.size() - 1));
        }
        //Iterator<Ingredient> iteratorFirst = ingredientsSorted.iterator();
        //Iterator<Ingredient> iteratorSecond = ingredientsSorted.iterator();
        //iteratorSecond.next();
        //while (iteratorSecond.hasNext()) {
            //Ingredient first = iteratorFirst.next();
            //Ingredient second = iteratorSecond.next();
            //if (first.equals(second)) {
                //Ingredient combinedIng = new Ingredient(first.getName(), first.getUnit(), first.getRequestedQuantity() + second.getRequestedQuantity());
                //combinedIngredients.add(combinedIng);
                //iteratorFirst.remove();
            //}
        return combinedIngredients;
    }
}