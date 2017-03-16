package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Interface representing a discrete DEM
 *@author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 */

public interface DiscreteElevationModel extends AutoCloseable {
    /**
     * Number of samples per degree
     * Number of samples per radian
     */
    public final static int SAMPLES_PER_DEGREE = 3600;
    public final static double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE*Math.toDegrees(1);
    
    /**
     * Computes index associated associated to given angle,
     * can be a latitude or longitude
     * @param angle
     * @return index
     */
    public static double sampleIndex(double angle) {
        return angle*SAMPLES_PER_RADIAN;
    }
    
    /**
     * Gets the extent of the DEM, Discrete Elevation Model
     * @return extent (two dimensional interval)
     */
    public abstract Interval2D extent();
    
    /**
     * Gets the height sample at the given coordinates
     * @param x x-coordinate 
     * @param y y-coordinate
     * @return elevation sample (as double)
     * @throws IllegalArgumentException if given coordinates is out of DEM's bounds
     */
    public abstract double elevationSample(int x, int y);
    
    /**
     * Computes union of two given DEMs if possible
     * @param that
     * @return unioned DEM
     * @throws IllegalArgumentException if not unionable
     */
    public default DiscreteElevationModel union(DiscreteElevationModel that) {
        checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this, that);
    }

}
