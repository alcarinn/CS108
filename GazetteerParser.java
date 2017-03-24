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

public final class GazetteerParser {
    
    public GazetteerParser() {} ;
    
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
    
    private static double switcher(String[] angle) throws IOException {
        checkArgument(angle.length == 3);
        checkIOException(0<=Integer.parseInt(angle[1]) && Integer.parseInt(angle[1])<60);
        checkIOException(0<=Integer.parseInt(angle[2]) && Integer.parseInt(angle[2])<60);
        return Integer.parseInt(angle[0]) + Integer.parseInt(angle[1])/60.0 + Integer.parseInt(angle[2])/3600.0;
    }
    
    
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
    
    private static void checkIOException(boolean b) throws IOException{
        if(!b){
            throw new IOException();
        }
    }
}
