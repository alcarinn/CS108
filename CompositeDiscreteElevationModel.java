package ch.epfl.alpano.dem;

import java.util.Objects;

import ch.epfl.alpano.Interval2D;

/**
 * Represents the union of two discrete DEM
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 */

final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
    /**
     * DEMs given as input values to create the composite DEM
     */
    protected DiscreteElevationModel dem1;
    protected DiscreteElevationModel dem2;

    /**
     * Returns a DEM composed of the two given DEM passed as input values
     * @param dem1 first given DEM
     * @param dem2 second given DEM
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
        this.dem1 = Objects.requireNonNull(dem1);
        this.dem2 = Objects.requireNonNull(dem2);
    }
    
    /**
     * Override of the close() method from AutoCloseable interface, closes the resources DEM
     */
    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }

    /**
     * Override of extent() method from DiscreteElevationModel interface
     */
    @Override
    public Interval2D extent() {
        return dem1.extent().union(dem2.extent());
    }

    /**
     * Override of elevationSample(int x, int y) method from DiscreteElevationModdel interface
     */
    @Override
    public double elevationSample(int x, int y) {
        if (dem1.extent().contains(x, y)) {
            return dem1.elevationSample(x, y);
        } else if (dem2.extent().contains(x,y)) {
            return dem2.elevationSample(x, y);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
