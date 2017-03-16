package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Objects;

/**
 * Represents a one dimensional interval of successive integers, 
 * starting from includedFrom, up to includedTo.
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */
public final class Interval1D {
    private int includedFrom;
    private int includedTo;
    
    /**
     * Basic constructor of Interval1D. Checks exception (cf. checkArgument()),
     * in order to ensure that the given lower isn't bigger than the upper bound.
     * @param includedFrom lower bound
     * @param includedTo upper bound
     */
    public Interval1D(int includedFrom, int includedTo) {
        checkArgument(includedTo >= includedFrom);     
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }
    
    /**
     * Getter of the lower bound of the given interval.
     * @return includedFrom, the lower bound.
     */
    public int includedFrom() {
        return includedFrom;
    }
    
    /**
     * Getter of the upper bound of the given interval.
     * @return includedTo, the upper bound.
     */
    public int includedTo() {
        return includedTo;
    }
    
    /**
     * Checks if a given integer belongs to the interval this.
     * @param v
     * @return the boolean value associated to the conditions above.
     */
    public boolean contains(int v) {
        return (includedFrom<=v && v<=includedTo);
    }
    
    /**
     * The number of elements of the interval.
     * @return size of the interval.
     */
    public int size(){
        return includedTo - includedFrom + 1;
    }
    
    /**
     * Returns the number of elements in common in between two intervals.
     * @param that
     * @return size of the intersection of the two intervals.
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        if (includedTo<that.includedFrom || includedFrom>that.includedTo) {
            return 0;
        }
        return Math.min(includedTo, that.includedTo) - Math.max(includedFrom, that.includedFrom) + 1 ;
    }
    
    /**
     * Returns the bounding union of two given intervals, one passed as input, other one is the one that called the method
     * @param that given one-dimensional interval
     * @return new interval created by computing the bounding union of the two given intervals
     */
    public Interval1D boundingUnion(Interval1D that) {
        return new Interval1D(Math.min(that.includedFrom, includedFrom), Math.max(that.includedTo, includedTo));
    }
    
    /**
     * Checks if two given intervals are unionable, one passed as argument, other one is the one that called the method
     * @param that given one-dimensional interval 
     * @return result of verification
     */
    public boolean isUnionableWith(Interval1D that) {
        if (sizeOfIntersectionWith(new Interval1D(that.includedFrom + 1,  that.includedTo + 1))>0) {
            return (sizeOfIntersectionWith(new Interval1D(that.includedFrom + 1,  that.includedTo + 1))>0);
        }
        
        else if (new Interval1D(includedFrom() + 1,  includedTo() + 1).sizeOfIntersectionWith(that)>0) {
            return (new Interval1D(includedFrom() + 1,  includedTo() + 1).sizeOfIntersectionWith(that)>0);
        } else {
            return false;
        }
    }
    
    /**
     * Returns the union formed of two given intervals, after having checked that they were unionable
     * @param that given one-dimensional interval
     * @return a new one-dimensional interval formed with the two input intervals
     */
    public Interval1D union(Interval1D that) {
        checkArgument(this.isUnionableWith(that));
        return boundingUnion(that);
    }
    
    /**
     * Override of the equals method from the Object class, stating that when called, the method compares all the arguments of
     * the one-dimensional intervals after having checked the instance of the object passed as argument
     * @return result of verification
     */
    @Override
    public boolean equals(Object thatO) {
        return (thatO instanceof Interval1D && ((Interval1D) thatO).includedFrom == this.includedFrom && ((Interval1D) thatO).includedTo == this.includedTo);
    }
    
    /**
     * Method associated to the equals method, it gives a hash value that is verified in the equal method,
     * this value is the same for all objects that return True when passed into the equals method
     */
    @Override
    public int hashCode() {
        return Objects.hash(includedFrom, includedTo);
    }
    
    /**
     * Override of the toString method from the Object class, returning a String value with all 
     * attributes of the Interval1D class
     * @return String of the type [lowerBound..upperBound]
     */    
    @Override
    public String toString() {
        return "[" + includedFrom + ".." + includedTo + "]";
    }
}
