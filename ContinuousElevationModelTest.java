package ch.epfl.alpano.dem;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class ContinuousElevationModelTest {
    private final static Interval2D EXT_100_100 = new Interval2D(
            new Interval1D(0, 100),
            new Interval1D(0, 100));

    private final static Interval2D EXT_13_13 = new Interval2D(
            new Interval1D(0, 13),
            new Interval1D(0, 13));

    @Test(expected = NullPointerException.class)
    public void constructorFailsWithNullDEM() {
        new ContinuousElevationModel(null);
    }

    @Test
    public void elevationAtReturns0OutsideOfExtent() {
        DiscreteElevationModel dDEM = new ConstantElevationDEM(EXT_100_100, 1000);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        assertEquals(0, cDEM.elevationAt(pointForSampleIndex(101, 0)), 0);
    }

    @Test
    public void elevationAtReturnsCorrectElevationInsideExtent() {
        double elevation = 1000;
        DiscreteElevationModel dDEM = new ConstantElevationDEM(EXT_100_100, elevation);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = rng.nextDouble() * 100d, y = rng.nextDouble() * 100d;
            assertEquals(elevation, cDEM.elevationAt(pointForSampleIndex(x, y)), 1e-10);
        }
    }

    @Test
    public void elevationAtInterpolatesJustOutsideExtent() {
        DiscreteElevationModel dDEM = new ConstantElevationDEM(EXT_100_100, 1000);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        assertEquals(500, cDEM.elevationAt(pointForSampleIndex(100.5, 10)), 1e-10);
    }

    @Test
    public void elevationAtReturnsCorrectInterpolatedElevation() {
        DiscreteElevationModel dDEM = new ConstantSlopeDEM(EXT_100_100);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        Random rng = new Random();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = rng.nextDouble() * 100;
            double y = rng.nextDouble() * 100;
            assertEquals((x + y) * ConstantSlopeDEM.INTER_SAMPLE_DISTANCE, cDEM.elevationAt(pointForSampleIndex(x, y)), 1e-6);
        }
    }

    @Test
    public void elevationAtStaysWithinBoundsOnRandomTerrain() {
        int maxElevation = 1000;
        DiscreteElevationModel dDEM = new RandomElevationDEM(EXT_13_13, maxElevation);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = rng.nextDouble() * dDEM.extent().iX().size();
            double y = rng.nextDouble() * dDEM.extent().iY().size();
            double e = cDEM.elevationAt(pointForSampleIndex(x, y));
            assertTrue(0 <= e && e <= maxElevation);
        }
    }

    @Test
    public void slopeAtReturnsCorrectInterpolatedSlope() {
        DiscreteElevationModel dDEM = new ConstantSlopeDEM(EXT_100_100);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        Random rng = new Random();
        double expectedSlope = Math.acos(1 / Math.sqrt(3));
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = 5 + rng.nextDouble() * 90;
            double y = 5 + rng.nextDouble() * 90;
            assertEquals(expectedSlope, cDEM.slopeAt(pointForSampleIndex(x, y)), 1e-4);
        }
    }

    @Test
    public void slopeAtStaysWithinBoundsOnRandomTerrain() {
        int maxElevation = 1000;
        DiscreteElevationModel dDEM = new RandomElevationDEM(EXT_13_13, maxElevation);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double x = rng.nextDouble() * dDEM.extent().iX().size();
            double y = rng.nextDouble() * dDEM.extent().iY().size();
            double e = toDegrees(cDEM.slopeAt(pointForSampleIndex(x, y)));
            assertTrue(0 <= e && e < 90);
        }
    }

    private static GeoPoint pointForSampleIndex(double x, double y) {
        return new GeoPoint(toRadians(x / 3600d), toRadians(y / 3600d));
    }
}

class RandomElevationDEM implements DiscreteElevationModel {
    private final Interval2D extent;
    private final double[][] elevations;

    public RandomElevationDEM(Interval2D extent, int maxElevation) {
        this.extent = extent;
        this.elevations = randomElevations(extent.iX().size(), extent.iY().size(), maxElevation);
    }

    private static double[][] randomElevations(int width, int height, int maxElevation) {
        Random rng = newRandom();
        double[][] es = new double[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                es[x][y] = rng.nextInt(maxElevation + 1);
            }
        }
        return es;
    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        return elevations[x][y];
    }

    @Override
    public void close() throws Exception { }
}

class ConstantSlopeDEM implements DiscreteElevationModel {
    public final static double INTER_SAMPLE_DISTANCE =
            2d * Math.PI * 6_371_000d / (3600d * 360d);

    private final Interval2D extent;

    public ConstantSlopeDEM(Interval2D extent) {
        this.extent = extent;
    }

    @Override
    public Interval2D extent() { return extent; }

    @Override
    public double elevationSample(int x, int y) {
        return (x + y) * INTER_SAMPLE_DISTANCE;
    }

    @Override
    public void close() throws Exception {}
}