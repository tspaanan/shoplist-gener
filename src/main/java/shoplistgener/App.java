package shoplistgener;

import shoplistgener.ui.UIJavaFX;
import java.lang.System;

public class App {
    public static void main(String[] args) {
        try {
            UIJavaFX.main(args);
        } catch (UnsupportedOperationException e) {
            System.out.println("JavaFX GUI in use: this program cannot be run over ssh "
                            + "connection, please use vdi.helsinki.fi to test");
            System.exit(1);
        } catch (Exception u) {
            System.out.println("Another exception occurred: " + u.getMessage());
        }
    }
}
