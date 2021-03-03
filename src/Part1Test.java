import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Part1Test {
    public static ArrayList<Customer> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;

    public static void main(String[] args) {
        customers = new ArrayList<Customer>();
        getCustomers(args[0]);
        ArrayList<Customer> list = new ArrayList<Customer>();
        double score = 0;

        //Test 1
        testNum++;
        score += test1_2(32, 25, list);

        //Test 2
        testNum++;
        score += test1_2(7, customers.size() - 1, list);

        //Test 3
        testNum++;
        score += test3(30, 17);

        score = Math.max(0.0, score);

        System.out.println("\nTotal score for Part 1: " + score);
    }

    private static double test1_2(int i, int cap, ArrayList<Customer> list) {
        System.out.println("\n*****Begin Test " + testNum + "*****");
        index = i;
        CustomerQueue pq = new CustomerQueue(cap);
        double score = 0;
        //check operations on empty PQ
        System.out.println("\nPQ is empty...");
        if (pq.size() == 0) {
            printMsg(true, "size");
            score += 0.5;//0.5
        } else
            printMsg(false, "size");
        if (pq.isEmpty()) {
            score += 0.5;//1.0
            printMsg(true, "isEmpty");
        } else
            printMsg(false, "isEmpty");
        if (pq.getMax() == null) {
            score += 0.5;//1.5
            printMsg(true, "getMax");
        } else
            printMsg(false, "getMax");
        if (pq.delMax() == null) {
            score += 0.5;//2.0
            printMsg(true, "delMax");
        } else
            printMsg(false, "delMax");

        //insert some Customers
        System.out.println("\nAdding some customers...");
        insertCustomers(list, pq, (int) (cap / 2));
        if (pq.size() == list.size()) {
            score += 0.5;//2.5
            printMsg(true, "size");
        } else
            printMsg(false, "size");
        if (!pq.isEmpty()) {
            score += 0.5;//3.0
            printMsg(true, "isEmpty");
        } else
            printMsg(false, "isEmpty");
        if (pq.getMax() == list.get(0)) {
            score += 1.0;//4.0
            printMsg(true, "getMax");
        } else {
            printMsg(false, "getMax");
        }
        if (pq.delMax() == list.remove(0) && pq.size() == list.size()) {
            score += 1.0;//5.0
            printMsg(true, "delMax");
        } else
            printMsg(false, "delMax");

        //fill up the queue
        System.out.println("\nFilling up the PQ...");
        insertCustomers(list, pq, cap - list.size());

        if (pq.size() == list.size()) {
            score += 0.5;//5.5
            printMsg(true, "size");
        } else
            printMsg(false, "size");
        if (pq.insert(customers.get(index)) == -1 && pq.size() == list.size()) {
            score += 1.0;//6.5
            printMsg(true, "insert");
        } else
            printMsg(false, "insert");

        //remove all remaining customers
        boolean pass = true;
        while (!list.isEmpty()) {
            Customer lp = list.remove(0);
            Customer qp = pq.delMax();
            if (lp != qp) {
                pass = false;
                System.out.println("Max customers not equal");
                printExpAct(lp.toString(), qp.toString());
                break;
            }
        }
        if (!pq.isEmpty())
            pass = false;
        if (pass) {
            score += 2.5;//9.0
            printMsg(true, "delMax until empty");
        } else
            printMsg(false, "delMax until empty");
        System.out.println("\nTotal score for Test " + testNum + ": " + score);
        return score;
    }

    private static double test3(int size, int start_index) {
        System.out.println("\n*****Begin Test 3*****");
        String oFile = "c1_test3_output_" + size + "_" + start_index + ".txt";
        CustomerQueue pq = new CustomerQueue(size);
        index = start_index;
        for (int i = 0; i < size; i++) {
            pq.insert(customers.get(index));
            incIndex();
        }
        String exp = getExp(oFile);
        String act = getAct(pq);
        if (exp.equals(act)) {
            System.out.println("Test 3 passed!");
            return 7.0;
        } else {
            System.out.println("Test 3 failed: underlying array not correct");
            printExpAct(exp, act);
        }
        return -18.0;
    }

    private static String getExp(String fn) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fn));
            String line = br.readLine();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getAct(CustomerQueue pq) {
        Customer[] array = pq.getArray();
        String ret = "";
        for (int i = 0; i < array.length; i++)
            if (array[i] != null)
                ret += "|" + array[i].toStringForTesting() + "|";
        return ret;
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

    private static void printArray(CustomerQueue pq) {
        Customer[] array = pq.getArray();
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i] + " | ");
    }

    private static void insertCustomers(ArrayList<Customer> list, CustomerQueue pq, int num) {
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

    private static void printList(ArrayList<Customer> list) {
        System.out.println("\nLIST: ");
        for (Customer p : list)
            System.out.print(p.toString() + " | ");
    }

    private static void printQueue(CustomerQueue pq) {
        System.out.println("\nQUEUE: ");
        Customer[] array = pq.getArray();
        for (int i = 1; i < pq.size(); i++)
            System.out.print(array[i].toString() + " | ");
    }
}

    
	