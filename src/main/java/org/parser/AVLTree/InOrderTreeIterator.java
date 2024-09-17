package org.parser.AVLTree;

import java.util.Stack;
import java.util.Iterator;
import java.util.NoSuchElementException;

class InOrderTreeIterator implements Iterator{
    private Stack<AVLNode> stack = new Stack<>();

    public InOrderTreeIterator(AVLNode root) {
        // Initialize the stack with the leftmost path from the root
        pushLeftPath(root);
    }

    // Push all the left children to the stack
    private void pushLeftPath(AVLNode node) {
        AVLNode nodeAVL = (AVLNode) node;
        while (nodeAVL != null) {
            stack.push(nodeAVL);
            nodeAVL = nodeAVL.left;
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the tree");
        }

        // Pop the top element from the stack
       AVLNode currentNode = stack.pop();
        AVLNode val = currentNode;

        // If there is a right child, push all its left children
        if (currentNode.right != null) {
            pushLeftPath(currentNode.right);
        }

        return val;
    }
}
