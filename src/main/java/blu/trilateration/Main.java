package blu.trilateration;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.elements.config.TcpConfig;
import org.eclipse.californium.elements.config.UdpConfig;

public class Main {

    static {
        CoapConfig.register();
        UdpConfig.register();
        TcpConfig.register();
    }

    public static void main(String[] args) {
        if (args.length != 8) {
            System.out.println("Usage: --serial <string> --top <string> --left <string> --right <string>");
            return;
        }

        String top = null;
        String left = null;
        String right = null;
        String serialPort = null;

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "--serial":
                    serialPort = args[i+1];
                case "--top":
                    top = args[i + 1];
                    break;
                case "--left":
                    left = args[i + 1];
                    break;
                case "--right":
                    right = args[i + 1];
                    break;
                default:
                    System.out.println("Unknown argument: " + args[i]);
                    System.out.println("Usage: --serial <string> --top <string> --left <string> --right <string>");
                    return;
            }
        }

        if (serialPort == null | top == null || left == null || right == null) {
            System.out.println("Missing required arguments.");
            System.out.println("Usage: --serial <string> --top <string> --left <string> --right <string>");
            return;
        }

        //Create new distance data object
        DistanceData distanceData = new DistanceData(top, left, right);

        //Create a new serial reader and start the thread
        NRFSerialReader nrfSerialReader = new NRFSerialReader(serialPort, distanceData);
        nrfSerialReader.start();

        //Create a new CoAP Server
        CoapServer server = new CoapServer();
        server.add(new PositionResource("position", distanceData));
        server.start();
        System.out.println("CoAP server is running on port 5683...");
    }
}