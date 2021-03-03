public class Customer {
    private String name;
    private int investment;
    private long time_in;
    private int posInQueue;//indicates the Customer's position in the CustomerQueue

    public Customer(String name, int investment, long time_in) {
        this.name = name;
        this.investment = investment;
        this.time_in = time_in;
        this.posInQueue = -1;
    }

    public String name() {
        return this.name;
    }

    public int investment() {
        return this.investment;
    }

    public void setInvestment(int investment) {
        this.investment = investment;
    }

    public long time_in() {
        return this.time_in;
    }

    public long compareTo(Customer other) {
        long diff = this.investment - other.investment();
        if (diff == 0) {
            diff = other.time_in() - this.time_in;
        }
        return diff;
    }

    //includes time_in
    public String toString() {
        return name + ", " + investment + ", " + time_in;
    }

    //includes posInQueue
    public String toStringWithPos() {
        return name + ", " + investment + ", " + posInQueue;
    }

    //does not include time_in
    public String toStringForTesting() {
        return name + ", " + investment;
    }

    public int posInQueue() {
        return posInQueue;
    }

    public void setPosInQueue(int pos) {
        this.posInQueue = pos;
    }
}