package ch.epfl.alpano;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoPointTest {
    private static GeoPoint CORNAVIN = new GeoPoint(toRadians(6.14308), toRadians(46.21023));
    private static GeoPoint M1_EPFL = new GeoPoint(toRadians(6.56599), toRadians(46.52224));
    private static GeoPoint FEDERAL_PALACE = new GeoPoint(toRadians(7.44428), toRadians(46.94652));
    private static GeoPoint SAENTIS = new GeoPoint(toRadians(9.34324), toRadians(47.24942));
    private static GeoPoint MONTE_TAMARO = new GeoPoint(toRadians(8.86598), toRadians(46.10386));

    @Test
    public void distanceToWorksOnKnownPoints() {
        assertEquals(226_000, M1_EPFL.distanceTo(SAENTIS), 10);
        assertEquals( 81_890, M1_EPFL.distanceTo(FEDERAL_PALACE), 10);
        assertEquals(143_560, FEDERAL_PALACE.distanceTo(MONTE_TAMARO), 10);
        assertEquals(269_870, SAENTIS.distanceTo(CORNAVIN), 10);
    }

    @Test
    public void azimuthToWorksOnKnownPoints() {
        assertEquals( 68.03, toDegrees(M1_EPFL.azimuthTo(SAENTIS)), 0.01);
        assertEquals( 54.50, toDegrees(M1_EPFL.azimuthTo(FEDERAL_PALACE)), 0.01);
        assertEquals(130.23, toDegrees(FEDERAL_PALACE.azimuthTo(MONTE_TAMARO)), 0.01);
        assertEquals(245.82, toDegrees(SAENTIS.azimuthTo(CORNAVIN)), 0.01);
    }
}
