public class Router {
    final Semaphore semaphore;

    public Router(int wifiConnections) {
        semaphore = new Semaphore(wifiConnections);
    }

    public void connect(Device device) {
        semaphore.acquire(device);
        device.performTask();
        semaphore.release();
    }
}
