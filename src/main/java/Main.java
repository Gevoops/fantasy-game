import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.marlin.Version;
import engine.Window;




public class Main {
    public static void main(String[] args) {

        System.out.println("hello LWJGL " + Version.getVersion() + "!");
        Window window = Window.get();
        window.run();



    }
}
