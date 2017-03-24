package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

/**
 * Represents a summit
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */

public final class Summit {
    private final String name;
    private final GeoPoint position;
    private int elevation;
    
    /**
     * Builds a summit by saving the given input values for the summit's name, position and elevation
     * @param name String representing the summit's name
     * @param position GeoPoint representing the location of the summit
     * @param elevation int representing elevation of the summit
     */
    public Summit(String name, GeoPoint position, int elevation) {
        this.name = Objects.requireNonNull(name);
        this.position = new GeoPoint(Objects.requireNonNull(position.longitude()), Objects.requireNonNull(position.latitude()));
        this.elevation = elevation;
    }
    
    /**
     * [Getter] Name
     * @return summit's name
     */
    public String name() {
        return name;
    }
    
    /**
     * [Getter] Position
     * @return summit's position
     */
    public GeoPoint position() {
        return position;
    }
    
    /**
     * [Getter] elevation
     * @return summit's elevation
     */
    public int elevation() {
        return elevation;
    }
    
    /**
     * Override of the toString() method from Object class, this modification returns a string 
     * of the following format : NAME (longitude, latitude) elevation
     */
    @Override
    public String toString() {
        return new String(name + " (" + Math.round(Math.toDegrees(position.longitude())*10000)/10000.0 + "," + Math.round(Math.toDegrees(position.latitude())*10000)/10000.0 + ") " + elevation );
    }

}
