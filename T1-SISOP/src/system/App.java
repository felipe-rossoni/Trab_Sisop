package system;

public class App {

    public static void main(String[] args) {
        Sistema s = new Sistema(false);
        OSInterface os = new OSInterface(s);
        os.run();
    }
}