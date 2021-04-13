package defaultPackage;

//GUI:
import defaultPackage.ui.UIJavaFX;
import java.lang.System;

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
        try {
            UIJavaFX.main(args);
        } catch (UnsupportedOperationException e) {
            System.out.println("JavaFX GUI in use: this program cannot be run over ssh "
                            + "connection, please use vdi.helsinki.fi to test");
            System.exit(1);
        }
    }
}
