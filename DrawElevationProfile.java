package ch.epfl.alpano.dem;

import java.awt.image.BufferedImage;
import java.io.File;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;

final class DrawElevationProfile {
  final static File HGT_FILE = new File("N46E006.hgt");
  final static double MAX_ELEVATION = 1_500;
  final static int LENGTH = 111_000;
  final static double AZIMUTH = Math.toRadians(27.97);
  final static double LONGITUDE = Math.toRadians(6.15432);
  final static double LATITUDE = Math.toRadians(46.20562);
  final static int WIDTH = 800, HEIGHT = 100;

  public static void main(String[] as) throws Exception {
    DiscreteElevationModel dDEM =
      new HgtDiscreteElevationModel(HGT_FILE);
    ContinuousElevationModel cDEM =
      new ContinuousElevationModel(dDEM);
    GeoPoint o =
      new GeoPoint(LONGITUDE, LATITUDE);
    ElevationProfile p =
      new ElevationProfile(cDEM, o, AZIMUTH, LENGTH);

    int BLACK = 0x00_00_00, WHITE = 0xFF_FF_FF;

    BufferedImage i =
      new BufferedImage(WIDTH, HEIGHT, TYPE_INT_RGB);
    for (int x = 0; x < WIDTH; ++x) {
      double pX = x * (double) LENGTH / (WIDTH - 1);
      double pY = p.elevationAt(pX);
      int yL = (int)((pY / MAX_ELEVATION) * (HEIGHT - 1));
      for (int y = 0; y < HEIGHT; ++y) {
        int color = y < yL ? BLACK : WHITE;
        i.setRGB(x, HEIGHT - 1 - y, color);
      }
    }
    dDEM.close();

    ImageIO.write(i, "png", new File("profile.png"));
  }
}