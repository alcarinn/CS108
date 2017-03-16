package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Locale;

/**
 * Represents a point in Earth's surface in spherical coordinate system,
 *  by it's longitude and latitude in radians.
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 */
public final class GeoPoint {
    private double longitude;
    private double latitude;
    
    /**
     * Constructor of GeoPoint. Checks if the longitude and latitude values in radians
     * are in the conventional interval. [-PI,PI] for longitude, [-PI/2,PI/2] for latitude.
     * @param longitude in Radians
     * @param latitude in Radians
     */
    public GeoPoint(double longitude, double latitude) {
        checkArgument(-Math.PI <= longitude && longitude <= Math.PI && -Math.PI/2 <= latitude && latitude <= Math.PI/2);
        
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    /**
     * Getter method.
     * @return longitude of a given point.
     */
    public double longitude() {
        return longitude;
    }
    
    /**
     * Getter method.
     * @return latitude of a given point.
     */
    public double latitude() {
        return latitude;
    }
    
    /**
     * Computes and returns a distance between two points.
     * @param that second point
     * @return distance in meters between two points (this and that) on the Earth's surface.
     */
    public double distanceTo(GeoPoint that) {
        return Distance.toMeters(2*Math.asin(Math.sqrt(Math2.haversin(this.latitude() - that.latitude())+ 
                Math.cos(this.latitude())*Math.cos(that.latitude())*Math2.haversin(this.longitude()-that.longitude()))));
    }
    
    /**
     * Computes and returns an azimuth angle between two points
     * @param that second point.
     * @return the azimuth angle of that, vis-a-vis of this.
     */
    public double azimuthTo(GeoPoint that) {
        return Azimuth.fromMath(Azimuth.canonicalize(Math.atan2(Math.sin((this.longitude()-that.longitude())*Math.cos(that.latitude())),
                (Math.cos(this.latitude())*Math.sin(that.latitude()) - 
                        Math.sin(this.latitude())*Math.cos(that.latitude())*Math.cos(this.longitude() - that.longitude())))));
    }
    
    @Override
    /**
     * Redefines the toString method of Object.
     * Formats the print in order to print the latitude and the longitude to 4 decimals maximum.
     * @return the formatted string, representing the coordinates of a given GeoPoint.
     */
    public String toString() {
        Locale l = null;
        return String.format(l, "(%.4f; %.4f)", longitude(), latitude()) ;
    }
}