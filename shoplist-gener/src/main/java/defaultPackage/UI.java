package defaultPackage;

import java.util.Scanner;

public class UI {
    
    private Scanner scanner;

    public UI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("Choose from the following: ");
            this.choices();
            String choice = this.scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("-----------");
            }
            if (choice.equals("5")) System.exit(0);
        }
    }

    private void choices() {
        System.out.println("Print a line: press 1");
        System.out.println("Exit: press 5");
    }
}
