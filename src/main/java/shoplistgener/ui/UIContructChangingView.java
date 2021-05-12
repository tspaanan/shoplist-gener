package shoplistgener.ui;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shoplistgener.domain.ShoplistgenerService;

/**
 * Returns HBox-objects that represent the changing view on the right side of the window
 */
public class UIContructChangingView {
    private HBox components;

    /**
     * Constructor, initializes HBox-object that is returned
     */
    public UIContructChangingView() {
        this.components = new HBox();
        this.components.setSpacing(10);
        this.components.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Creates the view for editing recipes
     * @param name pre-fetches recipe parts if not ""
     * @param domainHandler object containing application logic
     * @param recipeName pre-fetched recipe name
     * @param recipeInstructions pre-fetched recipe instructions 
     * @param ingredientItems pre-fetched ingredients
     * @param ingredientItemView JavaFX component for viewing pre-fetched ingredients
     * @param listOfIngredients pre-fetched ingredients
     * @param ingredientName pre-fetched ingredient name(s)
     * @param ingredientQuantity pre-fetched ingredient quanti
     * @param editSceneGridPane JavaFX component for placement of other components
     * @param addNewRecipeButton for adding new recipe
     * @param modifyExistingRecipeButton for modifying existing recipe
     * @return HBox-object representing the changing view on the right
     * @throws Exception
     */
    public HBox createEditView(String name, ShoplistgenerService domainHandler, TextField recipeName, TextField recipeInstructions, ObservableList<String> ingredientItems,
                                ListView<String> ingredientItemView, List<String> listOfIngredients, TextField ingredientName, TextField ingredientQuantity,
                                GridPane editSceneGridPane, Button addNewRecipeButton, Button modifyExistingRecipeButton) throws Exception {
        recipeName.setText("Recipe name");
        recipeInstructions.setText("Recipe instructions");
        listOfIngredients.clear();
        ingredientItems.clear();
        ingredientItems.add("Ingredients:");
        if (!name.equals("")) {
            this.populateEditView(name, domainHandler, recipeName, recipeInstructions, ingredientItems, listOfIngredients, editSceneGridPane,
                                    addNewRecipeButton, modifyExistingRecipeButton);
        } else {
            editSceneGridPane.getChildren().removeAll(addNewRecipeButton, modifyExistingRecipeButton);
            editSceneGridPane.add(addNewRecipeButton, 3, 3);
        }
        ingredientName.clear();
        ingredientQuantity.clear();
        this.components.getChildren().clear();
        this.components.getChildren().addAll(editSceneGridPane);
        return this.components;
    }

    private void populateEditView(String name, ShoplistgenerService domainHandler, TextField recipeName, TextField recipeInstructions,
                                    ObservableList<String> ingredientItems, List<String> listOfIngredients, GridPane editSceneGridPane,
                                    Button addNewRecipeButton, Button modifyExistingRecipeButton) throws Exception {
        List<String> modifiedRecipeInList = domainHandler.fetchRecipeList(name);
        recipeName.setText(modifiedRecipeInList.get(0));
        recipeInstructions.setText(modifiedRecipeInList.get(1));
        for (String ing : modifiedRecipeInList.get(2).split("\n")) {
            if (ing.isEmpty()) {
                continue;
            }
            String toWhitespace = ing.replace(";", " ");
            ingredientItems.add(toWhitespace);
            listOfIngredients.add(ing);
        }
        editSceneGridPane.getChildren().removeAll(addNewRecipeButton, modifyExistingRecipeButton);
        editSceneGridPane.add(modifyExistingRecipeButton, 3, 3);
    }

    /**
     * Creates the view for error messages
     * @param message error message to be displayed
     * @return HBox-object representing the changing view on the right
     */
    public HBox createErrorView(String message) {
        Label errorMessage = new Label();
        errorMessage.setText(message);
        this.components.getChildren().clear();
        this.components.getChildren().add(errorMessage);
        return this.components;
    }

    /**
     * Creates the view for Menu
     * @param newCourses names of the courses
     * @param newShoppingList names and quantities of the ingredients
     * @param listMenu JavaFX component for viewing Menu
     * @param listShoppingList JavaFX component for viewing shopping list
     * @param menuItems list of the courses
     * @param menuPlacement JavaFX component for placement of other components
     * @return HBox-object representing the changing view on the right
     * @throws Exception
     */
    public HBox createMenuView(String newCourses, String newShoppingList, Label listMenu, Label listShoppingList, ObservableList<String> menuItems,
                                HBox menuPlacement) throws Exception {
        listMenu.setText("Menu:\n" + newCourses);
        listShoppingList.setText("Shopping List:\n" + newShoppingList);
        menuItems.setAll(listMenu.getText().split("\\n"));
        this.components.getChildren().clear();
        this.components.getChildren().addAll(menuPlacement, listShoppingList);
        return this.components;
    }

    /**
     * Creates the view for viewing recipes
     * @param listRecipes list of all the recipes in the database
     * @param recipeSearchOptions JavaFX component holding other components for recipe searches
     * @param showRecipe space for displaying a single recipe
     * @param recipeModifications JavaFX component holding other components for recipe modifications
     * @return HBox-object representing the changing view on the right
     * @throws Exception
     */
    public HBox createRecipeView(ScrollPane listRecipes, VBox recipeSearchOptions, Label showRecipe, VBox recipeModifications) throws Exception {
        this.components.getChildren().clear();
        this.components.getChildren().addAll(listRecipes, recipeSearchOptions, recipeModifications);
        return this.components;
    }

    public HBox createKitchenView(ScrollPane listAllIngredients, Button addselectedIngredientToKitchen, ScrollPane listIngredientsInKitchen, Button removeSelectedIngredientInKitchen) {
        this.components.getChildren().clear();
        this.components.getChildren().addAll(listAllIngredients, addselectedIngredientToKitchen, listIngredientsInKitchen, removeSelectedIngredientInKitchen);
        return this.components;
    }
}
