import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CreditUnion {

    private NewCustomerQueue cq;
    private int cr_threshold;
    private int capacity;
    private int processed = 0;
    private int seenByManager = 0;
    private int sentToBank = 0;
    private int walkedOut = 0;

    public CreditUnion(int cap, int cr_threshold) {
        this.cq = new NewCustomerQueue(cap);
        this.cr_threshold = cr_threshold;
        this.capacity = cap;
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
    public String process(String name, int investment) throws NoSuchAlgorithmException {
//        System.out.println("capacity: " + capacity);
//        System.out.println("Threshold: " + cr_threshold);
//        System.out.println("probability walkin: " + Part4SimTest.pWalkIn);
//        System.out.println("probability manager available: " + Part4SimTest.pManagerAvailable);
//        System.out.println("probability emergency: "+ Part4SimTest.pEmergency);
//        System.out.println("probability walkout: " + Part4SimTest.pWalkOut);
        Customer customer = new Customer(name, investment,System.currentTimeMillis());
        processed++;

        if (investment > cr_threshold) {
            sendToBank(customer);
            return null;
        }
        if (cq.size() == capacity && investment > cq.getMax().investment()) {
            sendToBank(customer);
            return null;
        }
        if (cq.size() == capacity && investment <= cq.getMax().investment()) {
            sendToBank(cq.delMax());
            cq.insert(customer);
            return name;
        }
        cq.insert(customer);
        return name;
    }

    /*a Manager is available--send the Customer with
     *highest investment value to be seen; return the name
     *of the Customer or null if the queue is empty*/
    public String seeNext() throws NoSuchAlgorithmException {
        if (cq.isEmpty())
            return null;
        Customer c = cq.delMax();
        seeManager(c);
        return c.name();
    }

    /*Customer experiences an emergency, raising their
     *investment value; if the investment value exceeds the
     *cr_threshold, send them directly to the bank;
     *else update their investment value in the queue;
     *return true if the Customer is removed from the queue
     *and false otherwise*/
    public boolean handle_emergency(String name) throws NoSuchAlgorithmException {
        double a = Math.random();
        int b = (int) Math.random() * 9 + 1;
        Customer c = cq.get(name);
        if (c == null)
            return false;
        if (c.posInQueue() == -1 && cq.size() == 1)
            c.setPosInQueue(0);
        int current = c.investment();
        int updated;

        if (a < 0.5) {
            updated = current - (current * (b/100));
        } else {
            updated = current + (current * (b/100));
        }
        c.setInvestment(updated);
        if (updated > cr_threshold) {
            sendToBank(c);
            cq.remove(name);
            return true;
        }
        if (updated <= 0) {
            walk_out(name);
            return true;
        }
        cq.update(name, updated);
        return false;
    }

    /*Customer decides to walk out
     *remove them from the queue*/
    public void walk_out(String name) throws NoSuchAlgorithmException {
        Customer c = cq.remove(name);
        if (c != null)
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