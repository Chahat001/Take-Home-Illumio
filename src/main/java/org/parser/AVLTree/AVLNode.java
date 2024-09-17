package org.parser.AVLTree;

public class AVLNode<T> {
    AVLNode<T> left, right;
    public T val;

    public int count;

    int height;


    public AVLNode() {
        left = null;
        right = null;
        val = (T) "";
        height = 0;
    }

    public AVLNode(T val) {
        this.left = null;
        this.right = null;
        this.val = val;
        this.height = 0;
    }
}
