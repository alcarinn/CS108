package ch.epfl.alpano.dem;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

public final class ElevationProfile {
    ContinuousElevationModel elevationModel;
    GeoPoint origin;
    double azimuth;
    double length;
    private GeoPoint[] geoPointsArray;
    private final int DELTA = 4096;

    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, 
            double azimuth, double length){
        checkArgument(Azimuth.isCanonical(azimuth) && length > 0);
        
        this.elevationModel=Objects.requireNonNull(elevationModel);
        this.origin=Objects.requireNonNull(origin);
        this.azimuth=azimuth;
        this.length=length;
        
        geoPointsArray = new GeoPoint[(int)length/DELTA+5];
        double a = Azimuth.toMath(azimuth);
        double sina = Math.sin(a);
        double cosa = Math.cos(a);
        double sinLatitude = Math.sin(origin.latitude());
        double cosLatitude = Math.cos(origin.latitude());
        
        for (int i=0; i<geoPointsArray.length; i++){
            double latitude = Math.asin(sinLatitude*Math.cos(Distance.toRadians(i*DELTA))+cosa*cosLatitude*Math.sin(Distance.toRadians(i*DELTA)));
            double longitude = (origin.longitude() - Math.asin((sina*Math.sin(Distance.toRadians(i*DELTA)))/Math.cos(latitude))+Math.PI)%Math2.PI2 - Math.PI;
            geoPointsArray[i]= new GeoPoint(longitude,latitude);
        }
    }
    
    public double elevationAt(double x){
        checkArgument(0 <= x && x <= length);
        return elevationModel.elevationAt(positionAt(x));
        
    }
    public GeoPoint positionAt(double x){
        checkArgument(0 <= x && x <= length);
        int lowerBound = (int) x/DELTA;
        return new GeoPoint(Math2.lerp(geoPointsArray[lowerBound].longitude(), geoPointsArray[lowerBound+1].longitude(), x/DELTA -lowerBound),
                Math2.lerp(geoPointsArray[lowerBound].latitude(), geoPointsArray[lowerBound+1].latitude(), x/DELTA -lowerBound));

    }
    public double slopeAt(double x){
        checkArgument(0<= x && x<=length);
        return elevationModel.slopeAt(positionAt(x));
        
    }
}
