package com.shipmonk.collection;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A sorted linked list that maintains elements in ascending natural order.
 * <p>
 * This implementation supports only {@link String} or {@link Integer} values,
 * enforced through factory methods. Elements are automatically sorted upon insertion.
 * </p>
 *
 * <h2>Type Safety</h2>
 * <p>
 * The list can only be instantiated through factory methods:
 * <ul>
 *   <li>{@link #ofStrings()} - creates a list for String values</li>
 *   <li>{@link #ofIntegers()} - creates a list for Integer values</li>
 * </ul>
 * This ensures compile-time type safety and prevents mixing different types.
 * </p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Automatic sorting - elements are always maintained in sorted order</li>
 *   <li>Duplicates allowed - same value can appear multiple times</li>
 *   <li>Null safety - null values are not permitted</li>
 *   <li>Fail-fast iteration - detects concurrent modifications</li>
 *   <li>Stream support - full Java Stream API integration</li>
 * </ul>
 *
 * <h2>Complexity</h2>
 * <ul>
 *   <li>add(T): O(n)</li>
 *   <li>remove(T): O(n)</li>
 *   <li>contains(T): O(n)</li>
 *   <li>get(int): O(n)</li>
 *   <li>first()/last(): O(1)</li>
 *   <li>size()/isEmpty(): O(1)</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * SortedLinkedList<Integer> numbers = SortedLinkedList.ofIntegers();
 * numbers.add(5);
 * numbers.add(1);
 * numbers.add(3);
 * // List is now: [1, 3, 5]
 *
 * SortedLinkedList<String> words = SortedLinkedList.ofStrings();
 * words.add("banana");
 * words.add("apple");
 * // List is now: ["apple", "banana"]
 * }</pre>
 *
 * @param <T> the type of elements in this list (String or Integer)
 * @author ShipMonk Task Implementation
 * @see #ofStrings()
 * @see #ofIntegers()
 */
public final class SortedLinkedList<T extends Comparable<T>> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;

    /**
     * Private constructor to enforce factory method usage.
     * This ensures only String or Integer types can be used.
     */
    private SortedLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * Creates a new empty sorted linked list for String values.
     *
     * @return a new empty SortedLinkedList for Strings
     */
    public static SortedLinkedList<String> ofStrings() {
        return new SortedLinkedList<>();
    }

    /**
     * Creates a new empty sorted linked list for Integer values.
     *
     * @return a new empty SortedLinkedList for Integers
     */
    public static SortedLinkedList<Integer> ofIntegers() {
        return new SortedLinkedList<>();
    }

    /**
     * Adds a value to the list while maintaining sorted order.
     * <p>
     * The value is inserted at the correct position to maintain ascending order.
     * Duplicates are allowed and will be placed after existing equal elements.
     * </p>
     *
     * @param value the value to add (must not be null)
     * @throws NullPointerException if the value is null
     */
    public void add(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        Node<T> newNode = new Node<>(value);
        modCount++;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else if (value.compareTo(head.getValue()) < 0) {
            // Insert at head
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        } else if (value.compareTo(tail.getValue()) >= 0) {
            // Insert at tail
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        } else {
            // Insert in the middle - find the correct position
            Node<T> current = head;
            while (current != null && current.getValue().compareTo(value) <= 0) {
                current = current.getNext();
            }
            // Insert before current
            Node<T> prev = current.getPrev();
            prev.setNext(newNode);
            newNode.setPrev(prev);
            newNode.setNext(current);
            current.setPrev(newNode);
        }

        size++;
    }

    /**
     * Removes the first occurrence of the specified value from the list.
     *
     * @param value the value to remove
     * @return true if the value was found and removed, false otherwise
     * @throws NullPointerException if the value is null
     */
    public boolean remove(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        Node<T> current = head;
        while (current != null) {
            int cmp = current.getValue().compareTo(value);
            if (cmp == 0) {
                removeNode(current);
                return true;
            } else if (cmp > 0) {
                // Since list is sorted, no point continuing
                return false;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Removes all occurrences of the specified value from the list.
     *
     * @param value the value to remove
     * @return the number of elements removed
     * @throws NullPointerException if the value is null
     */
    public int removeAll(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        int removedCount = 0;
        Node<T> current = head;

        while (current != null) {
            int cmp = current.getValue().compareTo(value);
            if (cmp == 0) {
                Node<T> next = current.getNext();
                removeNode(current);
                removedCount++;
                current = next;
            } else if (cmp > 0) {
                // Since list is sorted, no point continuing
                break;
            } else {
                current = current.getNext();
            }
        }

        return removedCount;
    }

    /**
     * Internal helper method to remove a node from the list.
     *
     * @param node the node to remove
     */
    private void removeNode(Node<T> node) {
        modCount++;

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }

        size--;
    }

    /**
     * Checks if the list contains the specified value.
     *
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     * @throws NullPointerException if the value is null
     */
    public boolean contains(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        Node<T> current = head;
        while (current != null) {
            int cmp = current.getValue().compareTo(value);
            if (cmp == 0) {
                return true;
            } else if (cmp > 0) {
                // Since list is sorted, value cannot exist beyond this point
                return false;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element to return (0-based)
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T get(int index) {
        checkIndex(index);

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
    }

    /**
     * Returns the first (smallest) element in the list.
     *
     * @return an Optional containing the first element, or empty if the list is empty
     */
    public Optional<T> first() {
        return head == null ? Optional.empty() : Optional.of(head.getValue());
    }

    /**
     * Returns the last (largest) element in the list.
     *
     * @return an Optional containing the last element, or empty if the list is empty
     */
    public Optional<T> last() {
        return tail == null ? Optional.empty() : Optional.of(tail.getValue());
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
        modCount++;
    }

    /**
     * Counts the number of occurrences of the specified value.
     *
     * @param value the value to count
     * @return the number of occurrences
     * @throws NullPointerException if the value is null
     */
    public int count(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        int occurrences = 0;
        Node<T> current = head;

        while (current != null) {
            int cmp = current.getValue().compareTo(value);
            if (cmp == 0) {
                occurrences++;
            } else if (cmp > 0) {
                break;
            }
            current = current.getNext();
        }

        return occurrences;
    }

    /**
     * Returns a copy of the list contents as an ArrayList.
     * <p>
     * The returned list is a new instance and modifications to it
     * do not affect the original SortedLinkedList.
     * </p>
     *
     * @return a new ArrayList containing all elements in sorted order
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>(size);
        Node<T> current = head;
        while (current != null) {
            result.add(current.getValue());
            current = current.getNext();
        }
        return result;
    }

    /**
     * Returns a sequential Stream with this list as its source.
     *
     * @return a Stream over the elements in this list
     */
    public Stream<T> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(iterator(), size, 
                        Spliterator.ORDERED | Spliterator.SORTED | Spliterator.SIZED),
                false
        );
    }

    /**
     * Returns a fail-fast iterator over the elements in this list.
     * <p>
     * The iterator will throw {@link ConcurrentModificationException}
     * if the list is modified after the iterator is created.
     * </p>
     *
     * @return an iterator over the elements in sorted order
     */
    @Override
    public Iterator<T> iterator() {
        return new SortedLinkedListIterator();
    }

    /**
     * Returns the index of the first occurrence of the specified value.
     *
     * @param value the value to search for
     * @return the index of the first occurrence, or -1 if not found
     * @throws NullPointerException if the value is null
     */
    public int indexOf(T value) {
        Objects.requireNonNull(value, "Value cannot be null");

        Node<T> current = head;
        int index = 0;

        while (current != null) {
            int cmp = current.getValue().compareTo(value);
            if (cmp == 0) {
                return index;
            } else if (cmp > 0) {
                return -1;
            }
            current = current.getNext();
            index++;
        }

        return -1;
    }

    /**
     * Validates that the index is within bounds.
     *
     * @param index the index to validate
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    String.format("Index %d out of bounds for length %d", index, size)
            );
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getValue());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SortedLinkedList<?> other)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }

        Iterator<T> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();

        while (thisIterator.hasNext()) {
            if (!thisIterator.next().equals(otherIterator.next())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (T element : this) {
            result = 31 * result + element.hashCode();
        }
        return result;
    }

    /**
     * Fail-fast iterator implementation for SortedLinkedList.
     */
    private class SortedLinkedListIterator implements Iterator<T> {

        private Node<T> current;
        private Node<T> lastReturned;
        private int expectedModCount;

        SortedLinkedListIterator() {
            this.current = head;
            this.lastReturned = null;
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the list");
            }
            lastReturned = current;
            current = current.getNext();
            return lastReturned.getValue();
        }

        @Override
        public void remove() {
            checkForComodification();
            if (lastReturned == null) {
                throw new IllegalStateException("next() must be called before remove()");
            }
            removeNode(lastReturned);
            lastReturned = null;
            // Sync expectedModCount since this is an iterator-sanctioned modification
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException(
                        "List was modified during iteration"
                );
            }
        }
    }
}
