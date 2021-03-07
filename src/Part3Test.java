import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Part3Test {
    public static ArrayList<Customer> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random gen = new Random(System.currentTimeMillis());
        customers = new ArrayList<Customer>();
        getCustomers(args[0]);
        ArrayList<Customer> list = new ArrayList<Customer>();
        int cap = customers.size();
        NewCustomerQueue pq = new NewCustomerQueue(cap);
        double score = 0;

        //insert some customers
        insertCustomers(list, pq, cap);
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after inserts!");
        //check posInQueue
        boolean pass = checkPosInQueue(pq);
        if (pass) {
            printMsg(true, "posInQueue after inserts");
            score += 5.0;//5.0
        } else {
            printMsg(false, "posInQueue after inserts");
        }

        //check posInQueue after delMax
        Customer max1 = list.remove(0);
        Customer max2 = pq.delMax();
        if (max1 == max2 && max2.posInQueue() == -1) {
            printMsg(true, "posInQueue after delMax");
            score += 2.0;//7.0
        } else {
            printMsg(false, "posInQueue after delMax");
        }
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after delMax!");

        //update a Customer to highest urgency
        int i = gen.nextInt(list.size() / 2) + list.size() / 2 - 1;
        Customer p = list.remove(i);
        pq.update(p.name(), 100);
        if (p == pq.delMax()) {
            printMsg(true, "update to max");
            score += 2.0;//9.0
        } else {
            printMsg(false, "update to max");
        }
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after update!");

        //remove a Customer
        i = gen.nextInt(list.size() / 2) + list.size() / 2 - 1;
        p = list.remove(i);
        Customer p2 = pq.remove(p.name());
        if (p == p2 && p2.posInQueue() == -1 && !inQueue(p2, pq)) {
            printMsg(true, "remove");
            score += 2.0;//11.0
        } else {
            printMsg(false, "remove");
            printExpAct(p.toString(), p2.toString());
            System.out.println("If the Customers match, check that posInQueue is set to -1 and that the Customer is really removed from the queue!");
        }
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after removing a Customer!");

        //remove several Customers
        pass = true;
        for (int j = 0; j < 10; j++) {
            i = gen.nextInt(list.size());
            p = list.remove(i);
            p2 = pq.remove(p.name());
            if (p != p2) {
                printMsg(false, "remove several");
                pass = false;
                printExpAct(p == null ? "null" : p.toString(), p2 == null ? "null" : p2.toString());
                break;
            }
        }
        if (pass) {
            printMsg(true, "remove several");
            score += 2.0;//13.0
        }
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after removing several Customers!");
        //check the posInQueue
        pass = checkPosInQueue(pq);
        if (pass) {
            printMsg(true, "posInQueue after removing several customers");
            score += 3.0;//16.0
        } else
            printMsg(false, "posInQueue after removing several customers");

        //update several
        int[] diffs = new int[]{-3, -2, -1, 1, 2, 3};
        for (int j = 0; j < 10; j++) {
            i = gen.nextInt(list.size());
            int diff = diffs[gen.nextInt(diffs.length)];
            int urg = list.get(i).investment() + diff;
            if (urg < 0)
                urg += 5;
            pq.update(list.get(i).name(), urg);
            updateInList(list, i, urg);
        }
        pass = checkPosInQueue(pq);
        if (pass) {
            printMsg(true, "posInQueue after updates");
            score += 4.0;//20.0
        } else {
            printMsg(false, "posInQueue after updates");
        }
        if (list.size() != pq.size())
            System.out.println("Sizes do not match after updating several Customers!");

        //delMax until empty
        pass = true;
        while (!list.isEmpty()) {
            Customer pExp = list.remove(0);
            Customer a = pq.getMax();
            if (pExp != a) {
                System.out.println("HOLD UP");
                Customer c = pq.get(pExp.name());
                System.out.println(c.posInQueue());
            }
            Customer pAct = pq.delMax();
            if (pExp != pAct || pAct.posInQueue() != -1) {
                printMsg(false, "delMax until empty");
                printExpAct(pExp.toString(), pAct.toString());
                pass = false;
                break;
            }
            if (list.size() != pq.size())
                System.out.println("Sizes do not match during delMax until empty!");
        }
        if (pass) {
            printMsg(true, "delMax until empty");
            score += 5.0;//25.0
        }

        System.out.println("\nTotal score for Part 3: " + score);
    }

    private static void updateInList(ArrayList<Customer> list, int i, int urg) {
        Customer p = list.remove(i);
        p.setInvestment(urg);
        insertToList(list, p);
    }


    private static void printMsg(boolean passed, String method) {
        if (passed)
            System.out.println(method + " passed");
        else
            System.out.println(method + " failed");
    }

    private static void printExpAct(String exp, String act) {
        System.out.println("Expected: " + exp);
        System.out.println("Actual: " + act);
    }

    private static void printArray(NewCustomerQueue pq) {
        Customer[] array = pq.getArray();
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i] + " | ");
    }

    private static void printList(ArrayList<Customer> list) {
        System.out.println("\nLIST:");
        if (list == null)
            return;
        for (Customer p : list)
            System.out.print(p.toString() + " | ");
    }

    private static void insertCustomers(ArrayList<Customer> list, NewCustomerQueue pq, int num) throws NoSuchAlgorithmException {
        int count = 0;
        while (count < num) {
            Customer p = customers.get(index);
            pq.insert(p);
            insertToList(list, p);
            incIndex();
            count++;
        }
    }

    private static void insertToList(ArrayList<Customer> list, Customer p) {
        for (int i = 0; i < list.size(); i++) {
            if (p.compareTo(list.get(i)) > 0) {
                list.add(i, p);
                return;
            }
        }
        list.add(p);
    }

    private static void getCustomers(String fn) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fn));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(",");
                if (split.length >= 2) {
                    Customer p = new Customer(split[0], Integer.parseInt(split[1]), time++);
                    customers.add(p);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void incIndex() {
        index = (index + 1) % customers.size();
    }

    private static void printQueue(NewCustomerQueue pq) {
        System.out.println("\nQUEUE: ");
        Customer[] array = pq.getArray();
        for (int i = 1; i < pq.size(); i++)
            System.out.print(array[i].toString() + " | ");
    }

    private static boolean checkPosInQueue(NewCustomerQueue pq) {
        Customer[] array = pq.getArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                if (array[i].posInQueue() != i) {
                    System.out.println("posInQueue does not match index");
                    printExpAct("" + i, array[i].toStringWithPos());
                    return false;
                }
            }
        }
        return true;
    }

    //checks if Customer p is currently in the queue
    private static boolean inQueue(Customer p, NewCustomerQueue pq) {
        Customer[] array = pq.getArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].name().equals(p.name()))
                return true;
        }
        return false;
    }
}

    
	