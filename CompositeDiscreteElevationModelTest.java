package ch.epfl.alpano.dem;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class CompositeDiscreteElevationModelTest {
    private final static Interval2D ext1 = new Interval2D(
            new Interval1D(-100_000, 100_000),
            new Interval1D(0, 100_000));
    private final static Interval2D ext2 = new Interval2D(
            new Interval1D(-100_000, 100_000),
            new Interval1D(100_001, 200_000));
    private final static Interval2D ext12 = new Interval2D(
            new Interval1D(-100_000, 100_000),
            new Interval1D(0, 200_000));
    private final static Interval2D ext3 = new Interval2D(
            new Interval1D(0, 99_999),
            new Interval1D(0, 100_001));

    @Test
    public void samplesPerRadiansHasCorrectValue() {
        assertEquals(206264.80624709636, DiscreteElevationModel.SAMPLES_PER_RADIAN, 1e-8);
    }

    @Test
    public void sampleIndexWorksOnRandomValues() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int arcSeconds = rng.nextInt(2_000_000) - 1_000_000;
            double angle = toRadians(arcSeconds / 3_600d);
            assertEquals(arcSeconds, DiscreteElevationModel.sampleIndex(angle), 1e-5);
        }
    }

    @SuppressWarnings("resource")
    @Test(expected = IllegalArgumentException.class)
    public void unionFailsIfExtentsNotUnionable() {
        ConstantElevationDEM dem1 = new ConstantElevationDEM(ext1, 0);
        ConstantElevationDEM dem2 = new ConstantElevationDEM(ext3, 0);
        dem1.union(dem2);
    }

    @SuppressWarnings("resource")
    @Test
    public void extentOfUnionIsUnionOfExtent() {
        ConstantElevationDEM dem1 = new ConstantElevationDEM(ext1, 0);
        ConstantElevationDEM dem2 = new ConstantElevationDEM(ext2, 0);
        DiscreteElevationModel dem12 = dem1.union(dem2);
        assertEquals(ext12, dem12.extent());
    }

    @SuppressWarnings("resource")
    @Test(expected = IllegalArgumentException.class)
    public void elevationSampleFailsWhenOutsideOfExtent() {
        ConstantElevationDEM dem1 = new ConstantElevationDEM(ext1, 0);
        ConstantElevationDEM dem2 = new ConstantElevationDEM(ext2, 0);
        DiscreteElevationModel dem12 = dem1.union(dem2);
        dem12.elevationSample(0, 200_001);
    }

    @SuppressWarnings("resource")
    @Test
    public void elevationSampleWorksOnBothSubDEMs() {
        ConstantElevationDEM dem1 = new ConstantElevationDEM(ext1, 1);
        ConstantElevationDEM dem2 = new ConstantElevationDEM(ext2, 2);
        DiscreteElevationModel dem12 = dem1.union(dem2);
        assertEquals(1, dem12.elevationSample(-100_000, 0), 0);
        assertEquals(1, dem12.elevationSample(100_000, 0), 0);
        assertEquals(1, dem12.elevationSample(100_000, 100_000), 0);
        assertEquals(1, dem12.elevationSample(-100_000, 100_000), 0);
        assertEquals(2, dem12.elevationSample(-100_000, 100_001), 0);
        assertEquals(2, dem12.elevationSample(100_000, 100_001), 0);
        assertEquals(2, dem12.elevationSample(100_000, 200_000), 0);
        assertEquals(2, dem12.elevationSample(-100_000, 200_000), 0);
    }

    @SuppressWarnings("resource")
    @Test
    public void closeClosesBothSubDEMs() throws Exception {
        ConstantElevationDEM dem1 = new ConstantElevationDEM(ext1, 0);
        ConstantElevationDEM dem2 = new ConstantElevationDEM(ext2, 0);
        DiscreteElevationModel dem12 = dem1.union(dem2);
        dem12.close();
        assertTrue(dem1.isClosed);
        assertTrue(dem2.isClosed);
    }
}

class ConstantElevationDEM implements DiscreteElevationModel {
    private final Interval2D extent;
    private final double elevation;
    boolean isClosed = false;

    public ConstantElevationDEM(Interval2D extent, double elevation) {
        this.extent = extent;
        this.elevation = elevation;
    }

    @Override
    public void close() throws Exception { isClosed = true; }

    @Override
    public Interval2D extent() { return extent; }

    @Override
    public double elevationSample(int x, int y) { return elevation; }
}