package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Contains methods used to handle azimuths in radians
 * 
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public interface Azimuth {

    /**
     * Checks if given azimuth is canonical, meaning it's contained in [0;2PI[
     * 
     * @param azimuth azimuth in radians
     * @return the result of the verification as a boolean value 
     */
    public static boolean isCanonical(double azimuth) {
        return (0 <= azimuth && azimuth < Math2.PI2);
    }
    
    /**
     * Computes a given azimuth in his canonical form
     * 
     * @param azimuth azimuth in radians
     * @return the canonical form of given azimuth
     */
    
    public static double canonicalize(double azimuth){
        
        while (!isCanonical(azimuth)) {
            if (azimuth < 0){
                azimuth += Math2.PI2;
            }
            
            if (azimuth > Math2.PI2) {
                azimuth -= Math2.PI2;
            }
        }
        return azimuth;
    }
    
    /**
     * Computes an azimuth into a mathematical angle
     * 
     * @param azimuth azimuth in radians
     * @return the angle in radians
     */
    public static double toMath(double azimuth) {
        return fromMath(azimuth);
    }
    
    /**
     * Computes a mathematical angle into an azimuth
     * 
     * @param angle angle in radians
     * @return the azimuth in radians
     */
    public static double fromMath(double angle) {
        if (!isCanonical(angle)) {
            throw new IllegalArgumentException();
        }
        
        if (angle == 0) {
            return 0;
        }
        
        return Math2.PI2 - angle;
    }
    
    /**
     * Returns the cardinal direction associated to the given azimuth, 
     * by concatenating given String values associated to North, South, West & East
     * 
     * @param azimuth azimuth in radians
     * @param n String value for North
     * @param e String value for East
     * @param s String value for South
     * @param w String value for West
     * @return Concatenated String of cardinal direction
     */
    public static String toOctantString(double azimuth, String n, String e, String s, String w) {
        checkArgument(isCanonical(azimuth));
        
        if (azimuth <= Math.PI/8){
            return n;
        } else if (azimuth <= 3*Math.PI/8){
            return n+e;
        } else if (azimuth <= 5*Math.PI/8){
            return e;
        } else if (azimuth <= 7*Math.PI/8){
            return s+e;
        } else if (azimuth <= 9*Math.PI/8) {
            return s;
        } else if (azimuth <= 11*Math.PI/8) {
            return s + w;
        } else if (azimuth <= 13*Math.PI/8) {
            return w;
        } else if (azimuth <= 15*Math.PI/8) {
            return n + w;
        } else {
            return n;
        }
    }
}

