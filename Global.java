import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.awt.Point;

import java.util.Collections;



// import org.json.simple.*;
// import org.json.simple.parser.*;

import java.awt.geom.Area;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.Polygon;
// import java.awt.Triangle;
import java.awt.Color;
// import java.awt.Toolkit;
// import java.awt.Dimension;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;



public class Global {

    //fields and methods about all assets in the world
    static List<Asset> assets = new ArrayList<>();
    public static void addPoint(int r, int g, int b, double x, double y, double z){
        Asset asset = new Asset();
        asset.setKind("point");
        asset.setColor(new Color(r, g, b));
        asset.setCoordinates3d(Arrays.asList(x, y, z));
        assets.add(asset);
    }
    public static void addLine(int r, int g, int b, double x, double y, double z, double x2, double y2, double z2){
        Asset asset = new Asset();
        asset.setKind("line");
        asset.setColor(new Color(r, g, b));
        asset.setCoordinates3d(Arrays.asList(x, y, z, x2, y2, z2));
        assets.add(asset);
    }
    public static void addTriangle(int r, int g, int b, double x, double y, double z, double x2, double y2, double z2, double x3, double y3, double z3){
        Asset asset = new Asset();
        asset.setKind("triangle");
        asset.setColor(new Color(r, g, b));
        asset.setCoordinates3d(Arrays.asList(x, y, z, x2, y2, z2, x3, y3, z3));
        assets.add(asset);
    }
    public static Map<String, Integer> percentageToRgb(double percentage) {
        int number = (int)(percentage*1530);//6*255 = 1530
        int number2 = number % 255;
        int r = 0;
        int g = 0;
        int b = 0;
        if (number == 255*0) { r = 255; g = 0; b = 0; }
        if (number > 255*0 && number < 255*1){
            r = 255;
            g = number2;
            b = 0;
        }
        if (number == 255*1) { r = 255; g = 255; b = 0; }
        if (number > 255*1 && number < 255*2){
            r = 255-number2;
            g = 255;
            b = 0;
        }
        if (number == 255*2) { r = 0; g = 255; b = 0; }
        if (number > 255*2 && number < 255*3){
            r = 0;
            g = 255;
            b = number2;
        }
        if (number == 255*3) { r = 0; g = 255; b = 255; }
        if (number > 255*3 && number < 255*4){
            r = 0;
            g = 255-number2;
            b = 255;
        }
        if (number == 255*4) { r = 0; g = 0; b = 255; }
        if (number > 255*4 && number < 255*5){
            r = number2;
            g = 0;
            b = 255;
        }
        if (number == 255*5) { r = 255; g = 0; b = 255; }
        if (number > 255*5 && number < 255*6){
            r = 255;
            g = 0;
            b = 255-number2;
        }
        if (number == 255*6) { r = 255; g = 0; b = 0; }
        Map<String, Integer> result = new HashMap<>();
        result.put("r", r);
        result.put("g", g);
        result.put("b", b);
        return result;
    }
    
