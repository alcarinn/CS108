package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
import static ch.epfl.alpano.Preconditions.checkArgument;;

/**
 * Contains mathematical and conversion tools
 * 
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public interface Math2 {
    
    public final static double PI2 = 2*Math.PI;
    
    /** 
     * Computes the square value of input
     * 
     * @param x a real number
     * @return x² the square value of the input
     */
    public static double sq(double x) {
        return x*x;
    }
    
    /** 
     * Computes the rest of the floored division of two input values
     * 
     * @param x dividend
     * @param y divisor
     * @return rest of the floored division
     */
    public static double floorMod(double x, double y) {
        return x-y*(Math.floor(x/y))  ;
    }
    
    /** 
     * Computes the haversine of a real input value
     * 
     * @param x real value
     * @return haversin(x)
     */
    public static double haversin(double x) {
        return sq(Math.sin(x/2)) ;
    }
    
    /** 
     * Computes the angular distance of two real input values
     * 
     * @param a1 first angle
     * @param a2 second angle
     * @return angular distance between first and second angle
     */
    public static double angularDistance(double a1, double a2) {
        return floorMod(a2 - a1 + Math.PI, PI2) - Math.PI;
    }
    
    /**
     * Computes f(x) through linear interpolation, with two given images of the same function f 
     * and real input value
     * 
     * @param y0 f(0)
     * @param y1 f(1)
     * @param x real value
     * @return f(x)
     */
    public static double lerp(double y0, double y1, double x) {
        return (y1-y0) * x + y0;
    }
    
    /**
     * Computes f(x,y) through bilinear interpolation, with 4 given images of the same function f
     * and real input values
     * 
     * @param z00 f(0,0)
     * @param z10 f(1,0)
     * @param z01 f(0,1)
     * @param z11 f(1,1)
     * @param x first real input value
     * @param y second real input value
     * @return f(x,y)
     */
    public static double bilerp(double z00, double z10, double z01, double z11, double x, double y) {
        return lerp(lerp(z00, z10, x), lerp(z01, z11, x), y);
    }
    
    /**
     * Searches and return the smallest boundary of the first interval of given size
     * containing a root of given function f and bounded by given minimum and given maximum ;
     * if no root is found, returns Double.POSITIVE_INFINITY (positive infinity value)
     * 
     * @param f given function
     * @param minX given minimum
     * @param maxX given maximum
     * @param dX given size for wanted interval
     * @return the smallest boundary of wanted interval
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX) {   
        
       for (double i = minX; i< maxX; i+=dX) {
           if (f.applyAsDouble(i) * f.applyAsDouble(i+dX) <= 0){
               return i;
           }
       }
       
       return Double.POSITIVE_INFINITY; 
    }
    
    /**
     * Returns the smallest boundary of an interval containing a root of a given function f, the wanted interval 
     * is determined using dichotomy while it isn't smaller than epsilon (given minimal interval).
     * 
     * The wanted interval is contained in the interval [x1;x2]
     * 
     * @param f given function
     * @param x1 lower bound
     * @param x2 upper bound
     * @param epsilon given minimal interval
     * @return the smallest bound of wanted interval
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {
        checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) < 0);

        if (x2 - x1 <= epsilon) {
            return x1;
        }

        double xm = (x1 + x2) / 2;

        if (f.applyAsDouble(xm) == 0) {
            return xm;
        }

        else if (f.applyAsDouble(xm) * f.applyAsDouble(x1) < 0) {
            return improveRoot(f, x1, xm, epsilon);
        }
        else {
            return improveRoot(f, xm, x2, epsilon);
        }
    }
}
