package shoplistgener.ui;

import shoplistgener.CreateTestData;
import shoplistgener.dao.*;
import shoplistgener.domain.*;
//import shoplistgener.ui.*;

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
        properties.load(new FileInputStream("config.properties"));
        databaseName = properties.getProperty("databaseName");
        sqliteHandler = new ShoplistgenerDAOsqlite(properties.getProperty("databaseName")); //should this DAO be injected to domainHandler at all?
        domainHandler = new ShoplistgenerService(sqliteHandler);
        currentMenu = new HBox();
    }
    
    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("shoplist-gener");
        
        //mainScene components
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
        ScrollPane listRecipes = new ScrollPane(); //stays visible for now, although ugly
        listRecipes.setFitToWidth(true); //does nothing?
        Button modifyRecipe = new Button("Modify this recipe");
        modifyRecipe.setVisible(false);
        Button wholeMenu = new Button("Print Week's Menu and shopping list");
        Button allRecipes = new Button("Print All Recipes");
        TextField searchText = new TextField("...use exact name (for now)");
        Button searchRecipes = new Button("Search for a recipe");
        Button removeRecipe = new Button("Remove this recipe");
        removeRecipe.setVisible(false);
        Button addRecipe = new Button("Add a new recipe");
        
        //mainScene placement components
        HBox menuPlacement = new HBox();
        VBox menuPlacement2ndColumn = new VBox();
        menuPlacement.getChildren().addAll(menuItemView, menuPlacement2ndColumn);
        menuPlacement2ndColumn.getChildren().addAll(quickViewCourse, randomizeCourse, changeCourse, changeCourseSearchField);
        //menuPlacement.setVisible(false);
        //HBox labelPlacement = new HBox();
        //labelPlacement.setSpacing(10);
        //labelPlacement.setPadding(new Insets(10,300,10,10));
        VBox viewChoiceButtons = new VBox();
        viewChoiceButtons.setSpacing(10);
        viewChoiceButtons.setPadding(new Insets(10,10,10,10));
        viewChoiceButtons.getChildren().add(wholeMenu);
        viewChoiceButtons.getChildren().add(allRecipes);
        //viewChoiceButtons.getChildren().add(searchText);
        viewChoiceButtons.getChildren().add(searchRecipes);
        viewChoiceButtons.getChildren().add(addRecipe);
        //labelPlacement.getChildren().add(menuPlacement);
        //labelPlacement.getChildren().add(listShoppingList);
        //labelPlacement.getChildren().add(listRecipes);
        VBox recipeModifications = new VBox();
        recipeModifications.getChildren().addAll(removeRecipe, modifyRecipe);
        //labelPlacement.getChildren().add(recipeModifications);
        
        //mainScene parent component
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
                //listMenu.setText("Menu:\n" + newCourses);
                //listShoppingList.setText("Shopping List:\n" + newShoppingList);
                //menuItems.setAll(listMenu.getText().split("\\n"));
                //menuPlacement.setVisible(true);
                changingView.setCenter(currentMenu);
            } catch (Exception e) {
                //listRecipes.setContent(new Text("no recipes in database"));
                //System.out.println(e.getMessage());
                //HBox errorView = newViews.createErrorView(e.getMessage());
                changingView.setCenter(newViews.createErrorView(e.getMessage()));
            }
        });

        allRecipes.setOnAction((event) -> {
            try {
                String allRecipesFetched = domainHandler.fetchRecipe("");
                if (allRecipesFetched.equals("All Recipes:\n\n")) {
                    listRecipes.setContent(new Text("no recipes in database"));
                } else {
                    listRecipes.setContent(new Text(allRecipesFetched));
                }
                removeRecipe.setVisible(false);
                modifyRecipe.setVisible(false);
            } catch (Exception e) {
                listRecipes.setContent(new Text("Exception occured (but shouldn't have)"));
            }
        });

        searchRecipes.setOnAction((event) -> {
            try {
                String searchString = searchText.getText();
                listRecipes.setContent(new Text(domainHandler.fetchRecipe(searchString)));
                removeRecipe.setVisible(true);
                modifyRecipe.setVisible(true);
            } catch (Exception e) {
                listRecipes.setContent(new Text("error: no recipe by that name"));
            }
        });

        removeRecipe.setOnAction((event) -> {
            try {
                domainHandler.removeRecipe(searchText.getText());
                listRecipes.setContent(new Text("Recipe was removed!"));
                removeRecipe.setVisible(false);
                modifyRecipe.setVisible(false);
            } catch (Exception e) {
                listRecipes.setContent(new Text("error: recipe could not be removed"));
                removeRecipe.setVisible(false);
                modifyRecipe.setVisible(false);
            }
        });

        randomizeCourse.setOnAction((event) -> {
            this.changeSingleCourse(true, menuItemView, listMenu, listShoppingList, menuItems, changeCourseSearchField);
        });

        changeCourse.setOnAction((event) -> {
            this.changeSingleCourse(false, menuItemView, listMenu, listShoppingList, menuItems, changeCourseSearchField);
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
        
        //addRecipeScene components
        TextField newRecipeName = new TextField("Recipe name");
        TextField newRecipeInstructions = new TextField("Instructions for the recipe");
        ObservableList<String> ingredientItems = FXCollections.observableArrayList();
        ListView<String> ingredientItemView = new ListView<String>(ingredientItems);
        List<Ingredient> listOfNewIngredients = new ArrayList<Ingredient>(); //TODO: decide whether to use Ingredient-object at all in UI or restrict its use for domain
        TextField newIngredientName = new TextField("Ingredient name");
        TextField newIngredientQuantity = new TextField("Ingredient quantity");
        ObservableList<Unit> newIngredientUnit = FXCollections.observableArrayList(Unit.values());
        ListView<Unit> listView = new ListView<Unit>(newIngredientUnit);
        listView.setPrefSize(80, 30);
        Button addNewIngredient = new Button("Add new ingredient");
        Button removeSelectedIngredient = new Button("Remove selected ingredient");
        Button addNewRecipeButton = new Button("Add new recipe");
        addNewRecipeButton.setVisible(false);
        Button modifyExistingRecipeButton = new Button("Modify recipe");
        modifyExistingRecipeButton.setVisible(false);
        
        //addRecipeScene placement components
        HBox newRecipeFields = new HBox();
        newRecipeFields.setSpacing(10);
        newRecipeFields.getChildren().addAll(newRecipeName, newRecipeInstructions);
        HBox newIngredientFields = new HBox();
        newIngredientFields.getChildren().addAll(newIngredientName, newIngredientQuantity, listView);
        GridPane addRecipeSceneGridpane = new GridPane();
        addRecipeSceneGridpane.add(newRecipeFields, 2, 1);
        addRecipeSceneGridpane.add(newIngredientFields, 2, 2);
        addRecipeSceneGridpane.add(addNewIngredient, 2, 3);
        addRecipeSceneGridpane.add(addNewRecipeButton, 3, 3);
        addRecipeSceneGridpane.add(modifyExistingRecipeButton, 4, 3);
        addRecipeSceneGridpane.add(ingredientItemView, 2, 5);
        addRecipeSceneGridpane.add(removeSelectedIngredient, 2, 6);
        addRecipeSceneGridpane.setAlignment(Pos.CENTER);
        
        //scene objects
        Scene mainScene = new Scene(elementPlacement);
        Scene addRecipeScene = new Scene(addRecipeSceneGridpane);
        
        //lambdas for scene changes
        addRecipe.setOnAction((event) -> {
            addNewRecipeButton.setVisible(true);
            window.setScene(addRecipeScene); 
            ingredientItems.clear();
            ingredientItems.add("Ingredients:"); //TODO: put this into component header instead
            listOfNewIngredients.clear();
        });
        
        modifyRecipe.setOnAction((event) -> {
            try {
                Recipe modifiableRecipe = domainHandler.fetchRecipeObject(searchText.getText());
                newRecipeName.setText(modifiableRecipe.getName());
                newRecipeInstructions.setText(modifiableRecipe.getInstructions());
                ingredientItems.clear();
                ingredientItems.add("Ingredients:"); //TODO: put this into component header instead
                listOfNewIngredients.clear();
                for (Ingredient ing : modifiableRecipe.getIngredients()) {
                    listOfNewIngredients.add(ing);
                    ingredientItems.add(ing.getName() + " " + Integer.valueOf(ing.getRequestedQuantity()) + " " + ing.getUnit().toString().toLowerCase());
                }
                modifyExistingRecipeButton.setVisible(true);
                newRecipeName.setEditable(false);
                window.setScene(addRecipeScene);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        //lambdas for addRecipeScene button presses
        addNewRecipeButton.setOnAction((event) -> {
            List<String> newRecipeParts = new ArrayList<String>();
            newRecipeParts.add(newRecipeName.getText());
            newRecipeParts.add(newRecipeInstructions.getText());
            for (Ingredient ing : listOfNewIngredients) {
                newRecipeParts.add(ing.getName() + ";" + ing.getUnit().toString().toLowerCase() + ";" + ing.getRequestedQuantity());
            }
            boolean bTarkistus = domainHandler.addRecipe(newRecipeParts);
            //TODO: clean up this mess!
            if (!bTarkistus) {
                listRecipes.setContent(new Text("error in adding recipe"));
            } else {
                try {
                listRecipes.setContent(new Text(domainHandler.fetchRecipe(newRecipeParts.get(0))));
                removeRecipe.setVisible(true);
                modifyRecipe.setVisible(true);
                searchText.setText(newRecipeParts.get(0));
                } catch (Exception e) {
                    listRecipes.setContent(new Text("error fetching recipe after successful creation"));
                }
            };
            window.setScene(mainScene);
            addNewRecipeButton.setVisible(false);
        });

        //TODO: refactor copy-pasted code below with addNewRecipeButton code above
        modifyExistingRecipeButton.setOnAction((event) -> {
            List<String> newRecipeParts = new ArrayList<String>();
            newRecipeParts.add(newRecipeName.getText());
            newRecipeParts.add(newRecipeInstructions.getText());
            for (Ingredient ing : listOfNewIngredients) {
                newRecipeParts.add(ing.getName() + ";" + ing.getUnit().toString().toLowerCase() + ";" + ing.getRequestedQuantity());
            }
            try {
            domainHandler.modifyRecipe(newRecipeParts);
            listRecipes.setContent(new Text(domainHandler.fetchRecipe(newRecipeParts.get(0))));
            removeRecipe.setVisible(true);
            modifyRecipe.setVisible(true);
            searchText.setText(newRecipeParts.get(0));
            } catch (Exception e) {
                listRecipes.setContent(new Text("error fetching recipe after successful modification"));
                System.out.println(e.getMessage());
            }
            window.setScene(mainScene);
            modifyExistingRecipeButton.setVisible(false);
            newRecipeName.setEditable(true);
        });

        addNewIngredient.setOnAction((event) -> {
            ingredientItems.add(newIngredientName.getText() + " " + newIngredientQuantity.getText() + " " + listView.getSelectionModel().getSelectedItem().toString().toLowerCase());
            listOfNewIngredients.add(new Ingredient(newIngredientName.getText(), Unit.valueOf(listView.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(newIngredientQuantity.getText())));
        });

        removeSelectedIngredient.setOnAction((event) -> {
            int ingIndexNo = ingredientItemView.getSelectionModel().getSelectedIndex();
            ingredientItems.remove(ingIndexNo);
            listOfNewIngredients.remove(ingIndexNo - 1);
        });
        
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

    //TODO: this method might be (at least partially) moved to domain
    private void changeSingleCourse(boolean randomized, ListView<String> menuItemView, Label listMenu,
                                    Label listShoppingList, ObservableList<String> menuItems, TextField changeCourseTextField) {
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
            listMenu.setText("\nException occured");
            listShoppingList.setText("");
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
