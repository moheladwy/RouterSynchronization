import java.util.ArrayList;

public class Router {
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
