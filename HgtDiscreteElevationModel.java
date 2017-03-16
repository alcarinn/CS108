package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Represents a discrete DEM, built with a HGT file.
 *   
 * @author Deniz Ira (269728) & Nicolas d'Argenlieu (276507)
 *
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    private File file;
    private ShortBuffer fileArray;
    
    /**
     * Builds a DEM with the elevation values stored in the HGT file passed as argument. 
     * These elevation values have to be stored in a specific order to be used afterwards,
     * and the name must respect a specific format.
     * 
     * @param file HGT file containing elevation
     * @throws IOException if the HGT file cannot be used
     */
    public HgtDiscreteElevationModel(File file) throws IOException {
        if (file.equals(null)) {
            throw new NullPointerException();
        }
        checkArgument(file.exists());
        
        checkArgument(file.getName().length() == 11);
        checkArgument(file.getName().charAt(0) == 'N' || file.getName().charAt(0) == 'S');
        checkArgument(0 <= Integer.parseInt(file.getName().substring(1,3)) && Integer.parseInt(file.getName().substring(1,3)) <= 90);
        checkArgument(file.getName().charAt(3) == 'W' || file.getName().charAt(3) == 'E');
        checkArgument(0 <= Integer.parseInt(file.getName().substring(4,7)) && Integer.parseInt(file.getName().substring(4,7)) <= 180);
        checkArgument(file.getName().substring(7).contentEquals(".hgt"));
        
        checkArgument(file.length() == 25934402);
        
        this.file = file;
        try (FileInputStream f = new FileInputStream(file)) {
            fileArray = f.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
            }
    }

    
    @Override
    public void close() throws Exception {
        file = null;
        fileArray = null;
    }

    /**
     * Computes the extent of the DEM as a two-dimensional interval
     * @returns two-dimensional interval representing the discrete DEM
     */
    @Override
    public Interval2D extent() {
        int indexLongitude = Integer.parseInt(file.getName().substring(4, 7));
        int indexLatitude= Integer.parseInt(file.getName().substring(1, 3));
        indexLatitude = file.getName().charAt(0) == 'N' ? indexLatitude : -indexLatitude;
        indexLongitude = file.getName().charAt(3) == 'E' ? indexLongitude : -indexLongitude;
        return new Interval2D(new Interval1D(indexLongitude*3600, (indexLongitude+1)*3600),new Interval1D(indexLatitude*3600, (indexLatitude+1)*3600));
    }

    /**
     * Override of the elevationSample(...) method of the DiscreteElevationModel interface.
     */
    @Override
    public double elevationSample(int x, int y) {
        return fileArray.get(x-extent().iX().includedFrom()
                + (SAMPLES_PER_DEGREE - (y - extent().iY().includedFrom()))*(SAMPLES_PER_DEGREE+1));
    }

    
}
