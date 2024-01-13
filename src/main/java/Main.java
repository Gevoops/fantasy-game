import com.sun.marlin.Version;
import engine.Window;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("hello LWJGL " + Version.getVersion() + "!");
        Window window = null;
        window = Window.get();
        window.run();



    }
}
