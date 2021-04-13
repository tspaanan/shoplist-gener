package defaultPackage;

//GUI:
import defaultPackage.ui.UIJavaFX;

//debug
import java.io.File;
import defaultPackage.CreateTestData;

//TUI:
//import java.util.Scanner;
//import defaultPackage.dao.*;
//import defaultPackage.domain.shoplistgenerService;

public class App 
{
    public static void main( String[] args )
    {
        //debug
        if (!new File("Test.db").isFile()) {
            System.out.println("Creating test data...");
            CreateTestData.createSomeData();
            System.out.println("Test data created.");
        }

        //TUI:
        //Scanner scanner = new Scanner(System.in);
        //shoplistgenerDAO sqliteHandler = new shoplistgenerDAOsqlite();
        //shoplistgenerService domainHandler = new shoplistgenerService(sqliteHandler);
        //UI userInterface = new UI(scanner, domainHandler);
        //userInterface.start();

        //GUI:
        UIJavaFX.main(args);
    }
}
