package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;

import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Represents a file reader returning the summit modeled with the file's informations
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 */

public final class GazetteerParser {
    
    /**
     * Private constructor making it impossible to instantiate the class
     */
    public GazetteerParser() {} ;
    
    /**
     * Loads an input file with informations about summit, the informations are written following a standard format, one line for each summit.
     * Gets the informations and puts them into a table. The method returns the table as an unmodifiable Collection.
     * @param file the input file containing the summit informations
     * @return (List<Summit>) unmodifiableCollection list with all the summit from the file
     */
    public static List<Summit> readSummitsFrom(File file) {
        List<Summit> list = new ArrayList<Summit>();
        
        try {
            try (FileInputStream fIS = new FileInputStream(file)) {
                InputStreamReader fSR = new InputStreamReader(fIS);
                BufferedReader fBR = new BufferedReader(fSR);
                String s;
                
                while ((s = fBR.readLine()) != null) {
                    list.add(decode(s));
                }
                fBR.close();
                fIS.close();
                fSR.close();

            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        return Collections.unmodifiableList(list);
        
    }
   
    /**
     * Computes the given angle that is expressed in degrees, minutes and seconds into an angle only in degrees.
     * @param angle
     * @return the angle's value in degrees only
     * @throws IOException if the table representing the angle has more than 3 components (degrees, minutes, seconds)
     */
    private static double switcher(String[] angle) throws IOException {
        checkArgument(angle.length == 3);
        checkIOException(0<=Integer.parseInt(angle[1]) && Integer.parseInt(angle[1])<60);
        checkIOException(0<=Integer.parseInt(angle[2]) && Integer.parseInt(angle[2])<60);
        return Integer.parseInt(angle[0]) + Integer.parseInt(angle[1])/60.0 + Integer.parseInt(angle[2])/3600.0;
    }
    
    /**
     * Reads and gets each single information about the summit described in the line passed as input value : name, longitude, latitude, elevation
     * @param line String obtained from the file containing all the summit's information, it contains the wanted information
     * @return New Summit instantiated with the informations retrieved from the input String
     * @throws IOException if at least one argument is not valid
     */
    private static Summit decode(String line) throws IOException {
        checkIOException(line.charAt(9) == ' ');
        checkIOException(line.charAt(18) == ' ');
        checkIOException(line.charAt(19) == ' ');
        checkIOException(line.charAt(24) == ' ');
        checkIOException(line.charAt(25) == ' ');
        checkIOException(line.charAt(28) == ' ');
        checkIOException(line.charAt(32) == ' ');
        checkIOException(line.charAt(35) == ' ');
        
        String[] longitude = (line.substring(0,9).replace(" ", "").split(":"));
        checkIOException(-180 <= Integer.parseInt(longitude[0]) && Integer.parseInt(longitude[0]) <= 180);
        double Long = switcher(longitude);
          
   
        String[] latitude = (line.substring(10,20).replace(" ", "")).split(":");
        checkIOException(-90 <= Integer.parseInt(latitude[0]) && Integer.parseInt(latitude[0]) <= 90);
        double Lat = switcher(latitude);
        
        int elevation = Integer.parseInt(line.substring(20,24).replace(" ", ""));
        
        String name = line.substring(36);
        
        return new Summit(name, new GeoPoint(Math.toRadians(Long), Math.toRadians(Lat)), elevation);
    }
    
    /**
     * Throws an IOException if the boolean check passed as input value is false
     * @param b result of a boolean check
     * @throws IOException if the check is not valid
     */
    private static void checkIOException(boolean b) throws IOException{
        if(!b){
            throw new IOException();
        }
    }
}
