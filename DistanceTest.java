package ch.epfl.alpano;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.PI;
import static java.lang.Math.scalb;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class DistanceTest {
    private static double EARTH_CIRCUMFERENCE = 40_030_174; // rounded to nearest integer

    @Test
    public void toRadiansAndToMetersAreInverseForRandomValues() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double dRad = rng.nextDouble() * scalb(PI, 1);
            double dRad2 = Distance.toRadians(Distance.toMeters(dRad));
            assertEquals(dRad, dRad2, 1e-10);
        }
    }

    @Test
    public void toMetersIsCorrectForKnownValues() {
        assertEquals(0, Distance.toMeters(0), 0);
        assertEquals(EARTH_CIRCUMFERENCE, Distance.toMeters(scalb(PI, 1)), 0.5);
    }

    @Test
    public void toRadiansIsCorrectForKnownValues() {
        assertEquals(0, Distance.toRadians(0), 0);
        assertEquals(scalb(PI, 1), Distance.toRadians(EARTH_CIRCUMFERENCE), 1e-5);
    }
}