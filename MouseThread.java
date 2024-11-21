import java.awt.Robot;
import java.awt.AWTException;

public class MouseThread extends Thread {
    private Robot robot;
    private double deltaMouseX;
    private double deltaMouseY;

    public MouseThread(){
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            System.out.println("");
        }
    }

    @Override
    public void run(){
        while(true){

            if (Global.mouseLock == true) {
                deltaMouseX = Global.mouseX-Global.windowWidth/2;
                deltaMouseY = (Global.mouseY-Global.windowHeight/2)*-1;
                Global.hdir -= deltaMouseX/1000;
                Global.vdir += deltaMouseY/1000;

                robot.mouseMove((int)Global.screenLocation.getX()+Global.windowWidth/2, (int)Global.screenLocation.getY()+Global.windowHeight/2);
            }

            //1000 / 1000fps = 1ms
            try{
                Thread.sleep(20);
            }catch (InterruptedException e) {}
        }
    }
}
