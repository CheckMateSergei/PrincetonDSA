/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 12, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    // represent a node in the queue
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

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        Node oldTail = tail;
        tail = new Node(item);
        tail.setNext(null);
        // perhaps doubly linking will solve your problems
        tail.setPrev(oldTail);
        // if the list is empty set the head equal to the tail
        if (isEmpty()) {
            head = tail;
        }
        else oldTail.setNext(tail);
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        // check if list is empty
        if (isEmpty()) throw new NoSuchElementException();
        // dequeue random element
        int n = StdRandom.uniform(size);

        // set head to be a temporary node
        Node current = head;

        // if n = 0 we delete the head node
        if (n == 0) {
            if (head.getNext() == null) {
                head = null;
                tail = null;
                size = 0;
                return current.getItem();
            }
            head = head.getNext();
            head.setPrev(null);
            size--;
            return current.getItem();
        }

        // else we traverse to the n'th node then delete
        for (int i = 0; i < n; i++) {
            // moves to next node in the list
            current = current.getNext();
        }

        // if current is tail
        if (current == tail) {
            tail = tail.getPrev();
            tail.setNext(null);
            size--;
            return current.getItem();
        }

        // delete current
        current.getPrev().setNext(current.getNext());
        current.getNext().setPrev(current.getPrev());
        size--;
        return current.getItem();
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // check if list is empty
        if (isEmpty()) throw new NoSuchElementException();
        // random sample
        int n = StdRandom.uniform(size);

        Node current = head;
        // loop n times
        for (int i = 0; i < n; i++) {
            current = current.getNext();
        }
        return current.getItem();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        // randomly remove elements from a copy of the list
        // store randomly chosen elements in a deque O(2n)
        Deque<Item> iteratorDeque = new Deque<>();
        // copy the list
        RandomizedQueue<Item> copy = new RandomizedQueue<>();
        Node current = head;
        while (current != null) {
            copy.enqueue(current.getItem());
            current = current.getNext();
        } // copy takes O(n) memory and linear time to make

        // randomly remove and store in deque
        for (int i = 0; i < size; i++) {
            Item temp = copy.dequeue();
            iteratorDeque.addLast(temp);
        } // then iterate through the deque

        return iteratorDeque.iterator();
    }

    /* public void debug() {
        Node current = head;
        System.out.println("DEBUGGING");
        while (current != null) {
            System.out.printf("Current: %s\n", current.getItem());
            current = current.getNext();
        }
    } */

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<Integer> test = new RandomizedQueue<>();
        // check if empty
        System.out.printf("Is the queue empty? %b\n", test.isEmpty());

        // choose random number between 1 and 99
        int n = StdRandom.uniform(1, 100);

        // enqueue numbers (random between 0-999)
        for (int i = 0; i < n; i++) {
            test.enqueue(StdRandom.uniform(1000));
        }

        // check if empty
        System.out.printf("Is the queue empty? %b\n", test.isEmpty());

        // print size of the queue
        System.out.printf("The queue is %d elements long.\n", test.size());

        // test.debug();

        // choose another random int from 1-n
        n = StdRandom.uniform(1, n + 1);
        System.out.printf("Removing %d elements.\n", n);

        // remove n random elements
        for (int i = 0; i < n; i++) {
            System.out.printf("Removing element: %d\n", test.dequeue());
        }

        // iterate and print list contents
        System.out.print("Permuted queue, after operations: \n");
        for (Integer integer : test) System.out.printf("%d\n", integer);

    }
}