    //fields and methods to put assets in List<Asset>
    public static List<Double[]> getAssets(String pathAndFile){
        List<Double[]> doublelist = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(pathAndFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string;
            while( (string=bufferedReader.readLine()) != null){
                String[] arrayString = string.split(" ");
                Double[] arrayDouble = new Double[arrayString.length];
                for (int i = 0; i < arrayString.length; i++) {
                    arrayDouble[i] = Double.parseDouble(arrayString[i]);
                }
                doublelist.add(arrayDouble);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException i) {}
        return doublelist;
    }
    
    
    static{

        // addPoint(255, 0, 0, 10, 10, 10);
        // addTriangle(255, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0);

        // int rib = 10;
        // for (int i = 0; i < rib; i++) {
        //     for (int j = 0; j < rib; j++) {
        //         for (int k = 0; k < rib; k++) {
        //             Map<String, Integer> color = percentageToRgb( ((double)k)/rib );
        //             // addPoint(0, 255, 0, i, j, k);
        //             addPoint(color.get("r"), color.get("g"), color.get("b"), i, j, k);
        //             //addLine(color.get("r"), color.get("g"), color.get("b"), i, j, k, i+0.5, j+0.5, k+0.5);
        //             //addTriangle(color.get("r"), color.get("g"), color.get("b"), i, j, k, i+1, j, k, i+1, j+1, k);
        //         }
        //     }
        // }

        //highest point = 172.52115681613256
        //lowest point = -204.35919379982226
        List<Double[]> loadedAssets = getAssets("./assets.txt");
        for (Double[] doubles : loadedAssets) {
            double colorByHeight = (doubles[8] + 205) / 378;
            double colorBySlope = 1 - ( Math.abs(doubles[8] - doubles[5]) + Math.abs(doubles[8] - doubles[2]) + Math.abs(doubles[5] - doubles[2]) )/12;
            Map<String, Integer> color = percentageToRgb( colorByHeight*colorBySlope );
            addTriangle(color.get("r"), color.get("g"), color.get("b"), doubles[0], doubles[1], doubles[2], doubles[3], doubles[4], doubles[5], doubles[6], doubles[7], doubles[8]);
        }

    }



    //fields and methods about collecting, sorting and preparing assets to be drawn on screen
    
    public static boolean isInView(Shape shapeB) {
        Area areaA = new Area(new Rectangle2D.Double(0, 0, (double)windowWidth, (double)windowHeight));
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }

    static Map<Double, Asset> assetsToBeRendered = new HashMap<>();
    static void addAssetToBeRendered(Asset asset){
        List<Double> c = asset.getCoordinates2d();
        if (asset.getKind() == "point"){
            asset.setShape(new Rectangle2D.Double((double)c.get(0), (double)c.get(1), 5, 5));
        }
        if (asset.getKind() == "line"){
            asset.setShape(new Line2D.Double((double)c.get(0), (double)c.get(1), (double)c.get(2), (double)c.get(3)));
        }
        if (asset.getKind() == "triangle"){
            asset.setShape(new Polygon(new int[]{(int)(double)c.get(0), (int)(double)c.get(2), (int)(double)c.get(4)}, new int[]{(int)(double)c.get(1), (int)(double)c.get(3), (int)(double)c.get(5)}, 3));
        }
        double assetDistance = asset.getDistance();
        while ( assetsToBeRendered.containsKey(assetDistance) ){
            assetDistance += 0.00000001;
        }
        asset.setDistance(assetDistance);

        if (isInView(asset.getShape())) {
            assetsToBeRendered.put(asset.getDistance(), asset);
        }
    }

    static ArrayList<Asset> AssetsReadyToBeRendered = new ArrayList<>();
    public static void sortAssetsToBeRendered(){
        Map<Double, Asset> sortedAssetToBeRendered = new TreeMap<>(assetsToBeRendered);
        assetsToBeRendered.clear();
        AssetsReadyToBeRendered.clear();
        AssetsReadyToBeRendered.addAll(sortedAssetToBeRendered.values());
        Collections.reverse(AssetsReadyToBeRendered);
    }


    //fields about shapes drawn on screen
    // static{
    //     shapes.add(new Rectangle2D.Double(2, 30, 20, 20));
    //     shapes.add(new Line2D.Double(50, 30, 70, 90));
    //     shapes.add(new Polygon(new int[]{30, 50, 60}, new int[]{60, 70, 90}, 3));
    // }
    
    //field about keyboard
    static ArrayList<Boolean> keystatus = new ArrayList<>();
    static{
        for (int i = 0; i < 65536; i++) {
            keystatus.add(false);
        }
    }

    //fields about mouse
    static boolean mouseLock = false;
    static double mouseX;
    static double mouseY;

    //fields about window
    static int windowWidth;
    static int windowHeight;
    static Point screenLocation;
    
    static boolean isSizeChanged = false;
    static boolean isMoved = false;

    //fields for initial game settings
    static double xplayer = 0;
    static double yplayer = 0;
    static double zplayer = 0;
    static double hdir = 0;
    static double vdir = 0;
    static double renderdistance = 400;
    static double xFOV = 45;
    static double yFOV = xFOV;

    static int fps = 50;
}
