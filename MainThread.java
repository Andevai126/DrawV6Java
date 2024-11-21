// import javax.swing.SwingUtilities;
// import java.awt.Graphics;
// import java.awt.Color;
// import javax.swing.JPanel;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import javax.swing.JFrame;


import java.awt.Dimension;

import javax.swing.JComponent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.event.MouseAdapter;
// import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentAdapter;

public class MainThread extends Thread{
    private JFrame myJFrame;
    private JComponent jcomp;
    private Cursor defaultCursor;
    private Cursor noCursor;

    //fields about game camera
    double d;
    double e;
    double f;
    double o1;
    double p1;
    double q1;
    double r1;
    double s1;
    double j;
    double i;

    public MainThread (JComponent jcomp, KeyListener keyl, MouseAdapter mousea, ComponentAdapter compa){
        this.myJFrame = new JFrame("drawV6");
        this.myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.myJFrame.setPreferredSize(new Dimension(400, 439));
        Global.windowWidth = this.myJFrame.getWidth();
        Global.windowHeight = this.myJFrame.getHeight()-39;

        this.myJFrame.add(jcomp);
        this.myJFrame.addKeyListener(keyl);
        this.myJFrame.addMouseMotionListener(mousea);
        this.myJFrame.addMouseListener(mousea);
        // this.myJFrame.addMouseWheelListener(mousel);
        this.myJFrame.addComponentListener(compa);

        this.myJFrame.pack();
        this.myJFrame.setVisible(true);

        this.jcomp = jcomp;
        
        Global.screenLocation = this.myJFrame.getLocationOnScreen();

        this.defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.noCursor = toolkit.createCustomCursor(new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null);
    }

    private void checkWindow(){
        if (Global.isSizeChanged == true){
            Global.windowWidth = this.myJFrame.getWidth();
            Global.windowHeight = this.myJFrame.getHeight()-39;
            Global.isSizeChanged = false;
        }
        if (Global.isMoved == true) {
            Global.screenLocation = this.myJFrame.getLocationOnScreen();
            Global.isMoved = false;
        }
    }
    
    private void updateCursor(){
        if (Global.mouseLock == true) {
            this.myJFrame.setCursor(noCursor);
        } else {
            this.myJFrame.setCursor(defaultCursor);
        }
    }

    private void handleInput(){
        // if (Global.keystatus.get(KeyEvent.VK_J)){
        //     Global.xFOV++;
        //     Global.yFOV++;
        //     System.out.println("new fov = " + Global.xFOV);
        // }
        // if (Global.keystatus.get(KeyEvent.VK_L)){
        //     Global.xFOV--;
        //     Global.yFOV--;
        //     System.out.println("new fov = " + Global.xFOV);
        //     System.out.println("i = " + i);
        // }

        double hxdir = Math.cos(Global.hdir + (Math.PI / 2));
        double hydir = Math.sin(Global.hdir + (Math.PI / 2));
        if (Global.keystatus.get(KeyEvent.VK_LEFT) || Global.keystatus.get(KeyEvent.VK_A)){
            Global.xplayer += hxdir;
            Global.yplayer += hydir;
            // System.out.println("left");
        }
        if (Global.keystatus.get(KeyEvent.VK_RIGHT) || Global.keystatus.get(KeyEvent.VK_D)){
            Global.xplayer -= hxdir;
            Global.yplayer -= hydir;
            // System.out.println("right");
        }
        hxdir = Math.cos(Global.hdir);
        hydir = Math.sin(Global.hdir);
        if (Global.keystatus.get(KeyEvent.VK_UP) || Global.keystatus.get(KeyEvent.VK_W)){
            if (Global.keystatus.get(KeyEvent.VK_CONTROL)) {
                Global.xplayer += 6*hxdir;
                Global.yplayer += 6*hydir;
            } else {
                Global.xplayer += 3*hxdir;
                Global.yplayer += 3*hydir;
            }
            // System.out.println("forwards");
        }
        if (Global.keystatus.get(KeyEvent.VK_DOWN) || Global.keystatus.get(KeyEvent.VK_S)){
            Global.xplayer -= 3*hxdir;
            Global.yplayer -= 3*hydir;
            // System.out.println("backwards");
        }
        
        if (Global.keystatus.get(KeyEvent.VK_SPACE) || Global.keystatus.get(KeyEvent.VK_Q)){
            Global.zplayer += 1.5;
            // System.out.println("up");
        }
        if (Global.keystatus.get(KeyEvent.VK_SHIFT) || Global.keystatus.get(KeyEvent.VK_E)){
            Global.zplayer -= 1.5;
            // System.out.println("down");
        }
    }

