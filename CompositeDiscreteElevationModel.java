package ch.epfl.alpano.dem;

import java.util.Objects;
import ch.epfl.alpano.Interval2D;

final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
    protected final DiscreteElevationModel dem1;
    protected final DiscreteElevationModel dem2;
    
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
      
        this.dem1=Objects.requireNonNull(dem1);
        this.dem2=Objects.requireNonNull(dem2);
    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }

    @Override
    public Interval2D extent() {
        return dem1.extent().union(dem2.extent());
    }

    @Override
    public double elevationSample(int x, int y) {
        if(dem1.extent().contains(x, y)){
            return dem1.elevationSample(x, y);
        } else if (dem2.extent().contains(x, y)){
            return dem2.elevationSample(x, y);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
