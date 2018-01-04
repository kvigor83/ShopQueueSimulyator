package by.kastsiuchenka.kassa.simulyator;

public class DeskReport {
    private int deskNo = 0;
    private int servedCustomerCount = 0;
    private int soldItemCount = 0;
    private int spentTime = 0;

    public DeskReport(int deskNo, int servedCustomerCount, int soldItemCount, int spentTime){
        this.deskNo = deskNo;
        this.servedCustomerCount = servedCustomerCount;
        this.soldItemCount = soldItemCount;
        this.spentTime = spentTime;
    }

    public int getDeskNo() {
        return deskNo;
    }

    public int getServedClientCount() {
        return servedCustomerCount;
    }

    public int getSoldItemCount() {
        return soldItemCount;
    }

    public int getSpentTime() {
        return spentTime;
    }

}
