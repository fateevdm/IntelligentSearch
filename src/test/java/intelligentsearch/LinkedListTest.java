package intelligentsearch;

/*************************************************************************
 *  Compilation:  javac LinkedListTest.java
 *  Execution:    java LinkedListTest < input.txt
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt  
 *
 *  A generic queue, implemented using a linked list.
 *
 *  % java LinkedListTest < tobe.txt
 *  to be or not to be (2 left on queue)
 *
 *************************************************************************/

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>LinkedListTest</tt> class represents a first-in-first-out (FIFO)
 * queue of generic items.
 * It supports the usual <em>enqueue</em> and <em>dequeue</em>
 * operations, along with methods for peeking at the top item,
 * testing if the queue is empty, and iterating through
 * the items in FIFO order.
 * <p/>
 * All queue operations except iteration are constant time.
 * <p/>
 * For additional documentation, see <a href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class LinkedListTest<Item> implements Iterable<Item> {
    private int N;         // number of elements on queue
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue

    // helper linked list class
    private class Node<Item> {
        private Item item;
        private Node next;

        @Override
        public String toString() {
            return "Node{" +
                    "item=" + item +
                    ", next=" + next +
                    '}';
        }
    }

    /**
     * Create an empty queue.
     */
    public LinkedListTest() {
        first = null;
        last = null;
        N = 0;
        assert check();
    }

    /**
     * Is the queue empty?
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Return the number of items in the queue.
     */
    public int size() {
        return N;
    }

    /**
     * Return the item least recently added to the queue.
     *
     * @throws java.util.NoSuchElementException
     *          if queue is empty.
     */
    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("LinkedListTest underflow");
        return first.item;
    }

    /**
     * Add the item to the queue.
     */
    public void enqueue(Item item) {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        N++;
        assert check();
    }

    /**
     * Remove and return the item on the queue least recently added.
     *
     * @throws java.util.NoSuchElementException
     *          if queue is empty.
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("LinkedListTest underflow");
        Item item = first.item;
        first = first.next;
        N--;
        if (isEmpty()) last = null;   // to avoid loitering
        assert check();
        return item;
    }

    /**
     * Return string representation.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    // check internal invariants
    private boolean check() {
        if (N == 0) {
            if (first != null) return false;
            if (last != null) return false;
        } else if (N == 1) {
            if (first == null || last == null) return false;
            if (first != last) return false;
            if (first.next != null) return false;
        } else {
            if (first == last) return false;
            if (first.next == null) return false;
            if (last.next != null) return false;

            // check internal consistency of instance variable N
            int numberOfNodes = 0;
            for (Node x = first; x != null; x = x.next) {
                numberOfNodes++;
            }
            if (numberOfNodes != N) return false;

            // check internal consistency of instance variable last
            Node lastNode = first;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            if (last != lastNode) return false;
        }

        return true;
    }


    /**
     * Return an iterator that iterates over the items on the queue in FIFO order.
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public void printList(Node<String> first) {
        Node<String> cur = first;
        System.out.print("first:" + first.item);

        while (cur.next != null) {
            System.out.print(" ->next:" + cur.next.item);
            cur = cur.next;
        }
        System.out.println();
    }

    public void printNextNode(String node) {
        System.out.print("->next:" + node);
    }

    public Node reverse(Node<String> node) {
        Node<String> first = node;
        Node<String> reverse = null;
        printList(first);
        while (first != null) {
            Node<String> second = first.next;
            first.next = reverse;
            reverse = first;
            first = second;
        }
        printList(reverse);
        return reverse;
    }

    /**
     * A test client.
     */
    public static void main(String[] args) {
        LinkedListTest<String> q = new LinkedListTest<>();
    }

    @Test
    public void listTest() {
        Node<String> first = new Node<>();
        first.item = "1";
        Node<String> second = new Node<>();
        second.item = "2";
        Node<String> third = new Node<>();
        third.item = "3";
        first.next = second;
        second.next = third;
        Node<String> forth = new Node<>();
        forth.item = "4";
        third.next = forth;
        Node<String> fifth = new Node<>();
        fifth.item = "5";
        forth.next = fifth;
        reverse(first);
    }
}