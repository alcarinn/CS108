package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

final class WavyDEM implements DiscreteElevationModel {
    private final static double PERIOD = 100, HEIGHT = 1000;
    private final Interval2D extent;

    public WavyDEM(ch.epfl.alpano.Interval2D interval2d) {
      this.extent = interval2d;
    }

    @Override
    public void close() throws Exception { }

    @Override
    public Interval2D extent() { return extent; }

    @Override
    public double elevationSample(int x, int y) {
      double x1 = Math.PI * 2d * x / PERIOD;
      double y1 = Math.PI * 2d * y / PERIOD;
      return (1 + Math.sin(x1) * Math.cos(y1)) / 2d * HEIGHT;
    }
  }
