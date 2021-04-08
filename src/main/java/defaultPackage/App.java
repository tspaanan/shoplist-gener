package defaultPackage;

import java.io.File;
import java.util.Scanner;
import defaultPackage.dao.*;
import defaultPackage.domain.shoplistgenerService;

//debug
import defaultPackage.CreateTestData;

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

        Scanner scanner = new Scanner(System.in);
        shoplistgenerDAO sqliteHandler = new shoplistgenerDAOsqlite();
        shoplistgenerService domainHandler = new shoplistgenerService(sqliteHandler);
        UI userInterface = new UI(scanner, domainHandler);
        userInterface.start();
    }
}
