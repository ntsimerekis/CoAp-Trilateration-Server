package blu.trilateration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDistanceData  implements PositionData {

    private double top;
    private double left;
    private double right;

    private final Random rand;

    private static final int rangeMax = 10;
    private static final int rangeMin = 0;

    public RandomDistanceData() {
        rand = new Random();

        top = rand.nextDouble();
        left = rand.nextDouble();
        right = rand.nextDouble();
    }

    @Override
    public double[] getDistances() {
        top = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        left = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        right = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();

        return new double[]{top, left, right};
    }
}
