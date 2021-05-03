package shoplistgener.ui;

import shoplistgener.CreateTestData;
import shoplistgener.dao.*;
import shoplistgener.domain.*;

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
import javafx.scene.control.ScrollPane;
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
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIJavaFX extends Application {
    
    private String databaseName;
    private ShoplistgenerDAO sqliteHandler;
    private ShoplistgenerService domainHandler;
    private HBox currentMenu;

    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        //TODO: create config.properties if one does not exist
        properties.load(new FileInputStream("config.properties"));
        databaseName = properties.getProperty("databaseName");
        sqliteHandler = new ShoplistgenerDAOsqlite(properties.getProperty("databaseName"));
        domainHandler = new ShoplistgenerService(sqliteHandler);
        currentMenu = new HBox();
    }
    
    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("shoplist-gener");
        
        //sceneChange components
        Button wholeMenu = new Button("Print Week's Menu and shopping list");
        Button viewRecipes = new Button("View recipes");
        Button editRecipe = new Button("Add new recipe");
        VBox viewChoiceButtons = new VBox();
        viewChoiceButtons.setSpacing(10);
        viewChoiceButtons.setPadding(new Insets(10, 10, 10, 10));
        viewChoiceButtons.getChildren().addAll(wholeMenu, viewRecipes, editRecipe);

        //menuScene components
        Label listMenu = new Label();
        ObservableList<String> menuItems = FXCollections.observableArrayList();
        ListView<String> menuItemView = new ListView<String>(menuItems);
        menuItemView.setPrefSize(100, 50);
        Button quickViewCourse = new Button("View selected course");
        Alert quickViewRecipe = new Alert(AlertType.INFORMATION);
        Button randomizeCourse = new Button("Randomize selected course");
        Button changeCourse = new Button("Replace selected course");
        TextField changeCourseSearchField = new TextField("choose new course (exact name for now)");
        Label listShoppingList = new Label();
        HBox menuPlacement = new HBox();
        VBox menuPlacement2ndColumn = new VBox();
        menuPlacement.getChildren().addAll(menuItemView, menuPlacement2ndColumn);
        menuPlacement2ndColumn.getChildren().addAll(quickViewCourse, randomizeCourse, changeCourse, changeCourseSearchField);

        //recipeScene components
        ScrollPane listRecipes = new ScrollPane();
        listRecipes.setFitToWidth(true); //does nothing?
        ObservableList<String> allRecipesInList = FXCollections.observableArrayList();
        ListView<String> allRecipesListView = new ListView<String>(allRecipesInList);
        Label showRecipe = new Label();
        Button modifyRecipe = new Button("Modify this recipe");
        Button searchRecipeSelection = new Button("View selected recipe");
        TextField searchText = new TextField("...use exact name (for now)");
        Button searchRecipeName = new Button("Search recipe by name");
        Button removeRecipe = new Button("Remove this recipe");
        Button addRecipeFromRecipeScene = new Button("Add new recipe");
        VBox recipeSearchOptions = new VBox();
        recipeSearchOptions.getChildren().addAll(searchRecipeSelection, searchText, searchRecipeName, showRecipe);
        VBox recipeModifications = new VBox();
        recipeModifications.getChildren().addAll(addRecipeFromRecipeScene, removeRecipe, modifyRecipe);
        
        //editScene components
        Alert recipeModInfo = new Alert(AlertType.INFORMATION);
        TextField newRecipeName = new TextField("Recipe name");
        TextField newRecipeInstructions = new TextField("Instructions for the recipe");
        ObservableList<String> ingredientItems = FXCollections.observableArrayList();
        ListView<String> ingredientItemView = new ListView<String>(ingredientItems);
        List<String> listOfNewIngredients = new ArrayList<String>();
        TextField newIngredientName = new TextField("Ingredient name");
        TextField newIngredientQuantity = new TextField("Ingredient quantity");
        ObservableList<Unit> newIngredientUnit = FXCollections.observableArrayList(Unit.values());
        ListView<Unit> listUnit = new ListView<Unit>(newIngredientUnit);
        listUnit.setPrefSize(80, 30);
        Button addNewIngredient = new Button("Add new ingredient");
        Button removeSelectedIngredient = new Button("Remove selected ingredient");
        Button addNewRecipeButton = new Button("Add new recipe");
        Button modifyExistingRecipeButton = new Button("Modify recipe");
        HBox newRecipeFields = new HBox();
        newRecipeFields.setSpacing(10);
        newRecipeFields.getChildren().addAll(newRecipeName, newRecipeInstructions);
        HBox newIngredientFields = new HBox();
        newIngredientFields.getChildren().addAll(newIngredientName, newIngredientQuantity, listUnit);
        GridPane editSceneGridPane = new GridPane();
        editSceneGridPane.add(newRecipeFields, 2, 1);
        editSceneGridPane.add(newIngredientFields, 2, 2);
        editSceneGridPane.add(addNewIngredient, 2, 3);
        editSceneGridPane.add(ingredientItemView, 2, 5);
        editSceneGridPane.add(removeSelectedIngredient, 2, 6);
        editSceneGridPane.setAlignment(Pos.CENTER);

        //mainScene parent components
        UIContructChangingView newViews = new UIContructChangingView();
        BorderPane changingView = new BorderPane();
        BorderPane elementPlacement = new BorderPane();
        elementPlacement.setCenter(changingView);
        elementPlacement.setLeft(viewChoiceButtons);
        elementPlacement.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        
        //lambdas for mainScene button presses
        wholeMenu.setOnAction((event) -> {
            try {
                String newCourses = domainHandler.fetchCourses();
                String newShoppingList = domainHandler.fetchShoppingList();
                currentMenu = newViews.createMenuView(newCourses, newShoppingList, listMenu, listShoppingList, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        viewRecipes.setOnAction((event) -> {
            try {
                String allRecipesFetched = domainHandler.fetchRecipe("");
                if (allRecipesFetched.equals("All Recipes:\n\n")) {
                    listRecipes.setContent(new Text("no recipes in database"));
                } else {
                    for (String recipeName : allRecipesFetched.split("\n")) {
                        allRecipesInList.add(recipeName);
                    }
                    listRecipes.setContent(allRecipesListView);
                }
                changingView.setCenter(newViews.createRecipeView(listRecipes, recipeSearchOptions, showRecipe, recipeModifications));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        editRecipe.setOnAction((event) -> {
            try {
                changingView.setCenter(newViews.createEditView("", domainHandler, newRecipeName, newRecipeInstructions, ingredientItems, ingredientItemView,
                                                                listOfNewIngredients, newIngredientName, newIngredientQuantity, editSceneGridPane,
                                                                addNewRecipeButton, modifyExistingRecipeButton));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        //lambdas for menuScene button presses
        randomizeCourse.setOnAction((event) -> {
            try {
                String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
                String newCourses = this.domainHandler.changeCourse(true, changedCourse, "");
                String newShoppingList = domainHandler.fetchShoppingList();
                currentMenu = newViews.createMenuView(newCourses, newShoppingList, listMenu, listShoppingList, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        changeCourse.setOnAction((event) -> {
            try {
                String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
                String newCourseName = changeCourseSearchField.getText();
                String newCourses = this.domainHandler.changeCourse(false, changedCourse, newCourseName);
                String newShoppingList = domainHandler.fetchShoppingList();
                currentMenu = newViews.createMenuView(newCourses, newShoppingList, listMenu, listShoppingList, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        quickViewCourse.setOnAction((event) -> {
            try {
            String selectedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
            List<String> recipeInList = domainHandler.fetchRecipeList(selectedCourse);
            quickViewRecipe.setTitle(recipeInList.get(0));
            quickViewRecipe.setContentText(recipeInList.get(1));
            quickViewRecipe.setHeaderText(recipeInList.get(2));
            quickViewRecipe.showAndWait();
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });
        
        //lambdas for recipeScene button presses
        searchRecipeSelection.setOnAction((event) -> {
            try {
                String selectedRecipe = allRecipesListView.getSelectionModel().getSelectedItem().toString();
                List<String> recipeInList = domainHandler.fetchRecipeList(selectedRecipe);
                showRecipe.setText(String.join("\n\n", recipeInList));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        searchRecipeName.setOnAction((event) -> {
            try {
                String searchedRecipe = searchText.getText();
                List<String> recipeInList = domainHandler.fetchRecipeList(searchedRecipe);
                showRecipe.setText(String.join("\n\n", recipeInList));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        removeRecipe.setOnAction((event) -> {
            try {
                String removedRecipe = showRecipe.getText().split("\n\n")[0];
                domainHandler.removeRecipe(removedRecipe);
                allRecipesInList.remove(removedRecipe);
                showRecipe.setText("\n\nrecipe was removed");
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });
        
        modifyRecipe.setOnAction((event) -> {
            try {
                String modifiableRecipe = showRecipe.getText().split("\n\n")[0];
                changingView.setCenter(newViews.createEditView(modifiableRecipe, domainHandler, newRecipeName, newRecipeInstructions, ingredientItems, ingredientItemView,
                                                                listOfNewIngredients, newIngredientName, newIngredientQuantity, editSceneGridPane,
                                                                addNewRecipeButton, modifyExistingRecipeButton));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        addRecipeFromRecipeScene.setOnAction((event) -> {
            editRecipe.fire();
        });

        //lambdas for editScene button presses
        addNewRecipeButton.setOnAction((event) -> {
            try {
                List<String> newRecipeParts = new ArrayList<String>();
                newRecipeParts.add(newRecipeName.getText());
                newRecipeParts.add(newRecipeInstructions.getText());
                domainHandler.addRecipe(newRecipeParts, listOfNewIngredients);
                showRecipe.setText(newRecipeName.getText() + "\n\n");
                recipeModInfo.setTitle("Success!");
                recipeModInfo.setHeaderText("New recipe added!");
                recipeModInfo.setContentText("You have successfully added a new recipe!");
                recipeModInfo.showAndWait();
                modifyRecipe.fire();
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        modifyExistingRecipeButton.setOnAction((event) -> {
            try {
                List<String> newRecipeParts = new ArrayList<String>();
                newRecipeParts.add(newRecipeName.getText());
                newRecipeParts.add(newRecipeInstructions.getText());
                domainHandler.modifyRecipe(newRecipeParts, listOfNewIngredients);
                showRecipe.setText(newRecipeName.getText() + "\n\n");
                recipeModInfo.setTitle("Success!");
                recipeModInfo.setHeaderText("Recipe modification successful!");
                recipeModInfo.setContentText("You have successfully modified an existing recipe!");
                recipeModInfo.showAndWait();
                modifyRecipe.fire();
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        addNewIngredient.setOnAction((event) -> {
            try {
                ingredientItems.add(newIngredientName.getText() + ";" + newIngredientQuantity.getText() + ";" + listUnit.getSelectionModel().getSelectedItem().toString().toLowerCase());
                listOfNewIngredients.add(newIngredientName.getText() + ";" + newIngredientQuantity.getText() + ";" + listUnit.getSelectionModel().getSelectedItem().toString().toLowerCase());
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        removeSelectedIngredient.setOnAction((event) -> {
            try {
                int ingIndexNo = ingredientItemView.getSelectionModel().getSelectedIndex();
                ingredientItems.remove(ingIndexNo);
                listOfNewIngredients.remove(ingIndexNo - 1);
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });
        
        //scene objects
        Scene mainScene = new Scene(elementPlacement);
        
        //window commands
        window.setScene(mainScene);
        window.show();

        //database related dialog windows
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
        
        //ensure that a valid database exists before the program can be used
        //TODO: decide whether the lines below belong to domain or here
        //TODO: fix the logic with OR operator below!
        while (!new File(databaseName).isFile() || databaseName.isEmpty()) {
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
                    CreateTestData.createRandomTestData(sqliteHandler);
                }
            }
        }
    }

    @Override
    public void stop() throws Exception {
        //TODO: check that database is fine for closing
    }

    public static void main(String[] args) {
        launch(args);
    }
}
