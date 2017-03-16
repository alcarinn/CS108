package ch.epfl.alpano;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Azimuth.fromMath;
import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Azimuth.toOctantString;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.PI;
import static java.lang.Math.floorMod;
import static java.lang.Math.nextDown;
import static java.lang.Math.round;
import static java.lang.Math.scalb;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class AzimuthTest {
    @Test
    public void isCanonicalIsTrueFor0() {
        assertTrue(isCanonical(0));
    }

    @Test
    public void isCanonicalIsFalseFor0Pred() {
        assertFalse(isCanonical(nextDown(0)));
    }

    @Test
    public void isCanonicalIsTrueFor2PiPred() {
        assertTrue(isCanonical(nextDown(scalb(PI, 1))));
    }

    @Test
    public void isCanonicalIsFalseFor2Pi() {
        assertFalse(isCanonical(scalb(PI, 1)));
    }

    @Test
    public void isCanonicalIsTrueForRandomCanonicalAzimuths() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i)
            assertTrue(isCanonical(rng.nextDouble() * scalb(PI, 1)));
    }

    @Test
    public void canonicalizeCorrectlyCanonicalizesRoundedRandomAngles() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int aDeg = rng.nextInt(10_000) - 5_000;
            double aRad = toRadians(aDeg);
            double canonicalARad = canonicalize(aRad);
            assertTrue(0 <= canonicalARad && canonicalARad < scalb(PI, 1));
            int canonicalADeg = (int)round(toDegrees(canonicalARad));
            if (canonicalADeg == 360)
                canonicalADeg = 0;
            assertEquals(floorMod(aDeg, 360), canonicalADeg);
        }
    }

    @Test
    public void toMathCorrectlyHandles0() {
        assertEquals(0d, toMath(0d), 0d);
    }

    @Test
    public void fromMathCorrectlyHandles0() {
        assertEquals(0d, fromMath(0d), 0d);
    }

    @Test
    public void toMathWorksForKnownValues() {
        int[] vs = new int[] {
                0, 0,
                90, 270,
                180, 180,
                270, 90
        };
        for (int i = 0; i < vs.length; i += 2) {
            double a = toMath(toRadians(vs[i]));
            assertEquals(toRadians(vs[i+1]), a, 1e-10);
        }
    }

    @Test
    public void fromMathWorksForKnownValues() {
        int[] vs = new int[] {
                0, 0,
                90, 270,
                180, 180,
                270, 90
        };
        for (int i = 0; i < vs.length; i += 2) {
            double a = fromMath(toRadians(vs[i]));
            assertEquals(toRadians(vs[i+1]), a, 1e-10);
        }
    }

    @Test
    public void toMathAndFromMathAreInverseForRandomValues() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            double a = rng.nextDouble() * scalb(PI, 1);
            double a2 = fromMath(toMath(a));
            assertEquals(a, a2, 1e-10);

            double a3 = toMath(fromMath(a));
            assertEquals(a, a3, 1e-10);
        }
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void toMathThrowsFor2Pi() {
        toMath(scalb(PI, 1));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void fromMathThrowsFor2Pi() {
        fromMath(scalb(PI, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toOctantStringThrowsForNonCanonicalAzimuth() {
        toOctantString(-1, null, null, null, null);
    }

    @Test
    public void toOctantStringCorrectlyCyclesThroughValues() {
        String n = "north", e = "east", s = "south", w = "west";
        ArrayList<String> expected = new ArrayList<>();
        expected.addAll(Collections.nCopies(45, n));
        expected.addAll(Collections.nCopies(45, n+e));
        expected.addAll(Collections.nCopies(45, e));
        expected.addAll(Collections.nCopies(45, s+e));
        expected.addAll(Collections.nCopies(45, s));
        expected.addAll(Collections.nCopies(45, s+w));
        expected.addAll(Collections.nCopies(45, w));
        expected.addAll(Collections.nCopies(45, n+w));

        for (int aDeg = 0; aDeg < 360; ++aDeg) {
            double aRad = toRadians(floorMod(aDeg - 22, 360));
            String os = toOctantString(aRad, n, e, s, w);
            assertEquals(expected.get(aDeg), os);
        }
    }
}
