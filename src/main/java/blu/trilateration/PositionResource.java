package blu.trilateration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.stat.descriptive.moment.SemiVariance;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PositionResource extends CoapResource {

    private String message = "Hello, CoAP!";

    private final DistanceData distanceData;

    public PositionResource(String name, DistanceData distanceData) {
        super(name);
        getAttributes().setTitle("Position Resource");
        this.distanceData = distanceData;
    }

    @Override
    public void handleGET(CoapExchange exchange) {

        exchange.accept();

        // Respond with the current message
        double[][] positions = new double[][]{{0.0, 6.0}, {-6.0, 0.0}, {6.0, 0.0}};
        double[] distances = distanceData.getDistances();

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();
        Map<String, Double> coordinatesMap = new HashMap<>();
        coordinatesMap.put("x", BigDecimal.valueOf(centroid[0])
                 .setScale(3, RoundingMode.HALF_UP)
                .doubleValue());

        coordinatesMap.put("y", BigDecimal.valueOf(centroid[1])
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue());

        //Write to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            // Convert the HashMap to a JSON string
            jsonString = objectMapper.writeValueAsString(coordinatesMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        exchange.respond(jsonString);
    }

}
