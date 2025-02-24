package blu.trilateration;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NRFSerialReader {

    private final DistanceData distanceData;
    private String serialPort;

    public NRFSerialReader(String serialPort, DistanceData distanceData) {
        this.serialPort = serialPort;
        this.distanceData = distanceData;
    }

    public void start() {
        SerialNordicReader serialNordicReader = new SerialNordicReader();
        Thread thread = new Thread(serialNordicReader);
        thread.start();
    }

    class SerialNordicReader implements Runnable {
        public void run() {
            SerialPort comPort = SerialPort.getCommPort(serialPort);
            comPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY); // Set baud rate, data bits, etc.
            comPort.openPort();
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

            byte[] buffer = new byte[1024];
            int bytesRead;

            StringBuilder dataBuilder = new StringBuilder();
            Pattern pattern = Pattern.compile("Addr: ([A-F0-9:]+).*?best=([0-9.]+)", Pattern.DOTALL);
            try (InputStream inputStream = comPort.getInputStream()) {
                    System.out.println("Reading and parsing data...");
                    while (true) {
                        while ((bytesRead = inputStream.read(buffer)) > 0) {
                            String chunk = new String(buffer, 0, bytesRead);
                            dataBuilder.append(chunk);

                            // Parse complete entries
                            Matcher matcher = pattern.matcher(dataBuilder.toString());
                            while (matcher.find()) {
                                String addr = matcher.group(1);
                                double bestValue = Double.parseDouble(matcher.group(2));

                                distanceData.setDistance(addr, bestValue);
                            }

                            // Remove processed data to prevent duplicates
                            int lastIndex = dataBuilder.lastIndexOf("Measurement result:");
                            if (lastIndex >= 0) {
                                dataBuilder = new StringBuilder(dataBuilder.substring(lastIndex));
                            }

                            System.out.println(Arrays.toString(distanceData.getDistances()));
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                comPort.closePort();
            }

        }
    }
}
