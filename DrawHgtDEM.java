package ch.epfl.alpano.dem;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.*;
import ch.epfl.alpano.*;

public final class DrawHgtDEM {
    final static File HGT_FILE = new File("N46E006.hgt");
    final static double ORIGIN_LON = toRadians(6.25);
    final static double ORIGIN_LAT = toRadians(46.25);
    final static double WIDTH = toRadians(0.5);
    final static int IMAGE_SIZE = 300;
    final static double MIN_ELEVATION = 200;
    final static double MAX_ELEVATION = 1_500;

    public static void main(String[] as) throws Exception {
      DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
      ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
      
      double step = WIDTH / (IMAGE_SIZE - 1);
      BufferedImage i = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, TYPE_INT_RGB);
      for (int x = 0; x < IMAGE_SIZE; ++x) {
        double lon = ORIGIN_LON + x * step;
        for (int y = 0; y < IMAGE_SIZE; ++y) {
          
          double lat = ORIGIN_LAT + y * step;
          GeoPoint p = new GeoPoint(lon, lat);
          double el = (cDEM.elevationAt(p) - MIN_ELEVATION) / (MAX_ELEVATION - MIN_ELEVATION);
          i.setRGB(x, IMAGE_SIZE - 1 - y, gray(el));
        }
      }
      System.out.println(dDEM.extent());
      System.out.println("(" + ORIGIN_LON*3600*Math.toDegrees(1) + ".." + (ORIGIN_LON*3600*Math.toDegrees(1)+WIDTH*3600*Math.toDegrees(1)) + ") x (" + ORIGIN_LAT*3600*Math.toDegrees(1) + ".." + (ORIGIN_LAT*3600*Math.toDegrees(1)+WIDTH*3600*Math.toDegrees(1)) + ")");
      
      dDEM.close();

      ImageIO.write(i, "png", new File("dem.png"));
    }
    
    private static int gray(double v) {
        double clampedV = max(0, min(v, 1));
        int gray = (int) (255.9999 * clampedV);
        return (gray << 16) | (gray << 8) | gray;
      }
  }