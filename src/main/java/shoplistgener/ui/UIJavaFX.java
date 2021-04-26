package shoplistgener.ui;

import shoplistgener.CreateTestData;
import shoplistgener.dao.*;
import shoplistgener.domain.Ingredient;
import shoplistgener.domain.ShoplistgenerService;
import shoplistgener.domain.Unit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
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
    
    private String databaseName;
    private ShoplistgenerDAO sqliteHandler;
    private ShoplistgenerService domainHandler;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        
        //debug
        //System.out.println(properties.getProperty("databaseName"));
        //if (properties.getProperty("databaseName").isEmpty()) {
            //System.out.println("tyhjä");
        //}
        //if (!new File(properties.getProperty("databaseName")).isFile()) {
            //System.out.println("Ei tiedostoa");
        //}
        
        databaseName = properties.getProperty("databaseName");
        sqliteHandler = new ShoplistgenerDAOsqlite(properties.getProperty("databaseName")); //should this DAO be injected to domainHandler at all?
        domainHandler = new ShoplistgenerService(sqliteHandler);
    }
    
    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("shoplist-gener");
        
        Label listMenu = new Label();
        ObservableList<String> menuItems = FXCollections.observableArrayList();
        ListView<String> menuItemView = new ListView<String>(menuItems);
        menuItemView.setPrefSize(100, 50);
        //menuItemView.setVisible(false);
        Button randomizeCourse = new Button("Randomize selected course");
        //randomizeCourse.setVisible(false);
        Button changeCourse = new Button("Replace selected course");
        //changeCourse.setVisible(false);
        TextField changeCourseSearchField = new TextField("choose new course (exact name for now)");
        HBox menuPlacement = new HBox();
        VBox menuPlacement2ndColumn = new VBox();
        menuPlacement.getChildren().addAll(menuItemView, menuPlacement2ndColumn);
        menuPlacement2ndColumn.getChildren().addAll(randomizeCourse, changeCourse, changeCourseSearchField);
        menuPlacement.setVisible(false);

        Label listShoppingList = new Label();
        Label listRecipes = new Label();
        Button removeRecipe = new Button("Remove this recipe");
        removeRecipe.setVisible(false);

        Button wholeMenu = new Button("Print Week's Menu and shopping list");
        wholeMenu.setOnAction((event) -> {
            try {
                String newCourses = domainHandler.fetchCourses();
                String newShoppingList = domainHandler.fetchShoppingList();
                listMenu.setText("Menu:\n" + newCourses);
                listShoppingList.setText("Shopping List:\n" + newShoppingList);
                menuItems.setAll(listMenu.getText().split("\\n"));
                //menuItemView.setVisible(true);
                //randomizeCourse.setVisible(true);
                menuPlacement.setVisible(true);
                //System.out.println(menuItems.toString());
            } catch (Exception e) {
                //this ResultSet was closed error has been dealt with    
                //listShoppingList.setText("\n\nMysterious 'ResultSet was closed' error just happened. Not to worry:\n"
                //                        + "simply make your choice again: it has never occurred twice in a row...");
                listRecipes.setText("no recipes in database");
                System.out.println(e.getMessage());
            }
        });

        Button allRecipes = new Button("Print All Recipes");
        allRecipes.setOnAction((event) -> {
            try {
                String allRecipesFetched = domainHandler.fetchRecipe("");
                if (allRecipesFetched.equals("All Recipes:\n\n")) {
                    listRecipes.setText("no recipes in database");
                } else {
                    listRecipes.setText(allRecipesFetched);
                }
                removeRecipe.setVisible(false);
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
                removeRecipe.setVisible(true);
            } catch (Exception e) {
                listRecipes.setText("error: no recipe by that name");
            }
        });

        removeRecipe.setOnAction((event) -> {
            try {
                domainHandler.removeRecipe(searchText.getText());
                listRecipes.setText("Recipe was removed!");
                removeRecipe.setVisible(false);
            } catch (Exception e) {
                listRecipes.setText("error: recipe could not be removed");
                removeRecipe.setVisible(false);
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

        //labelPlacement.getChildren().add(listMenu);
        labelPlacement.getChildren().add(menuPlacement);
        labelPlacement.getChildren().add(listShoppingList);
        labelPlacement.getChildren().add(listRecipes);
        labelPlacement.getChildren().add(removeRecipe);

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

        randomizeCourse.setOnAction((event) -> {
            this.changeSingleCourse(true, menuItemView, listMenu, listShoppingList, menuItems, changeCourseSearchField);
            //try {
               //§String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
               //§String changedMenu = domainHandler.changeCourse(changedCourse);
               //§String changedShoppinglist = domainHandler.fetchShoppingList();
               //§listMenu.setText("Menu:\n" + changedMenu);
               //§listShoppingList.setText("Shopping List:\n" + changedShoppinglist);
               //§menuItems.setAll(listMenu.getText().split("\\n"));
            //} catch (Exception e) {
               //§listMenu.setText("\n\nMysterious 'ResultSet was closed' error just happened. Not to worry:\n"
                                   //§+ "simply make your choice again: it has never occurred twice in a row...");
               //§listShoppingList.setText("");
            //§}
        });

        changeCourse.setOnAction((event) -> {
            this.changeSingleCourse(false, menuItemView, listMenu, listShoppingList, menuItems, changeCourseSearchField);
        });
        
        window.setScene(mainScene);
        window.show();

        TextInputDialog setDatabaseNameDialog = new TextInputDialog("Test.db");
        setDatabaseNameDialog.setTitle("Set Database Name");
        setDatabaseNameDialog.setHeaderText("No Database Found: Set New Database");
        setDatabaseNameDialog.setContentText("Provide new name for database");
        Alert chooseDatabasePopulationDialog = new Alert(AlertType.CONFIRMATION);
        chooseDatabasePopulationDialog.setTitle("Choose How to Populate Database");
        chooseDatabasePopulationDialog.setHeaderText("Database is Currently Empty");
        chooseDatabasePopulationDialog.setContentText("Choose to populate database with test data or start with an empty database");
        ButtonType testDataBT = new ButtonType("Test Data");
        ButtonType emptyBT = new ButtonType("Empty Database");
        chooseDatabasePopulationDialog.getButtonTypes().setAll(testDataBT, emptyBT);
        while (databaseName.isEmpty() || !new File(databaseName).isFile()) {
            Optional<String> newDatabaseName = setDatabaseNameDialog.showAndWait();
            if (newDatabaseName.isPresent()) {
                databaseName = newDatabaseName.get();
                sqliteHandler = new ShoplistgenerDAOsqlite(databaseName);
                domainHandler = new ShoplistgenerService(sqliteHandler);
                FileWriter databaseNameWriter = new FileWriter(new File("config.properties"));
                databaseNameWriter.write("databaseName=" + databaseName);
                databaseNameWriter.close();
                Optional<ButtonType> chosenBT = chooseDatabasePopulationDialog.showAndWait();
                if (chosenBT.get() == testDataBT) {
                    CreateTestData.createRandomTestData(databaseName);
                }
            }
        }
    }

    private void changeSingleCourse(boolean randomized, ListView<String> menuItemView, Label listMenu, Label listShoppingList, ObservableList<String> menuItems, TextField changeCourseTextField) {
        try {
            String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
            String changedMenu = "";
            if (randomized) {
                changedMenu = domainHandler.changeCourse(changedCourse, randomized, "");
            } else {
                changedMenu = domainHandler.changeCourse(changedCourse, randomized, changeCourseTextField.getText());
            }
            String changedShoppinglist = domainHandler.fetchShoppingList();
            listMenu.setText("Menu:\n" + changedMenu);
            listShoppingList.setText("Shopping List:\n" + changedShoppinglist);
            menuItems.setAll(listMenu.getText().split("\\n"));
        } catch (Exception e) {
            listMenu.setText("\n\nMysterious 'ResultSet was closed' error just happened. Not to worry:\n"
                                + "simply make your choice again: it has never occurred twice in a row...");
            listShoppingList.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
