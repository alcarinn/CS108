package ch.epfl.alpano;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

/**
 * A bidimensionnal interval, containing two one dimensional 
 * intervals which represents every possible combination of each
 * elements from the two given intervals.
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */
public final class Interval2D {
    private Interval1D iX;
    private Interval1D iY;

    /**
     * Basic constructor of a bidimensionnal interval.
     * Note: construction ensures the immutability by constructing
     * the class via defensive copies for iX and iY.
     * @param iX first one dimensional interval of successive integers
     * @param iY second one dimensional interval of successive integers
     */
    public Interval2D(Interval1D iX, Interval1D iY){
        if (iX == null || iY == null){
            throw new NullPointerException();
        }
        
        this.iX= new Interval1D(iX.includedFrom(),iX.includedTo());
        this.iY= new Interval1D(iY.includedFrom(),iY.includedTo());
    }
    
    /**
     * Basic getter for the one dimensional interval iX.
     * @return iX, first one dimensional interval.
     */
    public Interval1D iX(){
        return iX;
    }
    
    /**
     * Basic getter for the one dimensional interval iY.
     * @return iY, second one dimensional interval.
     */
    public Interval1D iY(){
        return iY;
    }
    
    /**
     * Checks if the iX and iY intervals of Interval2D contains
     * the parameters x and y respectively.
     * @param x integer value to check in iX
     * @param y integer value to check in iY
     * @return the boolean value associated to the verification
     */
    public boolean contains(int x, int y){
        return iX().contains(x) && iY().contains(y);
    }
    
    /**
     * The size of the Interval2D, which is the number of all
     * possible combinations of elements from the first and second 
     * interval.
     * @return size of interval
     */
    public int size(){
        return iX().size()*iY().size();
    }
    
    /**
     * Size of the intersection between two given intervals, number of
     * pairs which are in common in the two intervals.
     * @param that
     * @return size of intersection
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return iX().sizeOfIntersectionWith(that.iX()) * iY().sizeOfIntersectionWith(that.iY());
    }
    
    /**
     * Returns the bounding union of two intervals, which is the bounding union of 
     * the first one dimensional intervals of this and that and the bounding union of the second
     * one dimensional intervals of this and that.
     * @param that the Interval2D to do the bounding union with this
     * @return the bidimensional interval representing the bounding union
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(this.iX().boundingUnion(that.iX()) ,this.iY().boundingUnion(that.iY()));
                
    }
    /**
     * Checks if a bidimensional interval is unionable with another, which means
     * if they contain at least one common pair of integers.
     * @param that the Interval2D to verify with this
     * @return boolean value of the verification
     */
    public boolean isUnionableWith(Interval2D that){
       return this.size()+that.size()-this.sizeOfIntersectionWith(that)==this.boundingUnion(that).size();
    }
    
    /**
     * Returns the union of two dimensional intervals that and this, if they're not unionable
     * throws an IllegalArgumentException via the method checkArgument.
     * @param that
     * @return union of two dimensional intervals
     */
    public Interval2D union(Interval2D that){
        checkArgument(this.isUnionableWith(that));
        return boundingUnion(that);
    }
    
    @Override
    /**
     * Override of the method equals of object, comparing if two Interval2D's are equal
     * @return boolean value associated to the equivalence of two two dimensional intervals.
     */
    public boolean equals(Object thatO){
        return (thatO instanceof Interval2D && ((Interval2D) thatO).iX().equals(this.iX()) && ((Interval2D) thatO).iY().equals(this.iY()));
    }
    
    @Override
    /**
     * Method associated to the equals method, it gives a hash value that is verified in the equals method,
     * this value is the same for all objects that return True when passed into the equals method
     */
    public int hashCode(){
        return Objects.hash(iX(),iY());
    }
    
    @Override
    /**
     * Override of Object's toString method, returns the string representing 
     * the bidimensional interval.
     */
    public String toString(){
        return "[" + iX().includedFrom() + ".." + iX().includedTo() + "]x[" + iY().includedFrom() + ".." + iY().includedTo() + "]";
    }
}