package com.shipmonk.collection;

/**
 * Internal node class for the doubly-linked list.
 * Package-private to prevent external access while allowing full access within the collection package.
 *
 * @param <T> the type of value stored in the node
 */
final class Node<T> {

    private final T value;
    private Node<T> next;
    private Node<T> prev;

    /**
     * Creates a new node with the specified value.
     *
     * @param value the value to store in this node (must not be null)
     */
    Node(T value) {
        this.value = value;
    }

    /**
     * Returns the value stored in this node.
     *
     * @return the stored value
     */
    T getValue() {
        return value;
    }

    /**
     * Returns the next node in the list.
     *
     * @return the next node, or null if this is the tail
     */
    Node<T> getNext() {
        return next;
    }

    /**
     * Sets the next node in the list.
     *
     * @param next the next node
     */
    void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Returns the previous node in the list.
     *
     * @return the previous node, or null if this is the head
     */
    Node<T> getPrev() {
        return prev;
    }

    /**
     * Sets the previous node in the list.
     *
     * @param prev the previous node
     */
    void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}
