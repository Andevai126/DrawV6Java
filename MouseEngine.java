import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class MouseEngine extends MouseAdapter{
    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println("mouse pressed");
        Global.mouseLock = true;
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // System.out.println("mouse released");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Global.mouseX = (double)e.getX();
        Global.mouseY = (double)e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Global.mouseX = (double)e.getX();
        Global.mouseY = (double)e.getY();
    }    
    
    // MouseEvent
    // MouseWheelEvent
    // MouseListener
    // MouseMotionListener
    // MouseWheelListener
}
