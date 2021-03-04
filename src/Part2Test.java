import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Part2Test {
    public static ArrayList<Customer> customers;
    public static int index;
    public static int time = 0;
    public static int testNum = 0;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        customers = new ArrayList<Customer>();
        getCustomers(args[0]);
        double score = 0;
        ArrayList<Customer> list = new ArrayList<Customer>();

        //Test 1
        testNum++;
        score += test1_2(32, 25, list);

        //Test 2
        testNum++;
        score += test1_2(7, customers.size() - 10, list);

        //Test 3
        testNum++;
        score += test3(26, 30);

        score = Math.max(0.0, score);

        System.out.println("\nTotal score for Part 2: " + score);
    }

    private static double test1_2(int i, int cap, ArrayList<Customer> list) throws NoSuchAlgorithmException {
        Random gen = new Random(System.currentTimeMillis());
        System.out.println("\n*****Begin Test " + testNum + "*****");
        index = i;
        CBlockChain table = new CBlockChain(cap);
        double score = 0;
        //check operations on empty HT
        System.out.println("\nHT is empty...");
        if (table.size() == 0) {
            printMsg(true, "size");
            score += 1.0;//1.0
        } else
            printMsg(false, "size");

        //insert Patients
        System.out.println("\nAdding some customers...");
        insertCustomers(list, table, (int) (cap / 2));
        System.out.println("list size " + list.size());
        System.out.println("table size " + table.size());
        if (table.size() == list.size()) {
            score += 1.0;//2.0
            printMsg(true, "size");
        } else
            printMsg(false, "size");

        //add the rest of the Patients
        System.out.println("\nAdding the rest of the customers...");
        insertCustomers(list, table, (cap - list.size()));
        if (table.size() == list.size()) {
            score += 1.0;//3.0
            printMsg(true, "size");
        } else
            printMsg(false, "size");

        //check get for a few random Patients in table
        System.out.println("Search for some random Customers...");
        boolean pass = true;
        for (int k = 0; k < 4; k++) {
            int j = gen.nextInt(list.size());
            Customer p1 = list.get(j);
            String s = p1.name();
            Customer p2 = table.get(s);
            if (p1 == p2) {
                score += 0.5;
            } else {
                System.out.println("Customers do not match");
                printExpAct(p1 == null ? "null" : p1.toString(), p2 == null ? "null" : p2.toString());
                pass = false;
            }//5.0
        }
        if (!pass)
            printMsg(false, "get");
        else
            printMsg(true, "get");

        //remove some Patients from table
        System.out.println("Remove some Customers...");
        pass = true;
        for (int k = 0; k < 4; k++) {
            int j = gen.nextInt(list.size());
            Customer p1 = list.remove(j);
            Customer p2 = table.remove(p1.name());
            if (p1 == p2)
                score += 0.5;
            else {
                System.out.println("Customers removed do not match.");
                printExpAct(p1.toString(), p2.toString());
            }
            if (list.size() != table.size()) {
                pass = false;
                System.out.println("Sizes do not match.");
                printExpAct(Integer.toString(list.size()), Integer.toString(table.size()));
            }//7.0
        }
        if (pass)
            printMsg(true, "remove");
        else
            printMsg(false, "remove");

        //search for a Patient not in table
        System.out.println("Search for a Customer not in table...");
        String name = "Customer Name";
        Customer p1 = new Customer(name, 0, 0);
        Customer p2 = table.get(name);
        if (p2 == null) {
            score += 1.0;//8.0
            printMsg(true, "get");
        } else {
            printMsg(false, "get");
            printExpAct("null", p2.toString());
        }
        p2 = table.remove(name);
        if (p2 == null) {
            score += 1.0;//9.0
            printMsg(true, "remove");
        } else {
            printMsg(false, "remove");
            printExpAct("null", p2.toString());
        }

        System.out.println("Total score for test " + testNum + ": " + score);
        return score;
    }

    private static double test3(int start, int size) throws NoSuchAlgorithmException {
        System.out.println("\n*****Begin Test 3*****");
        String oFile = "c2_test3_output_" + size + "_" + start + ".txt";
        CBlockChain ht = new CBlockChain(size);
        index = start;
        for (int i = 0; i < size; i++) {
            ht.put(customers.get(index));
            incIndex();
        }
        String exp = getExp(oFile);
        String act = getAct(ht);
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

    private static String getAct(CBlockChain ht) {
        ArrayList<Customer>[] array = ht.getArray();
        String ret = "";
        for (int i = 0; i < array.length; i++) {
            ret += "{";
            if (array[i] == null)
                ret += "null}";
            else {
                for (Customer p : array[i])
                    ret += "|" + p.toStringForTesting() + "|";
                ret += "}";
            }
        }
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

    private static void insertCustomers(ArrayList<Customer> list, CBlockChain table, int num) throws NoSuchAlgorithmException {
        while (list.size() < num) {
            Customer p = customers.get(index);
            table.put(p);
            insertToList(list, p);
            incIndex();
        }
    }

    private static void insertToList(ArrayList<Customer> list, Customer p) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name().equals(p.name())) {
                list.set(i, p);
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
}

    
	