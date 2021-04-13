package defaultPackage;

import java.util.List;
import java.util.Scanner;
import defaultPackage.domain.shoplistgenerService;

public class UI {
    
    private Scanner scanner;
    private shoplistgenerService domainHandler;

    public UI(Scanner scanner, shoplistgenerService domainHandler) {
        this.scanner = scanner;
        this.domainHandler = domainHandler;
    }

    public void start() {
        while (true) {
            System.out.println("\nChoose from the following: ");
            this.choices();
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("-----------");
            }

            if (choice.equals("1")) {
                try {
                    List<String> menu = this.domainHandler.fetchMenuTUI();
                    System.out.println("\nCourses for the next week are the following: ");
                    for (String l : menu) {
                        System.out.println(l);
                        if (l.equals("")) {System.out.println("\nShopping list generated: ");}
                    }
                } catch (Exception e) {
                    System.out.println("Mysterious 'ResultSet was closed' error just happened. Not to worry:\n"
                                        + "simply make your choice again: it has never occurred twice in a row...");
                }
            }

            if (choice.equals("2")) {
                System.out.println("Give the name of the recipe, or [ENTER] for a list of all the recipes: ");
                String recipeChoice = this.scanner.nextLine();
                try {
                    if (recipeChoice.equals("")) {
                        List<String> allRecipes = this.domainHandler.fetchRecipe("");
                        for (String recipeName : allRecipes) System.out.println(recipeName);
                    } else {
                        List<String> recipe = this.domainHandler.fetchRecipe(recipeChoice);
                        for (String recipePart : recipe) System.out.println(recipePart);
                    }
                } catch (Exception e) {System.out.println("Another mystery: put exception handling away from here!");}
            }

            if (choice.equals("5")) System.exit(0);
        }
    }

    private void choices() {
        System.out.println("Print a line (debug option): press 0");
        System.out.println("Print Week's Menu and shopping list: press 1");
        System.out.println("View recipes: press 2");
        System.out.println("Exit: press 5");
    }
}
