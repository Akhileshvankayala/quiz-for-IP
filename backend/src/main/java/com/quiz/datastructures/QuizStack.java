package com.quiz.datastructures;

/**
 * Custom Stack implementation for managing quiz questions
 * Priority 1 Data Structure - Used for question navigation and undo operations
 */
public class QuizStack<T> {
    private Node<T> top;
    private int size;
    
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public QuizStack() {
        this.top = null;
        this.size = 0;
    }
    
    /**
     * Push an element onto the stack
     */
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    /**
     * Pop an element from the stack
     */
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty - cannot pop");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    /**
     * Peek at the top element without removing it
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }
    
    /**
     * Check if stack is empty
     */
    public boolean isEmpty() {
        return top == null;
    }
    
    /**
     * Get the size of the stack
     */
    public int size() {
        return size;
    }
    
    /**
     * Clear all elements from the stack
     */
    public void clear() {
        top = null;
        size = 0;
    }
    
    /**
     * Display stack contents (for debugging)
     */
    public void display() {
        Node<T> current = top;
        System.out.print("Stack (top to bottom): ");
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}