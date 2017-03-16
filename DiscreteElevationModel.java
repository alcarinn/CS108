package ch.epfl.alpano.dem;
import static ch.epfl.alpano.Preconditions.checkArgument;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.Interval2D;

public interface DiscreteElevationModel extends AutoCloseable {

    public final static int SAMPLES_PER_DEGREE=3600;
    public final double SAMPLES_PER_RADIAN = Math.toDegrees(1)*SAMPLES_PER_DEGREE;
    
    public static double sampleIndex(double angle){
        return angle*SAMPLES_PER_RADIAN;
    }
    /**
     * @return extent of a given bidimensionnal interval.
     */
    public abstract Interval2D extent();
    
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public abstract double elevationSample(int x, int y);

    public default DiscreteElevationModel union(DiscreteElevationModel that){
        checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
        
    }
    
    public static void main(String[] args) {    
        System.out.println(Distance.toMeters(1/SAMPLES_PER_RADIAN));
    }
}