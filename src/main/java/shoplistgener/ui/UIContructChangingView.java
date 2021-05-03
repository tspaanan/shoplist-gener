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

public class UIContructChangingView {
    private HBox components;

    public UIContructChangingView() {
        this.components = new HBox();
        this.components.setSpacing(10);
        this.components.setPadding(new Insets(10, 10, 10, 10));
    }

    public HBox createEditView(String name, ShoplistgenerService domainHandler, TextField recipeName, TextField recipeInstructions, ObservableList<String> ingredientItems,
                                ListView<String> ingredientItemView, List<String> listOfIngredients, TextField ingredientName, TextField ingredientQuantity,
                                GridPane editSceneGridPane, Button addNewRecipeButton, Button modifyExistingRecipeButton) throws Exception {
        recipeName.clear();
        recipeInstructions.clear();
        listOfIngredients.clear();
        ingredientItems.clear();
        ingredientItems.add("Ingredients:");
        if (!name.equals("")) {
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
            System.out.println(ingredientItems);
            System.out.println(listOfIngredients);
            editSceneGridPane.getChildren().removeAll(addNewRecipeButton, modifyExistingRecipeButton);
            editSceneGridPane.add(modifyExistingRecipeButton, 3, 3);
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

    public HBox createErrorView(String message) {
        Label errorMessage = new Label();
        errorMessage.setText(message);
        this.components.getChildren().clear();
        this.components.getChildren().add(errorMessage);
        return this.components;
    }

    public HBox createMenuView(String newCourses, String newShoppingList, Label listMenu, Label listShoppingList, ObservableList<String> menuItems,
                                HBox menuPlacement) throws Exception {
        listMenu.setText("Menu:\n" + newCourses);
        listShoppingList.setText("Shopping List:\n" + newShoppingList);
        menuItems.setAll(listMenu.getText().split("\\n"));
        this.components.getChildren().clear();
        this.components.getChildren().addAll(menuPlacement, listShoppingList);
        return this.components;
    }

    public HBox createRecipeView(ScrollPane listRecipes, VBox recipeSearchOptions, Label showRecipe, VBox recipeModifications) throws Exception {
        this.components.getChildren().clear();
        this.components.getChildren().addAll(listRecipes, recipeSearchOptions, recipeModifications);
        return this.components;
    }
}
