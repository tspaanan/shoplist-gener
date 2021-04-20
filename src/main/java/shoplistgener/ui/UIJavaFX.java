package shoplistgener.ui;

import shoplistgener.dao.*;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.ShoplistgenerService;
import shoplistgener.domain.Unit;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIJavaFX extends Application {
    
    private ShoplistgenerDAO sqliteHandler;
    private ShoplistgenerService domainHandler;

    @Override
    public void init() {
    sqliteHandler = new ShoplistgenerDAOsqlite(); //should this DAO be injected to domainHandler at all?
    domainHandler = new ShoplistgenerService(sqliteHandler);
    }
    
    @Override
    public void start(Stage window) {
        window.setTitle("shoplist-gener");
        
        Label listMenu = new Label();
        Label listShoppingList = new Label();
        Label listRecipes = new Label();

        Button wholeMenu = new Button("Print Week's Menu and shopping list");
        wholeMenu.setOnAction((event) -> {
            try {
                String newCourses = domainHandler.fetchCourses();
                String newShoppingList = domainHandler.fetchShoppingList();
                listMenu.setText("Menu:\n" + newCourses);
                listShoppingList.setText("Shopping List:\n" + newShoppingList);
            } catch (Exception e) {
                    listMenu.setText("\n\nMysterious 'ResultSet was closed' error just happened. Not to worry:\n"
                                        + "simply make your choice again: it has never occurred twice in a row...");
                    listShoppingList.setText("");
            }
        });

        Button allRecipes = new Button("Print All Recipes");
        allRecipes.setOnAction((event) -> {
            try {
                listRecipes.setText(domainHandler.fetchRecipe(""));
                //debug
                //List<String> testList = new ArrayList<String>();
                //testList.add("uusi_resepti"); testList.add("Uudet_ohjeet"); testList.add("000;dl;222");
                //domainHandler.addRecipe(testList);
            } catch (Exception e) {
                listRecipes.setText("Exception occured (but shouldn't have)");
            }
        });

        TextField searchText = new TextField("...use exact name (for now)");
        Button searchRecipes = new Button("Search for a recipe");
        searchRecipes.setOnAction((event) -> {
            try {
                String searchString = searchText.getText();
                listRecipes.setText(domainHandler.fetchRecipe(searchString));
            } catch (Exception e) {
                listRecipes.setText("error: no recipe by that name");
            }
        });

        Button addRecipe = new Button("Add a new recipe");

        HBox labelPlacement = new HBox();
        labelPlacement.setSpacing(10);
        labelPlacement.setPadding(new Insets(10,300,10,10));
        VBox buttonPlacement = new VBox();
        buttonPlacement.setSpacing(10);
        
        buttonPlacement.getChildren().add(wholeMenu);
        buttonPlacement.getChildren().add(allRecipes);
        buttonPlacement.getChildren().add(searchText);
        buttonPlacement.getChildren().add(searchRecipes);
        buttonPlacement.getChildren().add(addRecipe);

        labelPlacement.getChildren().add(listMenu);
        labelPlacement.getChildren().add(listShoppingList);
        labelPlacement.getChildren().add(listRecipes);

        BorderPane elementPlacement = new BorderPane();
        elementPlacement.setRight(labelPlacement);
        elementPlacement.setLeft(buttonPlacement);
        elementPlacement.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        
        // addRecipeScene-komponentit
        TextField newRecipeName = new TextField("Recipe name");
        TextField newRecipeInstructions = new TextField("Instructions for the recipe");
        HBox newRecipeFields = new HBox();
        newRecipeFields.setSpacing(10);
        newRecipeFields.getChildren().addAll(newRecipeName, newRecipeInstructions);
        Label newIngredientsInList = new Label();
        //newRecipeFields.getChildren().add(newIngredientsInList);
        List<Ingredient> listOfNewIngredients = new ArrayList<Ingredient>(); //mietitään Ingredient-olion käyttöä vielä tässä

        TextField newIngredientName = new TextField("Ingredient name");
        TextField newIngredientQuantity = new TextField("Ingredient quantity");
        ObservableList<Unit> newIngredientUnit = FXCollections.observableArrayList(Unit.values());
        ListView<Unit> listView = new ListView<Unit>(newIngredientUnit);
        listView.setPrefSize(80, 30);
        Button addNewIngredient = new Button("Add new ingredient");
        Button addNewRecipeButton = new Button("Add new recipe");
        HBox newIngredientFields = new HBox();
        newIngredientFields.getChildren().addAll(newIngredientName, newIngredientQuantity, listView);
        GridPane addRecipeSceneGridpane = new GridPane();
        addRecipeSceneGridpane.add(newRecipeFields, 2, 1);
        addRecipeSceneGridpane.add(newIngredientFields, 2, 2);
        addRecipeSceneGridpane.add(addNewIngredient, 2, 3);
        addRecipeSceneGridpane.add(addNewRecipeButton, 3, 3);
        addRecipeSceneGridpane.add(newIngredientsInList, 2, 4);
        addRecipeSceneGridpane.setAlignment(Pos.CENTER_LEFT);
        
        Scene mainScene = new Scene(elementPlacement);
        //viewPort.setFill(Color.BLACK); //does nothing?
        Scene addRecipeScene = new Scene(addRecipeSceneGridpane);
        
        addRecipe.setOnAction((event) -> {
           window.setScene(addRecipeScene); 
            newIngredientsInList.setText("\nIngredients:");
            listOfNewIngredients.clear();
        });
        
        addNewRecipeButton.setOnAction((event) -> {
            List<String> newRecipeParts = new ArrayList<String>();
            newRecipeParts.add(newRecipeName.getText());
            newRecipeParts.add(newRecipeInstructions.getText());
            for (Ingredient ing : listOfNewIngredients) {
                newRecipeParts.add(ing.getName() + ";" + ing.getUnit().toString().toLowerCase() + ";" + ing.getRequestedQuantity());
            }
            boolean bTarkistus = domainHandler.addRecipe(newRecipeParts);
            //TODO: siisti tätä
            if (!bTarkistus) {
                listRecipes.setText("error in adding recipe");
            } else {
                try {
                listRecipes.setText(domainHandler.fetchRecipe(newRecipeParts.get(0)));
                } catch (Exception e) {
                    listRecipes.setText("error fetching recipe after successful creation");
                }
            };
            window.setScene(mainScene);
        });

        addNewIngredient.setOnAction((event) -> {
            String oldList = newIngredientsInList.getText();
            String newList = oldList + "\n" + newIngredientName.getText() + " " + newIngredientQuantity.getText()
                            + " " + listView.getSelectionModel().getSelectedItem().toString().toLowerCase();
            listOfNewIngredients.add(new Ingredient(newIngredientName.getText(), Unit.valueOf(listView.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(newIngredientQuantity.getText())));
            newIngredientsInList.setText(newList);
        });
        
        window.setScene(mainScene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
