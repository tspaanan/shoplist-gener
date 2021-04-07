package defaultPackage;

import java.util.ArrayList;
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
            System.out.println("Choose from the following: ");
            this.choices();
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("-----------");
            }
            if (choice.equals("1")) {
                try {
                    // System.out.println(this.daoHandler.fetchRecipe());
                    //tarkista tarvitaanko try-lohkoa enää
                    List<ArrayList<String>> menu = this.domainHandler.fetchMenu();
                    //System.out.println(menu);
                    System.out.println("Courses for the next week are the following: ");
                    for (List<String> l : menu) {
                        System.out.println(l.get(0));
                    }
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
        System.out.println("Print Week's Menu: press 1");
        System.out.println("Exit: press 5");
    }
}
