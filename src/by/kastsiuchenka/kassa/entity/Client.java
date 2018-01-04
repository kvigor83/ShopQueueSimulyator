package by.kastsiuchenka.kassa.entity;

public class Client {
    private int clientNo;
    private int itemCount;
    private boolean preOrder;

    public Client(int clientNo, int itemCount, boolean preOrder) {
        this.clientNo = clientNo;
        this.itemCount = itemCount;
        this.preOrder = preOrder;
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public int getClientNo() {
        return clientNo;
    }

    public String toString() {
        return "{Client " + clientNo + " ItemCount " + itemCount + "}";

    }

}