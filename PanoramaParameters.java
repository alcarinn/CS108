package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import ch.epfl.alpano.Azimuth;
import java.util.Objects;

/**
 * Represents all the parameters required to compute a panorama.
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public final class PanoramaParameters {
    private GeoPoint observerPosition;
    private int observerElevation;
    private double centerAzimuth;
    private double horizontalFieldOfView;
    private int maxDistance;
    private int width;
    private int height;
    /**
     * Represents the angle measure per pixel, to compute the conversions
     */
    private final double delta;
    
    /**
     * Models a panorama by saving all required parameters, every parameter's validity is verified before being saved.
     * @param observerPosition (GeoPoint) location of the observer
     * @param observerElevation (int) elevation of the observer 
     * @param centerAzimuth (double) azimuth (in radians) of the center of the panorama
     * @param horizontalFieldOfView (double) angle in radians representing the horizontal field of view
     * @param maxDistance (int) maximum view distance of the observer
     * @param width (int) width of the panorama image, in pixels
     * @param height (int) height of the panorama image, in pixels
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, 
            double centerAzimuth, double horizontalFieldOfView, int maxDistance, 
            int width, int height) {
        
        checkArgument(Azimuth.isCanonical(centerAzimuth));
        checkArgument(0 < horizontalFieldOfView && horizontalFieldOfView <= Math2.PI2);
        checkArgument(maxDistance > 0);
        checkArgument(width > 0 && height > 0);
        
        if (observerPosition == null) {
            throw new NullPointerException();
        }
        
        this.observerPosition = new GeoPoint(observerPosition.longitude(), observerPosition.latitude());
        this.observerElevation = observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.width = width;
        this.height = height;
        delta = horizontalFieldOfView/(width-1);
    }
    
    
    public GeoPoint observerPosition() {
        return new GeoPoint(observerPosition.longitude(), observerPosition.latitude());
    }
    
    public int observerElevation() {
        return observerElevation;
    }
    
    public double centerAzimuth() {
        return centerAzimuth;
    }
    
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }
    
    public double verticalFieldOfView() {
        return horizontalFieldOfView()*(height - 1)/(width - 1);
    }
    
    public int maxDistance() {
        return maxDistance;
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    /**
     * Computes the azimuth corresponding to the given input pixel value, the validity of the pixel is verified.
     * @param x (double) pixel value
     * @return the corresponding canonical azimuth (in radians)
     */
    public double azimuthForX(double x) {
        checkArgument(0<= x && x < width);
        return Azimuth.canonicalize(centerAzimuth + (x - (width-1)/2.)*delta);
    }
    
    /**
     * Computes the pixel corresponding to the given azimuth, the validity of the azimuth compared 
     * to the panorama is verified
     * @param a (double) azimuth in radians
     * @return the corresponding pixel 
     */
    public double xForAzimuth(double a) {
        double result = (a-centerAzimuth)/delta + ((width-1)/2);
        checkArgument(0 <= result && result < width);
        return (a-centerAzimuth)/delta + ((width-1)/2);
    }
    
    /**
     * Computes the altitude of the observer's gaze corresponding to the given input pixel value. The altitude
     * is computed compared to an originally horizontal observer's gaze, so it can be negative or positive. The 
     * pixel 's validity is checked.
     * @param y (double) input pixel value from the panorama image
     * @return the altitude (in radians) of the observer's gaze for the corresponding pixel
     */
    public double altitudeForY(double y) {
        double result = (((height - 1)/2.) - y)*delta;
        checkArgument(-verticalFieldOfView()/2 <= result && result <= verticalFieldOfView()/2);
        return result;
    }
    
    /**
     * Computes the pixel corresponding to the given observer's gaze altitude. The altitude's 
     * validity is checked.
     * @param a observer's gaze altitude (in radians) 
     * @return the corresponding pixel on the panorama image
     */
    public double yForAltitude(double a) {
        checkArgument(-verticalFieldOfView()/2 <= a && a <= verticalFieldOfView()/2);
        return -a/delta + ((height - 1)/2.);
    }
    
    /**
     * Boolean check verifying if the index belongs to the panorama image.
     * @param x coordinate on the x axis
     * @param y coordinate on the y axis
     * @return True if it does belong to the panorama image, false otherwise.
     */
    protected boolean isValidSampleIndex(int x, int y) {
        return (0 <= x && x < width) && (0 <= y && y < height);
    }
    
    /**
     * Converts the two-coordinate index into a linear index. The panorama is read from left to right, 
     * from up to down.
     * @param x coordinate on the x axis
     * @param y coordinate on the y axis
     * @return
     */
    protected int linearSampleIndex(int x, int y) {
        checkArgument(isValidSampleIndex(x,y)); //?
        return x + y*(width);
    }
    
}
