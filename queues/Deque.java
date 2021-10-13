/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 12, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // check if empty and throw NoSuchElementException
    private void checkEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    // use for corner case when removing and size is 1
    private Item cleanUp() {
        Item i = head.getItem();
        head.setNext(null);
        tail.setNext(null);
        head.setPrev(null);
        tail.setPrev(null);
        head = null;
        tail = null;
        size--;
        return i;
    }

    // represent a node in the list
    private class Node {
        private Item item;
        private Node next;
        private Node prev;


        public Node(Item item) {
            checkNull(item);
            this.setItem(item);
            setNext(null);
            setPrev(null);
        }

        private void checkNull(Item i) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.getItem();
            current = current.getNext();
            return item;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        size++;
        // check if list is empty
        if (isEmpty()) {
            // head is now the new node
            head = new Node(item);
            tail = head;
            return;
        }
        // set current head to temp
        Node oldFirst = head;
        head = new Node(item);
        head.setNext(oldFirst);
        oldFirst.setPrev(head);
    }

    // add the item to the back
    public void addLast(Item item) {
        size++;
        // check if list is empty
        if (isEmpty()) {
            // head is now the new node
            head = new Node(item);
            tail = head;
            return;
        }
        // set current tail to temp
        Node oldLast = tail;
        tail = new Node(item);
        oldLast.setNext(tail);
        tail.setPrev(oldLast);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmpty();
        // corner case
        if (size() == 1) {
            return cleanUp();
        }
        size--;
        Node oldFirst = head;
        head = oldFirst.getNext();
        head.setPrev(null);
        return oldFirst.getItem();
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkEmpty();
        // corner case
        if (size() == 1) {
            return cleanUp();
        }
        size--;
        Node oldLast = tail;
        tail = oldLast.getPrev();
        tail.setNext(null);
        return oldLast.getItem();
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<Integer> test = new Deque<>();
        // check if empty
        System.out.printf("Is the deque empty? %b\n", test.isEmpty());

        // choose random number between 1 and 99
        int n = StdRandom.uniform(1, 100);

        // add half of the numbers (random between 0-999) to the front
        for (int i = 0; i < n / 2; i++) {
            test.addFirst(StdRandom.uniform(1000));
        }

        // check if empty
        System.out.printf("Is the deque empty? %b\n", test.isEmpty());

        // add half of the numbers (random between 0-999) to the back
        for (int i = 0; i < n / 2; i++) {
            test.addLast(StdRandom.uniform(1000));
        }

        // print size of the deque
        System.out.printf("The deque is %d elements long.\n", test.size());

        // choose another random int from 1-n
        n = StdRandom.uniform(1, n);
        System.out.printf("Removing %d elements.\n", n);

        // remove half from front and half from back
        for (int i = 0; i < (n / 2) + 1; i++) {
            System.out.printf("Removing element: %d from front\n", test.removeFirst());
        }
        for (int i = 0; i < n / 2; i++) {
            System.out.printf("Removing element: %d from back\n", test.removeLast());
        }

        // iterate and print list contents
        System.out.print("List contents after operations: \n");
        for (Integer integer : test) System.out.printf("%d\n", integer);

    }
}
