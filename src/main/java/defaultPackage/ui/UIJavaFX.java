package defaultPackage.ui;

import defaultPackage.dao.*;
import defaultPackage.domain.shoplistgenerService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIJavaFX extends Application {
    
    private shoplistgenerDAO sqliteHandler;
    private shoplistgenerService domainHandler;

    @Override
    public void init() {
    sqliteHandler = new shoplistgenerDAOsqlite(); //should this DAO be injected to domainHandler at all?
    domainHandler = new shoplistgenerService(sqliteHandler);
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

        //FlowPane buttonPlacement = new FlowPane();
        HBox labelPlacement = new HBox();
        labelPlacement.setSpacing(10);
        labelPlacement.setPadding(new Insets(10,300,10,10));
        VBox buttonPlacement = new VBox();
        buttonPlacement.setSpacing(10);
        
        buttonPlacement.getChildren().add(wholeMenu);
        buttonPlacement.getChildren().add(allRecipes);
        buttonPlacement.getChildren().add(searchText);
        buttonPlacement.getChildren().add(searchRecipes);

        labelPlacement.getChildren().add(listMenu);
        labelPlacement.getChildren().add(listShoppingList);
        labelPlacement.getChildren().add(listRecipes);

        BorderPane elementPlacement = new BorderPane();
        elementPlacement.setRight(labelPlacement);
        elementPlacement.setLeft(buttonPlacement);
        elementPlacement.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        
        Scene viewPort = new Scene(elementPlacement);
        //viewPort.setFill(Color.BLACK); //does nothing?
        
        window.setScene(viewPort);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
