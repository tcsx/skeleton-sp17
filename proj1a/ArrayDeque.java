public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFront;
    private int nextLast;

    /**constructor with no parameter, adding a sentinel node */
    @SuppressWarnings("all")
    public ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
        nextFront = 7;
        nextLast = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    @SuppressWarnings("all")
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for(int i = 0; i < size; i++) {
            a[i] = get(i);
        }
        nextFront = a.length -1;
        nextLast = size;
        items = a;
    }

    /**add node it to the front of the queue */
    public void addFirst(T it) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFront] = it;
        size++;
        nextFront--;
    }

    /** Adds an item to the back of the Deque*/
    public void addLast(T it){
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = it;
        size++;
        nextLast++;
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
        for(int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
    }

    /** Removes and returns the item at the front of the Deque. If no such item exists, returns null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        double rate = (double)size / items.length;
        if (items.length >= 16 && rate <= 0.25) {
            resize(size * 2);
        }
        nextFront = (nextFront + 1) % items.length; 
        size--;
        return items[nextFront];
    }

    /**  Removes and returns the item at the back of the Deque. If no such item exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        double rate = (double)size / items.length;
        if (items.length >= 16 && rate <= 0.25) {
            resize(size * 2);
        }
        int last = nextLast > 0 ? nextLast - 1 : items.length -1;
        size--;
        nextLast = last;
        return items[last];
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. */
    public T get(int index) {
        if (index < 0 || index >= size) {
            System.out.println("index out of range");
            return null;
        }
        int first = (nextFront + 1) % items.length; //index of the first item in ArrayDeque
        return items[(first + index) % items.length];        
    }
} 