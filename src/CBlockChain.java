import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.util.Arrays;

public class CBlockChain {
    private ArrayList[] table;

    //set the table size to the first
    //prime number p >= capacity
    public CBlockChain(int capacity) {
        capacity = getNextPrime(capacity);
        this.table = new ArrayList[capacity];
    }

    //return the Customer with the given name
    //or null if the Customer is not in the table
    public Customer get(String name) throws NoSuchAlgorithmException {
        int hashKey = generateHashKey(name);
        if (table[hashKey] == null)
            return null;
        for (int i = 0; i < table[hashKey].size(); i++) {
            Customer c = (Customer) table[hashKey].get(i);
            if (c.name().equals(name))
                return c;
        }
        return null;
    }

    //put Customer c into the table
    public void put(Customer c) throws NoSuchAlgorithmException {
        int hashKey = generateHashKey(c.name());
        if (table[hashKey] != null) {
            if (table[hashKey].contains(c))
                return;
            table[hashKey].add(c);
            return;
        }

        table[hashKey] = new ArrayList();
        table[hashKey].add(c);
    }

    public int generateHashKey(String name) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] shaHash = messageDigest.digest(name.getBytes(StandardCharsets.UTF_8));

        int hashKey = toHexString(shaHash).hashCode() % table.length;

        if (hashKey < 0) {
            hashKey += table.length;
        }
        return hashKey;
    }

    public String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64)
            hexString.insert(0, '0');
        return hexString.toString();
    }

    //remove and return the Customer with the given name
    //from the table
    //return null if Customer doesn't exist
    public Customer remove(String name) throws NoSuchAlgorithmException {
        int hashKey = generateHashKey(name);
        if (table[hashKey] == null) {
            return null;
        }
        for (int i = 0; i < table[hashKey].size(); i++) {
            Customer c = (Customer) table[hashKey].get(i);
            if (c.name().equals(name)) {
                table[hashKey].remove(i);
                return c;
            }
        }
        return null;

    }

    //return the number of Customers in the table
    public int size() {
        int size = 0;
        for (ArrayList a : table) {
            if (a == null)
                continue;
            if (!a.isEmpty())
                size += a.size();
        }
        return size;
    }

    //returns the underlying structure for testing
    public ArrayList<Customer>[] getArray() {
        return table;
    }

    //get the next prime number p >= num
    private int getNextPrime(int num) {
        if (num == 2 || num == 3)
            return num;
        int rem = num % 6;
        switch (rem) {
            case 0:
            case 4:
                num++;
                break;
            case 2:
                num += 3;
                break;
            case 3:
                num += 2;
                break;
        }
        while (!isPrime(num)) {
            if (num % 6 == 5) {
                num += 2;
            } else {
                num += 4;
            }
        }
        return num;
    }


    //determines if a number > 3 is prime
    private boolean isPrime(int num) {
        if (num % 2 == 0) {
            return false;
        }

        int x = 3;
        for (int i = x; i < num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}


