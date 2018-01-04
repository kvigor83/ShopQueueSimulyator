package by.kastsiuchenka.kassa.entity;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CashDeskQueue {
    private static Logger logger = LogManager.getRootLogger();
    private int queueNo;
    private int counter;
    private ArrayDeque<Client> queue = new ArrayDeque<>();
    private ReentrantLock queueLock = new ReentrantLock();
    private Condition condition = queueLock.newCondition();

    public CashDeskQueue(int queueNo) {

        this.queueNo = queueNo;
    }

    public int getCounter() {
        return counter;
    }

    public void addClient(Client client) {
        queueLock.lock();
        try {
            queue.add(client);
            counter++;
            System.out.println(">queue" + queueNo + " " + queue.toString());
            condition.signal();
        } finally {
            queueLock.unlock();
        }
    }

    public Client getFirstClient() {
        Client client = null;
        queueLock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            client = queue.peekFirst();
        } catch (InterruptedException e) {
            logger.info("CashDesk waiting for client has been interrupted ", e);
        } finally {
            queueLock.unlock();
        }
        return client;
    }

    public void removeFirstClient() {
        Client client;
        queueLock.lock();
        try {
            client = queue.pollFirst(); //If the queue is empty, returns null
            counter--;
            System.out.println("< **desk" + queueNo + " served client " + client.getClientNo() + " queue" + queueNo + ""
                    + this.toString());
        } finally {
            queueLock.unlock();
        }
    }

    public Client getLastWithRemove() {
        Client client;
        queueLock.lock();
        try {
            client = queue.pollLast();
            counter--;
            System.out.println("<< queue" + queueNo + "" + this.toString());
            condition.signal();
        } finally {
            queueLock.unlock();
        }
        return client;
    }

    public boolean isEmpty() {
        return queue.peek() == null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Client client : queue) {
            sb.append(client.toString() + " ");
        }
        return sb.toString();
    }
}
