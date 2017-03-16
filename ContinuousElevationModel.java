package ch.epfl.alpano.dem;
import java.util.Objects;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

/**
 * 
 * @author user
 *
 */
public final class ContinuousElevationModel {
    private DiscreteElevationModel dem;
    public static final double DISTANCE_NORTH_SOUTH = Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN);

    public ContinuousElevationModel(DiscreteElevationModel dem){
        this.dem=Objects.requireNonNull(dem);
    }
    
    public double elevationAt(GeoPoint p) {
        int indexLongitude00 = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.longitude()));
        int indexLatitude00 = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.latitude()));
        
        double v1 = DiscreteElevationModel.sampleIndex(p.longitude())-indexLongitude00;
        double v2 = DiscreteElevationModel.sampleIndex(p.latitude())-indexLatitude00;
        
        double alt00 = discreteIndexElevationAt(indexLongitude00, indexLatitude00);
        double alt01 = discreteIndexElevationAt(indexLongitude00 + 1, indexLatitude00);
        double alt10 = discreteIndexElevationAt(indexLongitude00, indexLatitude00 + 1);
        double alt11 = discreteIndexElevationAt(indexLongitude00 + 1, indexLatitude00 + 1);
        
    
        return Math2.bilerp(alt00, alt10, alt01, alt11, v1, v2);
    }

    
    double slopeAt(GeoPoint p){
        int indexLongitude00 = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.longitude()));
        int indexLatitude00 = (int) Math.floor(DiscreteElevationModel.sampleIndex(p.latitude()));
        
        double v1 = DiscreteElevationModel.sampleIndex(p.longitude())-indexLongitude00;
        double v2 = DiscreteElevationModel.sampleIndex(p.latitude())-indexLatitude00;
                
        
        double alt00 = discreteIndexSlopeAt(indexLongitude00, indexLatitude00);
        double alt01 = discreteIndexSlopeAt(indexLongitude00 + 1, indexLatitude00);
        double alt10 = discreteIndexSlopeAt(indexLongitude00, indexLatitude00 + 1);
        double alt11 = discreteIndexSlopeAt(indexLongitude00 + 1, indexLatitude00 + 1);
        
        return Math2.bilerp(alt00, alt10, alt01, alt11, v1, v2);
        
    }
   
    private double discreteIndexElevationAt(int indexLong, int indexLat) {
        if (!dem.extent().contains(indexLong, indexLat)) {
            return 0;
        }
        return dem.elevationSample(indexLong, indexLat);
    }
    
    /**
     * Computes the slope of the point represented by the index coordinates entered in the parameters.
     * @param indexLong
     * @param indexLat
     * @return
     */
    private double discreteIndexSlopeAt(int indexLong, int indexLat){
        double da=discreteIndexElevationAt(indexLong+1, indexLat)-discreteIndexElevationAt(indexLong, indexLat);
        double db=discreteIndexElevationAt(indexLong, indexLat+1)-discreteIndexElevationAt(indexLong, indexLat);
              
        return Math.acos(DISTANCE_NORTH_SOUTH/Math.sqrt(Math2.sq(da)+Math2.sq(db)+Math2.sq(DISTANCE_NORTH_SOUTH)));
    }

}
