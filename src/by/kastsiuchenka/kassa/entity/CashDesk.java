package by.kastsiuchenka.kassa.entity;

import by.kastsiuchenka.kassa.action.QueueMonitor;
import by.kastsiuchenka.kassa.simulyator.Simulation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CashDesk implements Runnable {
    private Simulation simulation = null;
    private int cashDeskNo;
    private int servedClientCount;
    private int soldItemCount;
    private int spentTime;
    private CashDeskQueue deskQueue;

    public CashDesk(Simulation simulation, int cashDeskNo, CashDeskQueue deskQueue) {
        this.simulation = simulation;
        this.cashDeskNo = cashDeskNo;
        this.deskQueue = deskQueue;
    }

    @Override
    public void run() {
        System.out.println("Desk " + cashDeskNo + " open...");
        Client client;
        ExecutorService replaceExecutor = Executors.newSingleThreadExecutor();
        while (!deskQueue.isEmpty() || simulation.getClientFactory().getNotServedClientCount() != 0) {
            client = deskQueue.getFirstClient();
            //calculating time for current client service
            for (int i = client.getItemCount(); i > 1; i--) {
                soldItemCount++;
                spentTime += simulation.getRandomIntInRange(simulation.getMIN_TIME_SERVE_ITEM(), simulation.getMAX_TIME_SERVE_ITEM());
            }
            //time for client service
            simulation.sleepFor(spentTime);
            servedClientCount++;
            deskQueue.removeFirstClient();
            replaceExecutor.execute(new QueueMonitor(simulation.getQueueList(), cashDeskNo));
        }
        System.out.println("Desk " + cashDeskNo + "\tclosed.\tcustomers " + servedClientCount + "\titems " + soldItemCount + "\ttime(simulation) " + spentTime);
    }
}
