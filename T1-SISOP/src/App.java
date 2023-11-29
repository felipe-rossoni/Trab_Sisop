

public class App {

    public static void main(String[] args) {
        Sistema s = new Sistema(true);
        OSInterface os = new OSInterface(s);
        os.run();
    }
}