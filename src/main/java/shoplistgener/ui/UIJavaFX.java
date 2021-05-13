package shoplistgener.ui;

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
    public void init() {
        Properties properties = new Properties();
        try {
            if (!new File("config.properties").isFile()) {
                FileWriter configWriter = new FileWriter(new File("config.properties"));
                configWriter.write("databaseName=");
                configWriter.close();
            }
            properties.load(new FileInputStream("config.properties"));
            databaseName = properties.getProperty("databaseName");
            sqliteHandler = new ShoplistgenerDAOsqlite(databaseName);
            domainHandler = new ShoplistgenerService(sqliteHandler);
            currentMenu = new HBox();
        } catch (Exception e) {
            System.out.println("init() failed: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public void start(Stage window) {
        window.setTitle("shoplist-gener");
        
        //sceneChange components
        Button viewMenu = new Button("View Menu");
        Button viewRecipes = new Button("View recipes");
        Button editRecipe = new Button("Add new recipe");
        Button viewKitchen = new Button("View ingredients in kitchen");
        VBox viewChoiceButtons = new VBox();
        viewChoiceButtons.setSpacing(10);
        viewChoiceButtons.setPadding(new Insets(10, 10, 10, 10));
        viewChoiceButtons.getChildren().addAll(viewMenu, viewRecipes, editRecipe, viewKitchen);

        //menuScene components
        Button weekMenu = new Button("Print Week's Menu and shopping list");
        Label listMenu = new Label();
        ObservableList<String> menuItems = FXCollections.observableArrayList();
        ListView<String> menuItemView = new ListView<String>(menuItems);
        menuItemView.setPrefSize(100, 50);
        Button quickViewCourse = new Button("View selected course");
        Alert quickViewRecipe = new Alert(AlertType.INFORMATION);
        Button randomizeCourse = new Button("Randomize selected course");
        Button changeCourse = new Button("Replace selected course");
        TextField changeCourseSearchField = new TextField("choose new course by name");
        Label listShoppingList = new Label();
        Label listShoppingListwithKitchenIngredients = new Label();
        listShoppingList.setPadding(new Insets(0, 10, 10, 10));
        listShoppingListwithKitchenIngredients.setPadding(new Insets(10, 10, 10, 10));
        HBox menuPlacement = new HBox();
        VBox menuPlacement2ndColumn = new VBox();
        menuPlacement.getChildren().addAll(weekMenu, menuItemView, menuPlacement2ndColumn);
        menuPlacement2ndColumn.getChildren().addAll(quickViewCourse, randomizeCourse, changeCourse, changeCourseSearchField);

        //recipeScene components
        ScrollPane listRecipes = new ScrollPane();
        listRecipes.setFitToWidth(true); //does nothing?
        ObservableList<String> allRecipesInList = FXCollections.observableArrayList();
        ListView<String> allRecipesListView = new ListView<String>(allRecipesInList);
        Label showRecipe = new Label();
        Button modifyRecipe = new Button("Modify this recipe");
        Button searchRecipeSelection = new Button("View selected recipe");
        TextField searchText = new TextField("search by name");
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

        //kitchenScene components
        ScrollPane listAllIngredients = new ScrollPane();
        listAllIngredients.setFitToWidth(true);
        ObservableList<String> allIngredientsInList = FXCollections.observableArrayList();
        ListView<String> allIngredientsListView = new ListView<String>(allIngredientsInList);
        ScrollPane listIngredientsInKitchen = new ScrollPane();
        ObservableList<String> ingredientsInKitchenList = FXCollections.observableArrayList();
        ListView<String> ingredientsInKitchenListView = new ListView<String>(ingredientsInKitchenList);
        Button addSelectedIngredientToKitchen = new Button("Add selected ingredient");
        Button removeSelectedIngredientInKitchen = new Button("Remove selected ingredient");
        Button updateIngredientQuantityInKitchen = new Button("Update ingredient quantity");
        TextInputDialog setIngredientKitchenQuantity = new TextInputDialog("Integer only");
        setIngredientKitchenQuantity.setTitle("Set New Quantity");
        setIngredientKitchenQuantity.setHeaderText("Set New Quantity for an Ingredient");
        setIngredientKitchenQuantity.setContentText("Give an integer: ");

        //mainScene parent components
        UIContructChangingView newViews = new UIContructChangingView();
        BorderPane changingView = new BorderPane();
        BorderPane elementPlacement = new BorderPane();
        elementPlacement.setCenter(changingView);
        elementPlacement.setLeft(viewChoiceButtons);
        elementPlacement.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        
        //lambdas for mainScene button presses
        viewMenu.setOnAction((event) -> {
            try {
                currentMenu = newViews.createMenuView("", "", "", listMenu, listShoppingList, listShoppingListwithKitchenIngredients, menuItems, menuPlacement);
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

        viewKitchen.setOnAction((event) -> {
            try {
                allIngredientsListView.getItems().clear();
                String allIngredientsFetched = domainHandler.fetchAllIngredients();
                if (allIngredientsFetched.equals("All Ingredients:\n\n")) {
                    listAllIngredients.setContent(new Text("no ingredients in database"));
                } else {
                    for (String singleIngredient : allIngredientsFetched.split("\n")) {
                        allIngredientsInList.add(singleIngredient);
                    }
                    listAllIngredients.setContent(allIngredientsListView);
                }
                ingredientsInKitchenListView.getItems().clear();
                String kitchenIngredientsFetched = domainHandler.fetchKitchenIngredients();
                if (kitchenIngredientsFetched.equals("Ingredients in Kitchen:\n\n")) {
                    listIngredientsInKitchen.setContent(new Text("no ingredients in kitchen"));
                } else {
                    for (String singleIngredient : kitchenIngredientsFetched.split("\n")) {
                        ingredientsInKitchenList.add(singleIngredient);
                    }
                    listIngredientsInKitchen.setContent(ingredientsInKitchenListView);
                }
                changingView.setCenter(newViews.createKitchenView(listAllIngredients, addSelectedIngredientToKitchen, listIngredientsInKitchen,
                                        removeSelectedIngredientInKitchen, updateIngredientQuantityInKitchen));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        //lambdas for menuScene button presses
        weekMenu.setOnAction((event) -> {
            try {
                String newCourses = domainHandler.fetchCourses();
                HBox currentMenu = initializeMenuView(newViews, newCourses, listMenu, listShoppingList, listShoppingListwithKitchenIngredients, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        randomizeCourse.setOnAction((event) -> {
            try {
                String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
                String newCourses = this.domainHandler.changeCourse(true, changedCourse, "");
                HBox currentMenu = initializeMenuView(newViews, newCourses, listMenu, listShoppingList, listShoppingListwithKitchenIngredients, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an item from the list!"));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        changeCourse.setOnAction((event) -> {
            try {
                String changedCourse = menuItemView.getSelectionModel().getSelectedItem().toString();
                String newCourseName = changeCourseSearchField.getText();
                String newCourses = this.domainHandler.changeCourse(false, changedCourse, newCourseName);
                HBox currentMenu = initializeMenuView(newViews, newCourses, listMenu, listShoppingList, listShoppingListwithKitchenIngredients, menuItems, menuPlacement);
                changingView.setCenter(currentMenu);
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an item from the list!"));
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
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an item from the list!"));
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
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an item from the list!"));
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
                if (!removedRecipe.isEmpty()) {
                    if (!removedRecipe.equals("could not find any matching recipes")) {
                        domainHandler.removeRecipe(removedRecipe);
                        allRecipesInList.remove(removedRecipe);
                        showRecipe.setText("\n\nrecipe was removed");
                    }
                }
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });
        
        modifyRecipe.setOnAction((event) -> {
            try {
                String modifiableRecipe = showRecipe.getText().split("\n\n")[0];
                if (!modifiableRecipe.isEmpty()) {
                    if (!modifiableRecipe.equals("could not find any matching recipes")) {
                        changingView.setCenter(newViews.createEditView(modifiableRecipe, domainHandler, newRecipeName, newRecipeInstructions, ingredientItems, ingredientItemView,
                                                                listOfNewIngredients, newIngredientName, newIngredientQuantity, editSceneGridPane,
                                                                addNewRecipeButton, modifyExistingRecipeButton));
                    }
                }
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
                makeRecipeChanges(newRecipeName, newRecipeInstructions, listOfNewIngredients, showRecipe, true);
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
                makeRecipeChanges(newRecipeName, newRecipeInstructions, listOfNewIngredients, showRecipe, false);
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
                if (!newIngredientName.getText().isEmpty()) {
                    if (!newIngredientQuantity.getText().isEmpty()) {
                        ingredientItems.add(newIngredientName.getText().replace(" ", "_") + " " + newIngredientQuantity.getText() + " "
                                            + listUnit.getSelectionModel().getSelectedItem().toString().toLowerCase());
                        listOfNewIngredients.add(newIngredientName.getText().replace(" ", "_") + " " + newIngredientQuantity.getText() + " "
                                                + listUnit.getSelectionModel().getSelectedItem().toString().toLowerCase());
                    }
                }
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select a UNIT from the list!"));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        removeSelectedIngredient.setOnAction((event) -> {
            try {
                int ingIndexNo = ingredientItemView.getSelectionModel().getSelectedIndex();
                if (ingIndexNo > 0) {
                    ingredientItems.remove(ingIndexNo);
                    listOfNewIngredients.remove(ingIndexNo - 1);
                }
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        //lambdas for kitchenScene button presses
        addSelectedIngredientToKitchen.setOnAction((event) -> {
            try {
                String selectedIngredientInKitchen = allIngredientsListView.getSelectionModel().getSelectedItem().toString().split("\\[")[0].trim();
                if (!selectedIngredientInKitchen.equals("All Ingredients:")) {
                    domainHandler.addIngredientToKitchen(selectedIngredientInKitchen);
                    viewKitchen.fire();
                }
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an ingredient from the list!"));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        removeSelectedIngredientInKitchen.setOnAction((event) -> {
            try {
                String selectedIngredientInKitchenToRemove = ingredientsInKitchenListView.getSelectionModel().getSelectedItem().toString().split(" ")[0];
                if (!selectedIngredientInKitchenToRemove.isEmpty()) {
                    if (!selectedIngredientInKitchenToRemove.equals("Ingredients")) {
                        domainHandler.removeIngredientFromKitchen(selectedIngredientInKitchenToRemove);
                        viewKitchen.fire();
                    }
                }
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an ingredient from the list!"));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        updateIngredientQuantityInKitchen.setOnAction((event) -> {
            Integer newQuantityInInteger = 0;
            try {
                String selectedIngredientInKitchenToUpdate = ingredientsInKitchenListView.getSelectionModel().getSelectedItem().toString().split(" ")[0];
                if (!selectedIngredientInKitchenToUpdate.isEmpty()) {
                    if (!selectedIngredientInKitchenToUpdate.equals("Ingredients")) {
                        while (true) {
                            try {
                                Optional<String> newQuantity = setIngredientKitchenQuantity.showAndWait();
                                newQuantityInInteger = Integer.parseInt(newQuantity.get());
                                break;
                            } catch (Exception e) {
                                setIngredientKitchenQuantity.setContentText("GIVE AN INTEGER! ");
                                continue;
                            }
                        }
                        domainHandler.updateIngredientQuantityInKitchen(selectedIngredientInKitchenToUpdate, newQuantityInInteger);
                        viewKitchen.fire();
                    }
                }
            } catch (NullPointerException n) {
                changingView.setCenter(newViews.createErrorView("Remember to select an ingredient from the list!"));
            } catch (Exception e) {
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });
        
        //scene objects
        Scene mainScene = new Scene(elementPlacement);
        
        //window commands
        window.setMinHeight(600);
        window.setMinWidth(800);
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
        while (!new File(databaseName).isFile() || databaseName.isEmpty() || ShoplistgenerDAOsqlite.databaseIsEmpty) {
            Optional<String> newDatabaseName = setDatabaseNameDialog.showAndWait();
            if (newDatabaseName.isPresent()) {
                try {
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
                    ShoplistgenerDAOsqlite.databaseIsEmpty = false;
                } catch (Exception e) {
                    System.out.println("Exception in test data creation: " + e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    private HBox initializeMenuView(UIContructChangingView newViews, String newCourses, Label listMenu, Label listShoppingList, Label listShoppingListwithKitchenIngredients,
                                    ObservableList<String> menuItems, HBox menuPlacement) throws Exception {
        String newShoppingList = domainHandler.fetchShoppingList();
        String newShoppingListModifiedByKitchenIngredients = domainHandler.subtractKitchenIngredients();
        currentMenu = newViews.createMenuView(newCourses, newShoppingList, newShoppingListModifiedByKitchenIngredients, listMenu, listShoppingList,
                                                listShoppingListwithKitchenIngredients, menuItems, menuPlacement);
        return currentMenu;
    }

    private void makeRecipeChanges(TextField newRecipeName, TextField newRecipeInstructions, List<String> listOfNewIngredients, Label showRecipe,
                                    boolean useAddRecipe) throws Exception {
        List<String> newRecipeParts = new ArrayList<String>();
        newRecipeParts.add(newRecipeName.getText());
        newRecipeParts.add(newRecipeInstructions.getText());
        if (useAddRecipe) {
            domainHandler.addRecipe(newRecipeParts, listOfNewIngredients);
        } else {
            domainHandler.modifyRecipe(newRecipeParts, listOfNewIngredients);
        }
        showRecipe.setText(newRecipeName.getText() + "\n\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
