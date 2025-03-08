package blu.trilateration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDistanceData  implements PositionData {

    private double top;
    private double left;
    private double right;

    private final Random rand;

    public RandomDistanceData() {
        rand = new Random();

        top = rand.nextDouble();
        left = rand.nextDouble();
        right = rand.nextDouble();
    }

    @Override
    public double[] getDistances() {
        top += rand.nextGaussian();
        left += rand.nextGaussian();
        right += rand.nextGaussian();

        return new double[]{top, left, right};
    }
}
