package com.quiz.datastructures;

/**
 * Custom LinkedList implementation for managing quiz data and user responses
 * Priority 2 Data Structure - Used for storing quiz questions and maintaining user responses
 */
public class QuizLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;
        
        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    
    public QuizLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    /**
     * Add element to the end of the list
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    
    /**
     * Add element at specific index
     */
    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == size) {
            add(data);
            return;
        }
        
        Node<T> newNode = new Node<>(data);
        
        if (index == 0) {
            newNode.next = head;
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            Node<T> current = getNode(index);
            Node<T> prevNode = current.prev;
            
            newNode.next = current;
            newNode.prev = prevNode;
            prevNode.next = newNode;
            current.prev = newNode;
        }
        size++;
    }
    
    /**
     * Get element at specific index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).data;
    }
    
    /**
     * Remove element at specific index
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node<T> nodeToRemove = getNode(index);
        T data = nodeToRemove.data;
        
        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            head = nodeToRemove.next;
        }
        
        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            tail = nodeToRemove.prev;
        }
        
        size--;
        return data;
    }
    
    /**
     * Check if list contains element
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data != null && current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    /**
     * Get size of the list
     */
    public int size() {
        return size;
    }
    
    /**
     * Check if list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Clear all elements
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }
    
    /**
     * Get node at specific index (internal helper method)
     */
    private Node<T> getNode(int index) {
        Node<T> current;
        
        // Optimize by starting from head or tail based on index
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        
        return current;
    }
    
    /**
     * Convert to array for easy iteration
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = head;
        int index = 0;
        
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        
        return (T[]) array;
    }
    
    /**
     * Display list contents (for debugging)
     */
    public void display() {
        Node<T> current = head;
        System.out.print("LinkedList: ");
        while (current != null) {
            System.out.print(current.data + " <-> ");
            current = current.next;
        }
        System.out.println("null");
    }
}