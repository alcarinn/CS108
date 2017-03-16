package ch.epfl.alpano;

import static ch.epfl.test.ObjectTest.hashCodeIsCompatibleWithEquals;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class Interval2DTest {
    private static Interval2D newInterval2D(int x1, int x2, int y1, int y2) {
        return new Interval2D(new Interval1D(x1, x2), new Interval1D(y1, y2));
    }

    private static Interval2D i_0_10_0_10() {
        return newInterval2D(0, 10, 0, 10);
    }

    private static Interval2D i_0_9_0_11() {
        return newInterval2D(0, 9, 0, 11);
    }

    private static Interval2D i_0_10_11_20() {
        return newInterval2D(0, 10, 11, 20);
    }

    private static Interval2D i_0_10_0_20() {
        return newInterval2D(0, 10, 0, 20);
    }

    private static Interval2D i_11_20_0_10() {
        return newInterval2D(11, 20, 0, 10);
    }

    private static Interval2D i_0_20_0_10() {
        return newInterval2D(0, 20, 0, 10);
    }

    private static Interval2D i_2_2_2_2() {
        return newInterval2D(2, 2, 2, 2);
    }

    @Test(expected = NullPointerException.class)
    public void constructorFailsOnInvalidInterval() {
        new Interval2D(null, null);
    }

    @Test
    public void containsWorksOnKnownIntervals() {
        Interval2D i = i_2_2_2_2();
        for (int x = 1; x <= 3; ++x) {
            for (int y = 1; y <= 3; ++y) {
                assertEquals(x == 2 && y == 2, i.contains(x, y));
            }
        }
    }

    @Test
    public void sizeWorksOnKnownIntervals() {
        assertEquals(1, i_2_2_2_2().size());
        assertEquals(21 * 11, i_0_20_0_10().size());
        assertEquals(10 * 11, i_11_20_0_10().size());
    }

    @Test
    public void sizeOfIntersectionWorksOnNonIntersectingIntervals() {
        assertEquals(0, i_2_2_2_2().sizeOfIntersectionWith(i_11_20_0_10()));
        assertEquals(0, i_11_20_0_10().sizeOfIntersectionWith(i_2_2_2_2()));
    }

    @Test
    public void sizeOfIntersectionWorksOnIntersectingIntervals() {
        assertEquals(1, i_2_2_2_2().sizeOfIntersectionWith(i_2_2_2_2()));
        assertEquals(21 * 11, i_0_20_0_10().sizeOfIntersectionWith(i_0_20_0_10()));
        assertEquals(1, i_2_2_2_2().sizeOfIntersectionWith(i_0_20_0_10()));
        assertEquals(1, i_0_20_0_10().sizeOfIntersectionWith(i_2_2_2_2()));
        assertEquals(10 * 11, i_0_10_0_10().sizeOfIntersectionWith(i_0_9_0_11()));
    }

    @Test
    public void boudingUnionWorksOnKnownIntervals() {
        assertEquals(i_2_2_2_2(), i_2_2_2_2().boundingUnion(i_2_2_2_2()));

        Interval2D i1 = i_0_10_0_10().boundingUnion(i_0_9_0_11());
        assertEquals(0, i1.iX().includedFrom());
        assertEquals(10, i1.iX().includedTo());
        assertEquals(0, i1.iY().includedFrom());
        assertEquals(11, i1.iY().includedTo());

        Interval2D i2 = i_2_2_2_2().boundingUnion(i_11_20_0_10());
        assertEquals(2, i2.iX().includedFrom());
        assertEquals(20, i2.iX().includedTo());
        assertEquals(0, i2.iY().includedFrom());
        assertEquals(10, i2.iY().includedTo());
    }

    @Test
    public void isUnionableWorksOnKnownUnionableIntervals() {
        assertTrue(i_0_10_0_10().isUnionableWith(i_0_10_0_10()));
        assertTrue(i_0_10_0_10().isUnionableWith(i_0_10_11_20()));
        assertTrue(i_0_10_11_20().isUnionableWith(i_0_10_0_10()));
        assertTrue(i_0_10_0_10().isUnionableWith(i_11_20_0_10()));
        assertTrue(i_11_20_0_10().isUnionableWith(i_0_10_0_10()));
        assertTrue(i_0_10_0_10().isUnionableWith(i_2_2_2_2()));
        assertTrue(i_2_2_2_2().isUnionableWith(i_0_10_0_10()));
    }

    @Test
    public void isUnionableWorksOnKnownNonUnionableIntervals() {
        assertFalse(i_2_2_2_2().isUnionableWith(i_11_20_0_10()));
        assertFalse(i_11_20_0_10().isUnionableWith(i_2_2_2_2()));
        assertFalse(i_0_9_0_11().isUnionableWith(i_0_10_0_10()));
        assertFalse(i_0_10_0_10().isUnionableWith(i_0_9_0_11()));
    }

    @Test
    public void isUnionableWithIsReflexive() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            Interval2D interval = nextInterval(rng, 500, 1000);
            assertTrue(interval.isUnionableWith(interval));
        }
    }

    @Test
    public void isUnionableWithIsSymmetric() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            Interval2D i1 = nextInterval(rng, 5, 10);
            Interval2D i2 = nextInterval(rng, 5, 10);
            assertTrue(! i1.isUnionableWith(i2) || i2.isUnionableWith(i1));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void unionFailsOnNonUnionableIntervals() {
        i_2_2_2_2().union(i_11_20_0_10());
    }

    @Test
    public void unionWorksOnASingleInterval() {
        assertEquals(i_0_10_0_10(), i_0_10_0_10().union(i_0_10_0_10().union(i_0_10_0_10())));
    }

    @Test
    public void unionWorksOnKnownIntervals() {
        assertEquals(i_0_10_0_10(), i_0_10_0_10().union(i_2_2_2_2()));
        assertEquals(i_0_10_0_10(), i_2_2_2_2().union(i_0_10_0_10()));

        assertEquals(i_0_10_0_20(), i_0_10_0_10().union(i_0_10_11_20()));
        assertEquals(i_0_10_0_20(), i_0_10_11_20().union(i_0_10_0_10()));

        assertEquals(i_0_20_0_10(), i_0_10_0_10().union(i_11_20_0_10()));
        assertEquals(i_0_20_0_10(), i_11_20_0_10().union(i_0_10_0_10()));
    }

    @Test
    public void unionIsCommutative() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            Interval2D i1 = nextInterval(rng, 5, 10);
            Interval2D i2 = nextInterval(rng, 5, 10);
            if (i1.isUnionableWith(i2))
                assertEquals(i1.union(i2), i2.union(i1));
        }
    }

    @Test
    public void equalsIsStructural() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int x1 = rng.nextInt(1000) - 500;
            int x2 = x1 + rng.nextInt(1000);
            int y1 = rng.nextInt(1000) - 500;
            int y2 = y1 + rng.nextInt(1000);
            Interval2D int1 = newInterval2D(x1, x2, y1, y2);
            Interval2D int2 = newInterval2D(x1, x2, y1, y2);
            Interval2D int3 = newInterval2D(x1, x2, y1, y2 + 1);
            assertTrue(int1.equals(int2));
            assertTrue(int2.equals(int1));
            assertFalse(int1.equals(int3));
            assertFalse(int3.equals(int1));
        }
    }

    @Test
    public void hashCodeAndEqualsAreCompatible() {
        Random rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int a = rng.nextInt(1000) - 500, b = a + rng.nextInt(20);
            int c = rng.nextInt(1000) - 500, d = c + rng.nextInt(20);
            Interval2D i1 = newInterval2D(a, b, c, d);
            Interval2D i1b = newInterval2D(a, b, c, d);
            Interval2D i2 = newInterval2D(a, b, c, d + 1);
            assertTrue(hashCodeIsCompatibleWithEquals(i1, i1b));
            assertTrue(hashCodeIsCompatibleWithEquals(i1, i2));
        }
    }

    private Interval2D nextInterval(Random rng, int maxOffset, int maxSize) {
        int offsetX = rng.nextInt(maxOffset), offsetY = rng.nextInt(maxOffset);
        int sizeX = rng.nextInt(maxSize), sizeY = rng.nextInt(maxSize);
        return newInterval2D(-offsetX, sizeX - offsetX, -offsetY, sizeY - offsetY);
    }
}
