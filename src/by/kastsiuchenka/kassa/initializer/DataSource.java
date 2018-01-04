package by.kastsiuchenka.kassa.initializer;

import by.kastsiuchenka.kassa.exception.BundleException;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class DataSource {

    private static DataSource dataSource;
    private static AtomicBoolean instanceCreated = new AtomicBoolean(false);
    private static ReentrantLock lock = new ReentrantLock();

    private final int TOTAL_CLIENT_NUMBER;
    private final int DESK_NUMBER;
    private final int MIN_TIME_SERVE_ITEM;
    private final int MAX_TIME_SERVE_ITEM;


    {
        ResourceBundle rb = ResourceBundle.getBundle("data");
        if (rb == null) {
            TOTAL_CLIENT_NUMBER = 0;
            DESK_NUMBER = 0;
            MIN_TIME_SERVE_ITEM = 0;
            MAX_TIME_SERVE_ITEM = 0;
            System.out.println("Bundle not initialized");
        } else {
            TOTAL_CLIENT_NUMBER = Integer.parseInt(rb.getString("TOTAL_CLIENT_NUMBER"));
            DESK_NUMBER = Integer.parseInt(rb.getString("DESK_NUMBER"));
            MIN_TIME_SERVE_ITEM = Integer.parseInt(rb.getString("MIN_TIME_SERVE_ITEM"));
            MAX_TIME_SERVE_ITEM = Integer.parseInt(rb.getString("MAX_TIME_SERVE_ITEM"));
        }
    }

    private DataSource() {
    }

    public int getTOTAL_CLIENT_NUMBER() {
        return TOTAL_CLIENT_NUMBER;
    }

    public int getDESK_NUMBER() {
        return DESK_NUMBER;
    }

    public int getMIN_TIME_SERVE_ITEM() {
        return MIN_TIME_SERVE_ITEM;
    }

    public int getMAX_TIME_SERVE_ITEM() {
        return MAX_TIME_SERVE_ITEM;
    }

    public static DataSource getInstance() throws BundleException {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (!instanceCreated.get()) {
                    dataSource = new DataSource();
                    instanceCreated.set(true);
                }
            } catch (Exception e) {
                throw new BundleException(e);
            } finally {
                lock.unlock();
            }
        }
        return dataSource;

    }

}
