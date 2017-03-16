package ch.epfl.alpano.dem;
import java.util.Objects;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

/**
 * Represents a continuous DEM created by linear interpolation of a DEM
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public final class ContinuousElevationModel {
    /**
     * Discrete DEM used to create the continuous DEM
     * Distance between two samples
     */
    private DiscreteElevationModel dem;
    public static final double DISTANCE_NORTH_SOUTH = Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN);

    /**
     * Builds a continuous DEM based on the discrete DEM passed as input value
     * @param dem
     * @throws IllegalArgumentException if input DEM is null
     */
    public ContinuousElevationModel(DiscreteElevationModel dem){
        this.dem=Objects.requireNonNull(dem);
    }
    
    /**
     * Returns the elevation at the given location, elevation obtained by bilinear interpolation of the extended DEM
     * @param p GeoPoint object representing location
     * @return the elevation (in meters) at the location p
     */
    public double elevationAt(GeoPoint p) {
        int indexLongitude00 = (int) DiscreteElevationModel.sampleIndex(p.longitude());
        int indexLatitude00 = (int) DiscreteElevationModel.sampleIndex(p.latitude());
        
        double indexLong = DiscreteElevationModel.sampleIndex(p.longitude()) - indexLongitude00;
        double indexLat = DiscreteElevationModel.sampleIndex(p.latitude()) - indexLatitude00;
        
        
        double alt00 = discreteIndexElevationAt(indexLongitude00, indexLatitude00);
        double alt01 = discreteIndexElevationAt(indexLongitude00 + 1, indexLatitude00);
        double alt10 = discreteIndexElevationAt(indexLongitude00, indexLatitude00 + 1);
        double alt11 = discreteIndexElevationAt(indexLongitude00 + 1, indexLatitude00 + 1);

        return Math2.bilerp(alt00, alt01, alt10, alt11, indexLong, indexLat);
    }

    /**
     * Returns the slope at a given location, using bilinear interpolation of the slopes of the four discrete samples around 
     * @param p GeoPoint object representing location
     * @return the slope (in radians) of at the location p
     */
    public double slopeAt(GeoPoint p){
        int indexLongitude00 = (int) DiscreteElevationModel.sampleIndex(p.longitude());
        int indexLatitude00 = (int) DiscreteElevationModel.sampleIndex(p.latitude());
        
        double indexLong = DiscreteElevationModel.sampleIndex(p.longitude()) - indexLongitude00;
        double indexLat = DiscreteElevationModel.sampleIndex(p.latitude()) - indexLatitude00;
        
        double alt00 = discreteIndexSlopeAt(indexLongitude00, indexLatitude00);
        double alt01 = discreteIndexSlopeAt(indexLongitude00 + 1, indexLatitude00);
        double alt10 = discreteIndexSlopeAt(indexLongitude00, indexLatitude00 + 1);
        double alt11 = discreteIndexSlopeAt(indexLongitude00 + 1, indexLatitude00 + 1);
                
        return Math2.bilerp(alt00, alt01, alt10, alt11, indexLong, indexLat);
        
    }
    
    /**
     * Returns the elevation of the sample at (x, y), the sample belonging to the discrete DEM
     * @param indexLong x-value of the sample index
     * @param indexLat y-value of the index sample
     * @return the elevation (in radians) at the sample location
     */
    private double discreteIndexElevationAt(int indexLong, int indexLat) {
        if (!dem.extent().contains(indexLong, indexLat)) {
            return 0;
        }
        return dem.elevationSample(indexLong, indexLat);
    }
    
    /**
     * Computes the slope of the sample at (x, y), the sample belongs to the discrete DEM
     * @param indexLong x-value of the sample index
     * @param indexLat y-value of the sample index
     * @return the slope (in radians) at the sample location
     */
    private double discreteIndexSlopeAt(int indexLong, int indexLat){
        double da=discreteIndexElevationAt(indexLong+1, indexLat)-discreteIndexElevationAt(indexLong, indexLat);
        double db=discreteIndexElevationAt(indexLong, indexLat+1)-discreteIndexElevationAt(indexLong, indexLat);
              
        return Math.acos(DISTANCE_NORTH_SOUTH/Math.sqrt(Math2.sq(da)+Math2.sq(db)+Math2.sq(DISTANCE_NORTH_SOUTH)));
    }

}
