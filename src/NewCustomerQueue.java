import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class NewCustomerQueue {
    private Customer[] array;
    private CBlockChain table;

    //constructor: set variables
    //capacity = initial capacity of array
    public NewCustomerQueue(int capacity) {
        this.array = new Customer[capacity];
        this.table = new CBlockChain(capacity);
    }

    //insert Customer c into queue
    //return the final index at which the customer is stored
    //return -1 if the customer could not be inserted
    public int insert(Customer c) throws NoSuchAlgorithmException {
        if (isEmpty()) {
            array[0] = c;
            return 0;
        }
        if (size() == array.length)
            return -1;

        boolean isDone = false;
        int index = 0;
        while (!isDone) {
            if (isLeftEmpty(index)) {
                index = insertLeft(index, c);
                index = floatUp(index);
                isDone = true;
            } else if (isRightEmpty(index)) {
                index = insertRight(index, c);
                index = floatUp(index);
                isDone = true;

            } else {
                index++;
            }
        }
        array[index].setPosInQueue(index);
        table.put(c);
        return index;
    }

    public int floatUp(int i) {
        int parentIndex = (i-1)/2;
        while(i > 0 && array[i].compareTo(array[parentIndex]) > 0) {
            swap(i, parentIndex);
            i = parentIndex;
            parentIndex = (i-1)/2;
        }
        return i;
    }

    public boolean isLeftEmpty(int i) {
        if (2 * i + 1 >= array.length)
            return true;
        return array[2 * i + 1] == null;
    }

    public int insertLeft(int i, Customer c) {
        array[2 * i + 1] = c;
        return 2 * i + 1;
    }

    public boolean isRightEmpty(int i) {
        if (2 * i + 2 >= array.length)
            return true;
        return array[2 * i + 2] == null;
    }

    public int insertRight(int i, Customer c) {
        array[2 * i + 2] = c;
        return 2 * i + 2;
    }

    public boolean isLeaf(int i) {
        return isRightEmpty(i) && isLeftEmpty(i);
    }


    public void swap(int i, int j) {
//        System.out.println("swapping " + array[i].name() + " and " + array[j].name());
        Customer temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        array[i].setPosInQueue(i);
        array[j].setPosInQueue(j);
    }

    //remove and return the customer with the highest investment value
    //if there are multiple customers with the same investment value,
    //return the one who arrived first
    public Customer delMax() {
        Customer c;
        if (isEmpty())
            return null;
        if (isLeaf(0)) {
            c = array[0];
            array[0] = null;
            c.setPosInQueue(-1);
            return c;
        }
        int i = swimDown(0);
        c = array[i];
        array[i] = null;
        c.setPosInQueue(-1);
        return c;

    }

    public int swimDown (int i) {
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        if (isLeaf(i)) {
            return i;
        }
        if (isRightEmpty(i)) {
            swap(i, left);
            return swimDown(left);
        }
        if (isLeftEmpty(i)) {
            swap(i, right);
            return swimDown(right);
        }

        if (array[right].compareTo(array[left]) > 0) {
            swap(i, right);
            i = right;
        } else {
            swap(i, left);
            i = left;
        }
        return swimDown(i);
    }


    //return but do not remove the first customer in the queue
    public Customer getMax() {
        if (isEmpty())
            return null;
        int max = 0;
        int index = 0;
        for (int i = 0; i < size(); i++) {
            if (array[i].investment() > max) {
                max = array[i].investment();
                index = i;
            }
        }
        return array[index];
    }

    //return the number of customers currently in the queue
    public int size() {
        int size = 0;
        for (Customer c : array) {
            if (c != null)
                size++;
        }
        return size;
    }

    //return true if the queue is empty; false else
    public boolean isEmpty() {
        if (array == null)
            return true;
        for (Customer c : array) {
            if (c != null)
                return false;
        }
        return true;
    }

    //used for testing underlying data structure
    public Customer[] getArray() {
        return array;
    }

    //remove and return the Customer with
    //name s from the queue
    //return null if the Customer isn't in the queue
    public Customer remove(String s) throws NoSuchAlgorithmException {
        Customer customer = table.get(s);
        if (customer == null)
            return null;
        int index = customer.posInQueue();
        if (index == -1)
            return null;
        index = swimDown(index);
        array[index] = null;
        table.remove(s);
        customer.setPosInQueue(-1);
        return customer;
    }

//    public int search(Customer c, int index) {
//        if (array[index] == null) {
//            return -1;
//        }
//        if (array[index].name().equals(c.name()))
//            return index;
//        int left = index * 2 + 1;
//        int right = index * 2 + 2;
//        if (array[index].compareTo(c) > 0) {
//            return search(c, left);
//        } else {
//            return search(c, right);
//        }
//    }

    //update the emergency level of the Customer
    //with name s to investment
    public void update(String s, int investment) throws NoSuchAlgorithmException {
        Customer c = table.get(s);
        if (c == null)
            return;
        int index = c.posInQueue();
        int currentInvestment = array[index].investment();
        array[index].setInvestment(investment);
        if (investment > currentInvestment) {
            floatUp(index);
        } else {
            swimDown(index);
        }


    }
}
