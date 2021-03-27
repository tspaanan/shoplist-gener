package defaultPackage;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        Scanner scanner = new Scanner(System.in);
        UI userInterface = new UI(scanner);
        userInterface.start();
    }
}
