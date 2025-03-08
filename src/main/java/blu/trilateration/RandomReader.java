package blu.trilateration;

public class RandomReader {

    private final DistanceData distanceData;

    public RandomReader(DistanceData distanceData) {
        this.distanceData = distanceData;
    }
    public void start() {
        RandomPositionGenerator serialNordicReader = new RandomPositionGenerator();
        Thread thread = new Thread(serialNordicReader);
        thread.start();
    }

    class RandomPositionGenerator implements Runnable {

        @Override
        public void run() {

        }
    }
}

