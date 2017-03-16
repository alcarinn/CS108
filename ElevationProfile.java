package ch.epfl.alpano.dem;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

import ch.epfl.alpano.*;

/**
 * Computes the elevation profile of a DEM following a given direction over a given length.
 * 
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public final class ElevationProfile {
    private ContinuousElevationModel elevationModel;
    private GeoPoint origin;
    private double azimuth;
    private double length;
    private GeoPoint[] geoPointsArray;
    /**
     * 4096 : Length in meters of the standart distance between 2 GeoPoints 
     * location put into the geoPointsArray table.
     */
    private final int STANDARD = 4096;
    
    /**
     * Computes the elevation profile of the given continuous DEM. The profile starts at the origin and goes in the direction
     * given by the azimuth, over the given distance length.
     * 
     * @param elevationModel input continuous DEM
     * @param origin starting location of the profile (GeoPoint)
     * @param azimuth angle giving the profile's direction (double)
     * @param length length in meters of the profile (double)
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){
        checkArgument(Azimuth.isCanonical(azimuth) && length > 0);

        this.elevationModel= Objects.requireNonNull(elevationModel);
        this.origin= Objects.requireNonNull(origin);
        this.azimuth=azimuth;
        this.length=length;
        
        geoPointsArray = new GeoPoint[(int) Math.ceil(length/STANDARD) +1];
        double a = Azimuth.toMath(azimuth);
        double sina = Math.sin(a);
        double cosa = Math.cos(a);
        double sinLatitude = Math.sin(origin.latitude());
        double cosLatitude = Math.cos(origin.latitude());
        
        for (int i = 0; i < geoPointsArray.length; i++) {
            double latitude = Math.asin(sinLatitude*Math.cos(Distance.toRadians(i*STANDARD)) + cosLatitude*Math.sin(Distance.toRadians(i*STANDARD))*cosa);
            double longitude = (origin.longitude()-Math.asin( sina*Math.sin(Distance.toRadians(i*STANDARD)) / Math.cos(latitude) )+Math.PI)%Math2.PI2 - Math.PI;
            geoPointsArray[i] = new GeoPoint(longitude, latitude);
        }
    }
    
    /**
     * Gets the elevation at a given position on the profile.
     * @param x input distance in meters
     * @return the elevation of the given position
     */
    public double elevationAt(double x){
        checkArgument(0 <= x && x <= length);
        return elevationModel.elevationAt(positionAt(x));
    }
    
    /**
     * Gets the location as a GeoPoint of a given position on the profile, 
     * the location is calculated by linear interpolation of the two nearest bounds (multiples of 4096, they are two of the values entered
     * in the standart value table to optimize the code).
     * @param x given distance (in meters) on the profile
     * @return a GeoPoint representing the location of the position on the profile
     */
    public GeoPoint positionAt(double x){
        checkArgument(0 <= x && x <= length);
     
        int lowerBound = (int) (x)/STANDARD;
        if (lowerBound >= geoPointsArray.length) {
            return new GeoPoint(geoPointsArray[lowerBound].longitude(),
                    geoPointsArray[lowerBound].latitude());
        }
        return new GeoPoint(Math2.lerp(geoPointsArray[lowerBound].longitude(), geoPointsArray[lowerBound+1].longitude(), x/STANDARD - lowerBound),
                Math2.lerp(geoPointsArray[lowerBound].latitude(), geoPointsArray[lowerBound+1].latitude(), x/STANDARD - lowerBound));
    }
    
    /**
     * Gets the slope at a given location, computed with a given distance representing a location on the profile.
     * @param x given distance (in meters) on the profile
     * @return a slope (in radians)
     */
    public double slopeAt(double x){
        checkArgument(0<= x && x<=length);
        return elevationModel.slopeAt(positionAt(x));
        
    }
}