import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.io.*;


class Semaphore {
    public final int maxConnection;

    private int value;
    private Queue<Device> deviceQueue;
    protected Semaphore(int maxConnection) {
        this.value = maxConnection;
        this.maxConnection = maxConnection;
        this.deviceQueue = new ArrayDeque<>();
    }

    public synchronized void acquire(Device device) {
        value-- ;
        if (value < 0) {
            try {
                WriteToFile.write("- (" + device.getDeviceName() + ") (" + device.getType() + ") arrived and waiting.\n");
                wait();
            } catch (InterruptedException exception) {
                WriteToFile.write("Semaphore.P() - caught InterruptedException: " + exception.getMessage() + "\n");
            }
        }
        else {
            WriteToFile.write("- (" + device.getDeviceName() + ") (" + device.getType() + ") arrived.\n");
        }
        device.setConnectionNumber(device.getConnectedRouter().getEmptyConnection());
        WriteToFile.write("Connection " + device.getConnectionNumber() + ": (" + device.getDeviceName() + ") Occupied.\n");
    }

    public synchronized int getValue() {
        return value;
    }

    public synchronized void release() {
        value++;
        if (value <= 0)
            notify();
    }
}

//======================================================================================================================

class Device extends Thread {
    private final String type;
    private final String name;
    private final Router connectedRouter;
    private int connectionNumber;

    public Device(Router router, String type, String name) {
        this.connectedRouter = router;
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getDeviceName() {
        return name;
    }

    public Router getConnectedRouter() {
        return connectedRouter;
    }

    @Override
    public void run() {
        connectedRouter.connect(this);
    }

    public void performTask() {
        login();
        performOnlineActivity();
        logout();
    }

    private void login() {
        WriteToFile.write("Connection "+ connectionNumber +": (" + name + ") Logged in.\n");
    }

    private void performOnlineActivity() {
        WriteToFile.write("Connection "+ connectionNumber +": (" + name + ") is performing online activity.\n");
    }

    private void logout() {
        WriteToFile.write("Connection "+ connectionNumber +": (" + name + ") Logged out.\n");
    }

    public void setConnectionNumber(int connectionNumber) {
        this.connectionNumber = connectionNumber;
    }

    public int getConnectionNumber() {
        return connectionNumber;
    }

    public void sleepTask() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException exception) {
            WriteToFile.write("Device.sleepTask() - caught InterruptedException: " + exception.getMessage() + "\n");
        }
    }
}

//======================================================================================================================

class Router {
    final Semaphore semaphore;
    private ArrayList<Device> connections;
    public Router(int wifiConnections) {
        semaphore = new Semaphore(wifiConnections);
        connections = new ArrayList<>(wifiConnections);
        for (int i = 0; i < wifiConnections; i++) {
            connections.add(null);
        }
    }

    public void connect(Device device) {
        semaphore.acquire(device);
        connections.add(device.getConnectionNumber() - 1, device);
        device.performTask();
        connections.add(device.getConnectionNumber() - 1, null);
        semaphore.release();
    }

    public int getEmptyConnection(){
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i) == null){
                return i + 1;
            }
        }
        return -1;
    }

}
//=====================================================================================================================
class WriteToFile {
    private static final String fileName = "log.txt";
    public static void write(String str)
    {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }
}

//======================================================================================================================

class Network {
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