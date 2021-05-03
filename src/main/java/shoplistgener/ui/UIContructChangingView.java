package shoplistgener.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import shoplistgener.domain.ShoplistgenerService;

public class UIContructChangingView {
    private HBox components;

    public UIContructChangingView() {
        this.components = new HBox();
        this.components.setSpacing(10);
        this.components.setPadding(new Insets(10, 10, 10, 10));
    }

    public HBox createErrorView(String message) {
        Label errorMessage = new Label();
        errorMessage.setText(message);
        this.components.getChildren().clear();
        this.components.getChildren().add(errorMessage);
        return this.components;
    }

    public HBox createMenuView(String newCourses, String newShoppingList, Label listMenu, Label listShoppingList, ObservableList<String> menuItems, HBox menuPlacement) {
        listMenu.setText("Menu:\n" + newCourses);
        listShoppingList.setText("Shopping List:\n" + newShoppingList);
        menuItems.setAll(listMenu.getText().split("\\n"));
        this.components.getChildren().clear();
        this.components.getChildren().addAll(menuPlacement, listShoppingList);
        return this.components;
    }

    public HBox createRecipeView(ShoplistgenerService domainHandler, String recipeName) {
        //listRecipes.setContent(new Text(domainHandler.fetchRecipe(recipeName)));
        return this.components;
    }
}
