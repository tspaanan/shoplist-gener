package defaultPackage;

import java.util.Scanner;
import defaultPackage.dao.*;
import defaultPackage.domain.shoplistgenerService;

public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        Scanner scanner = new Scanner(System.in);
        shoplistgenerDAO sqliteHandler = new shoplistgenerDAOsqlite();
        shoplistgenerService domainHandler = new shoplistgenerService(sqliteHandler);
        UI userInterface = new UI(scanner, domainHandler);
        userInterface.start();
    }
}
