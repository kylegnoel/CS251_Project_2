import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.security.NoSuchAlgorithmException;

public class Part4SimTest {
    public static ArrayList<String> customers;//all Customers
    public static ArrayList<Customer> inQueue;//Customers in the queue
    public static CreditUnion creditunion;
    public static Random gen;
    public static int numCustomers;//total number of Customers
    public static int pWalkIn;//probability that a new Customer walks in
    public static int pManagerAvailable;//probability that a manager is available
    public static int pEmergency;//probability that a Customer experiences an emergency
    public static int pWalkOut;//probability that a Customer walks out

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //get Customer list
        customers = new ArrayList<String>();
        inQueue = new ArrayList<Customer>();
        String customerFile = args[0];
        getCustomers(customerFile);
        numCustomers = customers.size();

        //set up creditunion and Random generator
        creditunion = new CreditUnion(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        gen = new Random(System.currentTimeMillis());

        //set probability variables
        pWalkIn = Integer.parseInt(args[3]);
        pManagerAvailable = Integer.parseInt(args[4]);
        pEmergency = Integer.parseInt(args[5]);
        pWalkOut = Integer.parseInt(args[6]);

        //process a few customers to start with
        int num = gen.nextInt(20) + 5;
        for (int i = 0; i < num; i++)
            process();

        //continue simulation until all the customers are handled
        while (!customers.isEmpty() || !inQueue.isEmpty()) {
            System.out.println("customers left: " + customers.size());
            System.out.println("customers in queue: " + inQueue.size());
            int prob = gen.nextInt(100);
            if (prob <= pWalkIn)
                process();
            if (prob <= pManagerAvailable)
                seeNextCustomer();
            if (prob <= pEmergency)
                emergency();
            if (prob <= pWalkOut)
                walkOut();
        }

        //print statistics
        System.out.println("Number of Customers processed: " + creditunion.processed());
        System.out.println("Number of Customers sent to Bank: " + creditunion.sentToBank());
        System.out.println("Number of Customers seen by manager: " + creditunion.seenByManager());
        System.out.println("Number of Customers who walked out: " + creditunion.walkedOut());

        //print score
        int score = 0;
        if (creditunion.processed() == numCustomers) {
            System.out.println("All customers successfully processed!");
            score += 12;
        } else {
            System.out.println("Not all the customers were processed!");
        }
        if (creditunion.sentToBank() + creditunion.seenByManager() + creditunion.walkedOut() == numCustomers) {
            System.out.println("The numbers add up!");
            score += 13;
        } else {
            System.out.println("The numbers don't add up!");
        }
        System.out.println("Part 4 Total Score: " + score);
    }

    private static void process() throws NoSuchAlgorithmException {
        System.out.println("process, inQueue: " + inQueue.size());
        if (customers.isEmpty())
            return;
        int i = gen.nextInt(customers.size());
        Customer c = new Customer(customers.remove(i), gen.nextInt(creditunion.cr_threshold() + 5), System.currentTimeMillis());
        String name = creditunion.process(c.name(), c.investment());
        if (name != null) {
            inQueue.add(c);
            if (!name.equals(c.name()))
                removeFromList(name);
        }
    }

    private static void seeNextCustomer() throws NoSuchAlgorithmException {
        System.out.println("seeNext, inQueue: " + inQueue.size());
        if (inQueue.isEmpty())
            return;
        String name = creditunion.seeNext();
        if (name != null)
            removeFromList(name);
    }

    private static void removeFromList(String name) {
        for (int i = 0; i < inQueue.size(); i++) {
            if (inQueue.get(i).name().equals(name)) {
                inQueue.remove(i);
                break;
            }
        }
    }

    private static void emergency() throws NoSuchAlgorithmException {
        System.out.println("emergency, inQueue: " + inQueue.size());
        if (inQueue.isEmpty())
            return;
        int i = gen.nextInt(inQueue.size());
        Customer c = inQueue.get(i);
        int urg = c.investment();
        urg += gen.nextInt(5) + 1;
        c.setInvestment(urg);
        if (creditunion.handle_emergency(c.name()))
            inQueue.remove(i);
    }

    private static void walkOut() throws NoSuchAlgorithmException {
        System.out.println("walkout, inQueue: " + inQueue.size());
        if (inQueue.isEmpty())
            return;
        int i = gen.nextInt(inQueue.size());
        Customer c = inQueue.remove(i);
        creditunion.walk_out(c.name());
    }

    private static void getCustomers(String fn) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fn));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(",");
                if (split.length >= 2) {
                    customers.add(split[0]);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}