    private void calculateCamera(){
        d = Math.cos(Global.hdir) * Math.cos(Global.vdir);
        e = Math.sin(Global.hdir) * Math.cos(Global.vdir);
        f = Math.sin(Global.vdir);
        o1 = Math.cos(Global.hdir) * Math.cos(Global.vdir + (Math.PI / 2));
        p1 = Math.sin(Global.hdir) * Math.cos(Global.vdir + (Math.PI / 2));
        q1 = Math.cos(Global.vdir);
        r1 = Math.sin(Global.hdir);
        s1 = Math.sin(Global.hdir - (Math.PI / 2));
        j = Global.xFOV;
        i = Global.yFOV;;
    }

    private Map<String, Double> calculatePoint(double a, double b, double c, double a1, double b1, double c1){
        double distance = Math.sqrt((((a - a1) * (a - a1)) + (((b - b1) * (b - b1)) + ((c - c1) * (c - c1)))));
        double infront = 0;
        double x = 0;
        double y = 0;
        if((distance < Global.renderdistance)){
            double g = (-1 * (((d * (a - a1)) + ((e * (b - b1)) + (f * (c - c1)))) / ((d * d) + ((e * e) + (f * f)))));
            double n2 = (a + (g * d));
            double o2 = (b + (g * e));
            double p2 = (c + (g * f));
            double ae = Math.sqrt((((g * d) * (g * d)) + (((g * e) * (g * e)) + ((g * f) * (g * f)))));
            double be = Math.sqrt((((d - (g * d)) * (d - (g * d))) + (((e - (g * e)) * (e - (g * e))) + ((f - (g * f)) * (f - (g * f))))));
            if((ae > be)){
                infront = 1;
            }
            double t1 = (-1 * (((r1 * (n2 - a1)) + (s1 * (o2 - b1))) / ((r1 * r1) + (s1 * s1))));
            double u1 = (-1 * (((o1 * (n2 - a1)) + ((p1 * (o2 - b1)) + (q1 * (p2 - c1)))) / ((o1 * o1) + ((p1 * p1) + (q1 * q1)))));
            double k = ae;
            double m = (Math.sqrt((((t1 * r1) * (t1 * r1)) + ((t1 * s1) * (t1 * s1)))) / (Math.tan((j/180)*Math.PI) * k));
            double n = (Math.sqrt((((u1 * o1) * (u1 * o1)) + (((u1 * p1) * (u1 * p1)) + ((u1 * q1) * (u1 * q1))))) / (Math.tan((i/180)*Math.PI) * k));
            double cp = Math.sqrt(((((a - r1) - a1) * ((a - r1) - a1)) + ((((b - s1) - b1) * ((b - s1) - b1)) + ((c - c1) * (c - c1)))));
            double mp = Math.sqrt(((((a + r1) - a1) * ((a + r1) - a1)) + ((((b + s1) - b1) * ((b + s1) - b1)) + ((c - c1) * (c - c1)))));
            double dp = Math.sqrt(((((a - o1) - a1) * ((a - o1) - a1)) + ((((b - p1) - b1) * ((b - p1) - b1)) + (((c - q1) - c1) * ((c - q1) - c1)))));
            double lp = Math.sqrt(((((a + o1) - a1) * ((a + o1) - a1)) + ((((b + p1) - b1) * ((b + p1) - b1)) + (((c + q1) - c1) * ((c + q1) - c1)))));
            if((cp < mp)){
                m = (-1 * m);
            }
            if((dp < lp)){
                n = (-1 * n);
            }
            // xscreen = width/2 + m*width/2;
            // yscreen = height/2 - n*height/2;
            x = (Global.windowWidth / 2) + (m * (Global.windowWidth / 2));
            y = (Global.windowHeight / 2) - (n * (Global.windowWidth / 2));
        }
        
        Map<String, Double> result = new HashMap<>();
        result.put("distance", distance);
        result.put("infront", infront);
        result.put("x", x);
        result.put("y", y);
        return result;
    }

