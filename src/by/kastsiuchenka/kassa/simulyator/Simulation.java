package by.kastsiuchenka.kassa.simulyator;

import by.kastsiuchenka.kassa.entity.Client;
import by.kastsiuchenka.kassa.entity.CashDeskQueue;
import by.kastsiuchenka.kassa.entity.CashDesk;
import by.kastsiuchenka.kassa.exception.BundleException;
import by.kastsiuchenka.kassa.factory.ClientFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import by.kastsiuchenka.kassa.initializer.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Simulation {
    private static Logger logger = LogManager.getRootLogger();
    private static final Random GLOBAL_RANDOM = new Random(System.currentTimeMillis());
    private final int TOTAL_CLIENT_NUMBER;
    private final int DESK_NUMBER;
    private final int MIN_TIME_SERVE_ITEM;
    private final int MAX_TIME_SERVE_ITEM;

    private ClientFactory clientFactory;
    private ArrayList<DeskReport> results;
    private ArrayList<CashDeskQueue> queueList = new ArrayList<>();     //list is used for quick access to queues of desk
    private DataSource dataSource;

    public Simulation() {
        try {
            dataSource = DataSource.getInstance();
        } catch (BundleException e) {
            logger.error("initialization error from file data.properties", e);
            throw new RuntimeException("!!!!ERROR!!!!");
        }

        if (dataSource != null) {
            TOTAL_CLIENT_NUMBER = dataSource.getTOTAL_CLIENT_NUMBER();
            DESK_NUMBER = dataSource.getDESK_NUMBER();
            MIN_TIME_SERVE_ITEM = dataSource.getMIN_TIME_SERVE_ITEM();
            MAX_TIME_SERVE_ITEM = dataSource.getMAX_TIME_SERVE_ITEM();
            results = new ArrayList<>(DESK_NUMBER);
            clientFactory = new ClientFactory(this, TOTAL_CLIENT_NUMBER);
        }else {
            TOTAL_CLIENT_NUMBER = 0;
            DESK_NUMBER = 0;
            MIN_TIME_SERVE_ITEM = 0;
            MAX_TIME_SERVE_ITEM = 0;

        }
    }

    public ArrayList<DeskReport> getResults() {
        return results;
    }

    public ClientFactory getClientFactory() {
        return clientFactory;
    }

    public ArrayList<CashDeskQueue> getQueueList() {
        return queueList;
    }

    public int getMIN_TIME_SERVE_ITEM() {
        return MIN_TIME_SERVE_ITEM;
    }

    public int getMAX_TIME_SERVE_ITEM() {
        return MAX_TIME_SERVE_ITEM;
    }

    void startSimulation() {

        System.out.println("Restaurant open...");

        //cashDeskExecutor starts all threads of CashDesks
        ExecutorService cashDeskExecutor = Executors.newFixedThreadPool(DESK_NUMBER);
        for (int i = 1; i <= DESK_NUMBER; i++) {
            CashDeskQueue queue = new CashDeskQueue(i);
            queueList.add(queue);
            cashDeskExecutor.execute(new CashDesk(this, i, queue));
        }

        //thread places new clients in queue with minimum size
        new Thread(() -> {
            Client client;
            while ((client = clientFactory.getClientWithRandomCapacity()) != null) {
                int minQueueSize = queueList.get(0).getCounter();
                int minQueueNo = 0;
                for (int i = 1; i < DESK_NUMBER; i++) {
                    if (queueList.get(i).getCounter() < minQueueSize) {
                        minQueueSize = queueList.get(i).getCounter();
                        minQueueNo = i;
                    }
                }
                queueList.get(minQueueNo).addClient(client);
                //time between clients
                sleepFor(60);
            }
        }
        ).start();
        cashDeskExecutor.shutdown();
        try {
            cashDeskExecutor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.error("Error. Tasks interrupted", ex);
        } finally {
            if (!cashDeskExecutor.isTerminated()) {
                logger.error("cancel non-finished tasks");
                cashDeskExecutor.shutdownNow();
            }
        }
        System.out.println("restaurant closed");
    }


    public int getRandomIntInRange(int min, int max) {
        return GLOBAL_RANDOM.nextInt(max) + min;
    }

    public void sleepFor(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            logger.error("sleep interrupted", e);
        }
    }

    void printResult() {
        int maxTimeS = 0;
        int maxItems = 0;
        int maxClients = 0;

        int minItems = Integer.MAX_VALUE;
        int minClients = Integer.MAX_VALUE;

        for (DeskReport r : results) {
            if (r.getSpentTime() > maxTimeS) {
                maxTimeS = r.getSpentTime();
            }

            if (r.getSoldItemCount() > maxItems) {
                maxItems = r.getSoldItemCount();
            }
            if (r.getSoldItemCount() < minItems) {
                minItems = r.getSoldItemCount();
            }


            if (r.getServedClientCount() > maxClients) {
                maxClients = r.getServedClientCount();
            }
            if (r.getServedClientCount() < minClients) {
                minClients = r.getServedClientCount();
            }

        }

        Date date = new Date(maxTimeS * 1000L);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);


        System.out.println("---------------- RESULTS ----------------");
        System.out.println(DESK_NUMBER + " desks served " + TOTAL_CLIENT_NUMBER + " clients in " + dateFormatted);
        System.out.println(minItems + "\t~\t" + maxItems + " \titems/desk");
        System.out.println(minClients + "\t~\t" + maxClients + "\tclients/desk");
    }

}
