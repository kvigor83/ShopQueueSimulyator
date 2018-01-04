package by.kastsiuchenka.kassa.factory;

import by.kastsiuchenka.kassa.entity.Client;
import by.kastsiuchenka.kassa.simulyator.Simulation;

import java.util.concurrent.locks.ReentrantLock;

public class ClientFactory {
    private Simulation simulation;
    private int notServedClientCount;
    private int clientId = 1;
    private ReentrantLock lock = new ReentrantLock();

    public ClientFactory(Simulation simulation, int numberClient) {
        this.simulation = simulation;
        notServedClientCount = numberClient;
    }

    public int getNotServedClientCount() {
        return notServedClientCount;
    }

    public Client getClientWithRandomCapacity() {
        lock.lock(); //
        if (notServedClientCount > 0) {
            notServedClientCount--;
            lock.unlock(); //
            return new Client(clientId++, simulation.getRandomIntInRange(1, 15), false);
        } else {
            lock.unlock(); //
            return null;
        }
    }
}