    private void calculatePoints(){
        for (Asset asset : Global.assets) {
            List<Double> c = asset.getCoordinates3d();
            if (asset.getKind() == "point") {
                Map<String, Double> point = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(0), (double)c.get(1), (double)c.get(2));
                if (point.get("distance") < Global.renderdistance && point.get("infront") == 1) {
                    asset.setDistance(point.get("distance"));
                    asset.setCoordinates2d(new ArrayList<Double>(Arrays.asList(point.get("x"), point.get("y"))));
                    Global.addAssetToBeRendered(asset);
                }
            }else if (asset.getKind() == "line") {
                Map<String, Double> point = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(0), (double)c.get(1), (double)c.get(2));
                Map<String, Double> point2 = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(3), (double)c.get(4), (double)c.get(5));
                if ( (point.get("distance") < Global.renderdistance && point2.get("distance") < Global.renderdistance) && (point.get("infront") == 1 || point2.get("infront") == 1)) { //&& point2 < renderdistance, recalculate total distance
                    double xpart = Math.pow(Global.xplayer - (c.get(0)+c.get(3))/2, 2);
                    double ypart = Math.pow(Global.yplayer - (c.get(1)+c.get(4))/2, 2);
                    double zpart = Math.pow(Global.zplayer - (c.get(2)+c.get(5))/2, 2);
                    asset.setDistance(Math.sqrt(xpart + ypart + zpart));
                    asset.setCoordinates2d(new ArrayList<Double>(Arrays.asList(point.get("x"), point.get("y"), point2.get("x"), point2.get("y"))));
                    Global.addAssetToBeRendered(asset);
                }
            }else{ //asset is a triangle
                Map<String, Double> point = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(0), (double)c.get(1), (double)c.get(2));
                Map<String, Double> point2 = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(3), (double)c.get(4), (double)c.get(5));
                Map<String, Double> point3 = calculatePoint(Global.xplayer, Global.yplayer, Global.zplayer, (double)c.get(6), (double)c.get(7), (double)c.get(8));
                if ( (point.get("distance") < Global.renderdistance && point2.get("distance") < Global.renderdistance && point3.get("distance") < Global.renderdistance) && (point.get("infront") == 1 || point2.get("infront") == 1 || point3.get("infront") == 1)) { //&& point2 < renderdistance, recalculate total distance
                    double xpart = Math.pow(Global.xplayer - (c.get(0)+c.get(3)+c.get(6))/3, 2);
                    double ypart = Math.pow(Global.yplayer - (c.get(1)+c.get(4)+c.get(7))/3, 2);
                    double zpart = Math.pow(Global.zplayer - (c.get(2)+c.get(5)+c.get(8))/3, 2);
                    asset.setDistance(Math.sqrt(xpart + ypart + zpart));
                    asset.setCoordinates2d(new ArrayList<Double>(Arrays.asList(point.get("x"), point.get("y"), point2.get("x"), point2.get("y"), point3.get("x"), point3.get("y"))));
                    Global.addAssetToBeRendered(asset);
                }
            }
        }
    }

    @Override
    public void run(){
    
        while (true){
            checkWindow();
            updateCursor();
            handleInput();

            calculateCamera();
            calculatePoints();
            // Global.sortAssetsToBeRendered();
            Global.sortAssetsToBeRendered();

            //refresh screen
            jcomp.repaint();

            //wait
            try{
                Thread.sleep(Math.round(1000/Global.fps));
            }catch (InterruptedException e) {}
        }
    }
}