package ch.epfl.alpano;

/**
 * Allows to convert angles to distances on the Earth's surface 
 * and vice versa
 * 
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public interface Distance {
    
    /**
     * Earth's radius in meters
     */
    public final static double EARTH_RADIUS = 6371000;

    /**
     * Converts a distance into an angle
     * @param distanceInMeters distance in meters
     * @return angle angle in radians
     */
    public static double toRadians(double distanceInMeters) {
        return distanceInMeters/EARTH_RADIUS; 
    }
    
    /**
     * Converts an angle into a distance
     * 
     * @param distanceInRadians angle in radians
     * @return distance distance in meters
     */
    public static double toMeters(double distanceInRadians) {
        return distanceInRadians*EARTH_RADIUS;
    }
}
