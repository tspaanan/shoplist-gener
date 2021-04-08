package defaultPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import defaultPackage.domain.shoplistgenerService;

//for debug
import defaultPackage.domain.Ingredient;
import defaultPackage.domain.Unit;

public class UI {
    
    private Scanner scanner;
    private shoplistgenerService domainHandler;

    public UI(Scanner scanner, shoplistgenerService domainHandler) {
        this.scanner = scanner;
        this.domainHandler = domainHandler;
    }

    public void start() {
        while (true) {
            System.out.println("Choose from the following: ");
            this.choices();
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("-----------");
                Ingredient ing1 = new Ingredient("nimi1", Unit.CL);
                Ingredient ing2 = new Ingredient("nimi1", Unit.CL);
                System.out.println(ing1.equals(ing2));
            }
            if (choice.equals("1")) {
                try {
                    // System.out.println(this.daoHandler.fetchRecipe());
                    //tarkista tarvitaanko try-lohkoa enää
                    List<String> menu = this.domainHandler.fetchMenu();
                    //System.out.println(menu);
                    System.out.println("\nCourses for the next week are the following: ");
                    for (String l : menu) {
                        System.out.println(l);
                        if (l.equals("")) {System.out.println("\nShopping list generated: ");}
                    }
                    //for (List<String> l : menu) {
                        //for (int i = 2; i < l.size(); i++) {
                            //System.out.println(l.get(i));
                        //}
                    //}
                } catch (Exception e) {
                    // TODO Change this auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (choice.equals("5")) System.exit(0);
        }
    }

    private void choices() {
        System.out.println("Print a line (debug option): press 0");
        System.out.println("Print Week's Menu and shopping list: press 1");
        System.out.println("Exit: press 5");
    }
}
