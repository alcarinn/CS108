package ch.epfl.alpano;

/**
 * Throws specific exceptions for unfilled preconditions
 * 
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public interface Preconditions {
    
    /** 
     * Throws an IllegalArgumentException alone (without linked message)
     * if input is false, does nothing otherwise 
     * 
     * @param boolean b boolean input
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean b) {
        if (!b)
            throw new IllegalArgumentException() ;            
    }
    /**
     * Throws an IllegalArgumentException and displays linked message
     * if input is false, does nothing otherwise
     *
     * @param boolean b boolean input
     * @param String message linked message
     */
    
    public static void checkArgument(boolean b, String message) {
        if (!b) 
            throw new IllegalArgumentException(message);
    }
}
