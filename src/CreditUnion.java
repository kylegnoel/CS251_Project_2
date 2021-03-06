public class CreditUnion {

    private NewCustomerQueue cq;
    private int cr_threshold;
    private int capacity;
    private int processed = 0;
    private int seenByManager = 0;
    private int sentToBank = 0;
    private int walkedOut = 0;

    public CreditUnion(int cap, int cr_threshold) {
        //TO BE COMPLETED
    }

    public int cr_threshold() {
        return this.cr_threshold;
    }

    public int capacity() {
        return this.capacity;
    }

    /*process a new Customer:
     *if their investment value is higher than the cr_threshold,
     *send them directly to the emergency room and return null
     *otherwise, try to insert them into the queue
     *if the queue is full, compare their investment to the highest
     *investment currently in the queue; if their investment is higher,
     *send them to the Bank and return null; if the current max
     *is higher, send the max Customer to the Bank, insert
     *the new Customer into the queue, and return the name
     *of the max Customer
     */
    public String process(String name, int investment) {
        //TO BE COMPLETED
        processed++;
        return null;
    }

    /*a Manager is available--send the Customer with
     *highest investment value to be seen; return the name
     *of the Customer or null if the queue is empty*/
    public String seeNext() {
        //TO BE COMPLETED
        return null;
    }

    /*Customer experiences an emergency, raising their
     *investment value; if the investment value exceeds the
     *cr_threshold, send them directly to the bank;
     *else update their investment value in the queue;
     *return true if the Customer is removed from the queue
     *and false otherwise*/
    public boolean handle_emergency(String name) {
        //TO BE COMPLETED
        return false;
    }

    /*Customer decides to walk out
     *remove them from the queue*/
    public void walk_out(String name) {
        //TO BE COMPLETED
        walkedOut++;
    }

    /*Indicates that Customer c has been sent to the Bank*/
    private void sendToBank(Customer c) {
        System.out.println("Customer " + c + " sent to Bank.");
        sentToBank++;
    }

    /*Indicates that a Customer is being seen by a Manager*/
    private void seeManager(Customer c) {
        System.out.println("Customer " + c + " is seeing a manager.");
        seenByManager++;
    }

    public int processed() {
        return processed;
    }

    public int sentToBank() {
        return sentToBank;
    }

    public int seenByManager() {
        return seenByManager;
    }

    public int walkedOut() {
        return walkedOut;
    }
}