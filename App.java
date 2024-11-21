public class App{
    public static void main(String[] args){

        MainThread mainThread = new MainThread(new DrawEngine(), new InputEngine(), new MouseEngine(), new WindowEngine());
        mainThread.start();
        
        MouseThread mouseThread = new MouseThread();
        mouseThread.start();
    }
}