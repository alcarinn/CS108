package ch.epfl.alpano;

import static ch.epfl.test.ObjectTest.hashCodeIsCompatibleWithEquals;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class Interval1DTest {
    private static Interval1D i_0_9() { return new Interval1D(0, 9); }
    private static Interval1D i_0_2() { return new Interval1D(0, 2); }
    private static Interval1D i_3_5() { return new Interval1D(3, 5); }
    private static Interval1D i_4_6() { return new Interval1D(4, 6); }
    private static Interval1D i_6_9() { return new Interval1D(6, 9); }

    @Test(expected = IllegalArgumentException.class)
    public void constructorFailsForInvalidBounds() {
        new Interval1D(1, 0);
    }

    @Test
    public void constructorWorksForSingletonInterval() {
        new Interval1D(10, 10);
    }

    @Test
    public void containsIsTrueOnlyForTheIntervalsElements() {
        int sqrtIt = (int)ceil(sqrt(RANDOM_ITERATIONS));
        Random rng = newRandom();
        for (int i = 0; i < sqrtIt; ++i) {
            int a = rng.nextInt(200) - 100;
            int b = a + rng.nextInt(50);
            Interval1D interval = new Interval1D(a, b);
            for (int j = 0; j < sqrtIt; ++j) {
                int v = rng.nextInt(200) - 100;
                assertEquals(a <= v && v <= b, interval.contains(v));
            }
        }
    }

    @Test
    public void containsWorksAtTheLimit() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int max = rng.nextInt(2000);
            int a = rng.nextInt(max) - 1000;
            int b = max - 1000;
            Interval1D interval = new Interval1D(a, b);
            assertFalse(interval.contains(a - 1));
            assertTrue(interval.contains(a));
            assertTrue(interval.contains(b));
            assertFalse(interval.contains(b + 1));
        }
    }
    @Test
    public void sizeWorksOnKnownIntervals() {
        assertEquals(10, i_0_9().size());
        assertEquals(3, i_0_2().size());
        assertEquals(3, i_3_5().size());
        assertEquals(3, i_4_6().size());
        assertEquals(4, i_6_9().size());
    }

    @Test
    public void sizeOfIntersectionWorksOnNonIntersectingIntervals() {
        assertEquals(0, i_0_2().sizeOfIntersectionWith(i_3_5()));
        assertEquals(0, i_0_2().sizeOfIntersectionWith(i_4_6()));
        assertEquals(0, i_0_2().sizeOfIntersectionWith(i_6_9()));
    }

    @Test
    public void sizeOfIntersectionWorksOnIntersectingIntervals() {
        assertEquals(3, i_0_2().sizeOfIntersectionWith(i_0_9()));
        assertEquals(3, i_0_9().sizeOfIntersectionWith(i_0_2()));
        assertEquals(1, i_4_6().sizeOfIntersectionWith(i_6_9()));
    }

    @Test
    public void boundingUnionWorksOnKnownIntervals() {
        assertEquals(0, i_0_2().boundingUnion(i_6_9()).includedFrom());
        assertEquals(9, i_0_2().boundingUnion(i_6_9()).includedTo());
        assertEquals(0, i_6_9().boundingUnion(i_0_2()).includedFrom());
        assertEquals(9, i_6_9().boundingUnion(i_0_2()).includedTo());
        assertEquals(0, i_0_9().boundingUnion(i_0_9()).includedFrom());
        assertEquals(9, i_0_9().boundingUnion(i_0_9()).includedTo());
    }

    @Test
    public void isUnionableWithWorksOnKnownIntervals() {
        // Intersecting intervals
        assertTrue(i_0_9().isUnionableWith(i_0_9()));
        assertTrue(i_0_9().isUnionableWith(i_3_5()));
        assertTrue(i_3_5().isUnionableWith(i_3_5()));
        assertTrue(i_3_5().isUnionableWith(i_0_9()));
        assertTrue(i_3_5().isUnionableWith(i_4_6()));
        assertTrue(i_4_6().isUnionableWith(i_4_6()));
        assertTrue(i_4_6().isUnionableWith(i_3_5()));

        // Contiguous intervals
        assertTrue(i_3_5().isUnionableWith(i_6_9()));
        assertTrue(i_6_9().isUnionableWith(i_3_5()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unionFailsOnNonUnionableIntervals() {
        i_0_2().union(i_6_9());
    }

    @Test
    public void unionWorksWhenOneIntervalContainsTheOther() {
        assertEquals(i_0_9(), i_0_9().union(i_3_5()));
        assertEquals(i_0_9(), i_3_5().union(i_0_9()));
    }

    @Test
    public void unionWorksWithASingleInterval() {
        assertEquals(i_3_5(), i_3_5().union(i_3_5()).union(i_3_5()));
    }

    @Test
    public void unionWorksWhenOneIntervalIsContiguousWithTheOther() {
        Interval1D i = i_0_2().union(i_6_9().union(i_3_5()));
        assertEquals(i_0_9(), i);
    }

    @Test
    public void equalsIsStructural() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int a = rng.nextInt(), b = rng.nextInt();
            Interval1D int1 = new Interval1D(min(a, b), max(a, b));
            Interval1D int2 = new Interval1D(min(a, b), max(a, b));
            Interval1D int3 = new Interval1D(min(a, b) + 1, max(a, b) + 1);
            assertTrue(int1.equals(int2));
            assertFalse(int1.equals(int3));
        }
    }

    @Test
    public void hashCodeAndEqualsAreCompatible() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int a = rng.nextInt(), b = rng.nextInt();
            int c = rng.nextInt(), d = rng.nextInt();
            Interval1D int1 = new Interval1D(min(a, b), max(a, b));
            Interval1D int2 = new Interval1D(min(c, d), max(c, d));
            Interval1D int3 = new Interval1D(min(c, d), max(c, d));
            assertTrue(hashCodeIsCompatibleWithEquals(int1, int2));
            assertTrue(hashCodeIsCompatibleWithEquals(int2, int3));
        }
    }
}
