package by.kastsiuchenka.kassa.action;

import by.kastsiuchenka.kassa.entity.CashDeskQueue;
import by.kastsiuchenka.kassa.entity.Client;

import java.util.ArrayList;

public class QueueMonitor implements Runnable {
    private ArrayList<CashDeskQueue> queueList = new ArrayList<>();
    private int currentQueueNo;

    public QueueMonitor(ArrayList<CashDeskQueue> queueList, int currentQueueNo) {
        this.queueList = queueList;
        this.currentQueueNo = currentQueueNo;
    }

    @Override
    public void run() {
        Client client;
        int currentQueueSize = queueList.get(currentQueueNo - 1).getCounter();
        for (int i = 0; i < queueList.size(); i++) {
            if (queueList.get(i).getCounter() - 1 > currentQueueSize) {
                System.out.println("client from desk" + (i + 1) + " GO TO desk" + currentQueueNo);
                client = queueList.get(i).getLastWithRemove();
                queueList.get(currentQueueNo - 1).addClient(client);
            }

        }
    }
}
