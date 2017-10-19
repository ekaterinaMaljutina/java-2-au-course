package ru.spbau.mit;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListImpl<T extends Comparable<T>> implements ru.spbau.mit.LockFreeList<T> {

    private final Node tail = new Node(null, new AtomicMarkableReference<>(null, false));
    private final Node head = new Node(null, new AtomicMarkableReference<>(tail, false));

    @Override
    public void append(T value) {
        Node insertNode = new Node(value, new AtomicMarkableReference<>(null, false));
        while (true) {
            Pair position = findAfterTailNode();
            if (position.rightNode != tail) {
                // false
                break;
            }
            insertNode.next.set(position.rightNode, false);
            if (position.leftNode.next.compareAndSet(position.rightNode, insertNode, false, false)) {
                //true
                break;
            }
        }
    }

    @Override
    public boolean remove(final T value) {
        Pair position;
        Node right;

        while (true) {
            position = findPositionNode(value);
            if (position.rightNode == tail || !position.rightNode.value.equals(value)) {
                return false;
            }
            right = position.rightNode.next.getReference();
            if (position.rightNode.next.attemptMark(right, true)) {
                break;
            }
        }

        if (position.leftNode.next.compareAndSet(position.rightNode, right, false, false)) {
            findPositionNode(position.rightNode.value);
        }
        return true;
    }

    @Override
    public boolean contains(final T value) {
        Pair position = findPositionNode(value);
        return position.rightNode != tail && position.rightNode.value.equals(value);
    }

    @Override
    public boolean isEmpty() {
        while (head.next.getReference() != tail) {
            Node next = head.next.getReference();
            if (!head.next.isMarked()) {
                return false;
            }
            head.next.compareAndSet(next, head.next.getReference(), false, false);
        }
        return true;
    }

    private Pair findAfterTailNode() {
        Node afterNode = head;
        while (afterNode.next.getReference() != tail) {
            Node next = afterNode.next.getReference();
            if (afterNode.next.isMarked()) {
                afterNode.next.compareAndSet(next, afterNode.next.getReference(), false, false);
                continue;
            }
            afterNode = afterNode.next.getReference();
        }
        return new Pair(afterNode, tail);
    }

    private Pair findPositionNode(final T value) {
        Node leftNode = head;
        Node rightNode;

        while (true) {
            Node prev = head;
            Node next = null;

            /* 1: Find left_node and right_node */
            while (prev != tail &&
                    (prev.next.isMarked() || prev.value == null || !prev.value.equals(value))) {
                if (!prev.next.isMarked()) {
                    leftNode = prev;
                    next = prev.next.getReference();
                }
                prev = prev.next.getReference();
            }
            rightNode = prev;

            /* 2: Check nodes are adjacent */
            if (leftNode == rightNode) {
                if (rightNode != tail && rightNode.next.isMarked()) {
                    continue;
                }
                return new Pair(leftNode, rightNode);
            }
            /* 3: Remove one or more marked nodes */
            if (leftNode.next.compareAndSet(next, rightNode, false, false)) {
                if (rightNode != tail && rightNode.next.isMarked()) {
                    continue;
                }
                return new Pair(leftNode, rightNode);
            }
        }
    }

    private final class Node {
        private final T value;
        private final AtomicMarkableReference<Node> next;

        Node(final T value, final AtomicMarkableReference<Node> next) {
            this.value = value;
            this.next = next;
        }
    }

    private final class Pair {
        private final Node leftNode;
        private final Node rightNode;

        Pair(final Node leftNode, final Node rightNode) {
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }
    }
}
