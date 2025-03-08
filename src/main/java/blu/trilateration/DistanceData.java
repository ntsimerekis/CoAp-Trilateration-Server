package blu.trilateration;

import java.util.HashMap;

public class DistanceData implements PositionData {
    private HashMap<String, Double> distances = new HashMap<String, Double>();
    private final String topReflector;
    private final String leftReflector;
    private final String rightReflector;

    public DistanceData(String top, String left, String right) {
        topReflector = top;
        distances.put(top, 6.0);
        leftReflector = left;
        distances.put(left, 6.0);
        rightReflector = right;
        distances.put(right, 6.0);
    }

    public void setDistance(String macAddress, double distance) {
        this.distances.put(macAddress, distance);
    }

    public double[] getDistances() {

        double[] distances = {0,0,0};

        distances[0] = this.distances.get(topReflector);
        distances[1] = this.distances.get(leftReflector);
        distances[2] = this.distances.get(rightReflector);

        return distances;
    }


}
