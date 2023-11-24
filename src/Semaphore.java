public class Semaphore {
    public final int maxConnection;

    private int value;

    protected Semaphore(int maxConnection) {
        this.value = maxConnection;
        this.maxConnection = maxConnection;
    }

    public synchronized void acquire(Device device) {
        value-- ;
        if (value < 0) {
            try {
                System.out.println("- (" + device.getDeviceName() + ") (" + device.getType() + ") arrived and waiting.");
                wait();
            } catch (InterruptedException exception) {
                System.err.println("Semaphore.P() - caught InterruptedException: " + exception.getMessage());
            }
        }
        else {
            System.out.println("- (" + device.getDeviceName() + ") (" + device.getType() + ") arrived.");
        }
        device.setConnectionNumber(maxConnection - getValue());
        System.out.println("Connection " + device.getConnectionNumber() + ": (" + device.getDeviceName() + ") Occupied.");
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
