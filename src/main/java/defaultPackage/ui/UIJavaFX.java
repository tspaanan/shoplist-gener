package defaultPackage.ui;

import defaultPackage.dao.*;
import defaultPackage.domain.shoplistgenerService;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
        
        Label listMenu = new Label("Menu:\n");
        Label listShoppingList = new Label("Shopping List:\n");
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

        //FlowPane buttonPlacement = new FlowPane();
        HBox buttonPlacement = new HBox();
        buttonPlacement.setSpacing(10);
        
        buttonPlacement.getChildren().add(wholeMenu);
        buttonPlacement.getChildren().add(listMenu);
        buttonPlacement.getChildren().add(listShoppingList);
        
        Scene viewPort = new Scene(buttonPlacement);
        
        window.setScene(viewPort);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
