import java.util.ArrayList;
import java.util.Scanner;

public class Network {
    Router router;

    public Network(int wifiConnections) {
        router = new Router(wifiConnections);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Network Simulator!");
        System.out.println("=================================");

        // Take the number of WI-FI Connections and number of devicesNumber from user.
        Scanner scanner = new Scanner(System.in);
        System.out.print("What is the number of WI-FI Connections? ");
        int wifiConnections = Integer.parseInt(scanner.nextLine());

        System.out.print("What is the number of devices Clients want to connect? ");
        int devicesNumber = Integer.parseInt(scanner.nextLine());

        Network network = new Network(wifiConnections);
        ArrayList<Device> deviceList = new ArrayList<>(devicesNumber);
        for (int i = 0; i < devicesNumber; i++) {
            // Take Device information from user.
            System.out.print("Enter the device name and type (mobile/tablet/pc) separated by space: ");
            String input = scanner.nextLine();

            // Split the input into name and type.
            String[] deviceInfo = input.split(" ");
            String name = deviceInfo[0];
            String type = deviceInfo[1];

            // Create a new device and add it to the deviceList.
            deviceList.add(i, new Device(network.router, type, name));
        }
        System.out.println("============================================================================");
        // Start all the devices in the deviceList.
        for (Device device: deviceList)
            device.start();

        scanner.close();
    }
}
