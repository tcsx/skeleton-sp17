import com.sun.corba.se.impl.orbutil.graph.Node;

public class LinkedListDeque<T> {
    private class Node {
        private T item;
        private Node next;
        private Node prev;

        public Node(Node p, T i, Node n) {
            item = i;
            next = n;
            prev = p;
        }
        public Node() {
            item = null;
            next = this;
            prev = this;
        }
    }

    private Node sentinel;
    private int size;

    /**constructor with no parameter, adding a sentinel node */
    public LinkedListDeque(){
        sentinel = new Node();
        size = 0;
    }

    /**add node it to the front of the queue */
    public void addFirst(T it) {
        Node newNode = new Node(sentinel, it, sentinel.next);
        newNode.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    /** Adds an item to the back of the Deque*/
    public void addLast(T it){
        Node newNode = new Node(sentinel.prev, it, sentinel);
        newNode.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    /** Returns true if deque is empty, false otherwise */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the Deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the Deque from first to last, separated by a space. */
    public void printDeque() {
        for(Node p = sentinel.next; p != sentinel; p = p.next) {
            System.out.print(p.item + " ");
        }
    }

    /** Removes and returns the item at the front of the Deque. If no such item exists, returns null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size--;
        return sentinel.next.item;
    }

    /**  Removes and returns the item at the back of the Deque. If no such item exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node last = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return last.item;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. */
    public T get(int index) {
        if (size == 0 || index < 0 || index >= size) {
            return null;
        }
        Node p = sentinel;
        for(int i = 0; i <= index; i++, p = p.next);
        return p.item;
    }

    /** Gets the node at the given index via recursion, where 0 is the front, 1 is the next node, and so forth. If no such node exists, returns null. */
    private Node getNode(int index) {
        if (index == 0) {
            return sentinel.next;
        }
        return getNode(index - 1).next;
    }
    /** Gets the item at the given index via recursion, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. */
    public T getRecursive(int index){
        if (size == 0 || index < 0 || index >= size) {
            return null;
        }
        return getNode(index).item;
    }
}