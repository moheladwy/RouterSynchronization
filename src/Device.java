import java.util.concurrent.TimeUnit;

public class Device extends Thread {
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
//        System.out.println("Connection "+ connectionNumber +": (" + name + ") Logged in.");
        System.out.println("Connection "+ connectionNumber +": (" + name + ") Logged in.");
    }

    private void performOnlineActivity() {
        System.out.println("Connection "+ connectionNumber +": (" + name + ") is performing online activity.");
    }

    private void logout() {
        System.out.println("Connection "+ connectionNumber +": (" + name + ") Logged out.");
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
            System.err.println("Device.sleepTask() - caught InterruptedException: " + exception.getMessage());
        }
    }
}
