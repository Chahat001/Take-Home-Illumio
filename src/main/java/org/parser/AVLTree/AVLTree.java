package org.parser.AVLTree;

import java.util.Iterator;

public class AVLTree<T extends Comparable<T>> implements Iterable{
    private AVLNode root;

    public AVLTree() {
        root = null;
    }


    /**
     *
     * @param avlNode
     * @return
     */
    private int height(AVLNode avlNode) {
        return avlNode == null ? -1 : avlNode.height;
    }

    /**
     *
     * @param lHeight
     * @param rHeight
     * @return
     */
    private int max(int lHeight, int rHeight) {
        return lHeight > rHeight ? lHeight : rHeight;
    }

    /**
     *
     * @param key
     */
    public void insert(T key) {
        root = insert(key, root);
    }


    /**
     *
     * @param key
     * @param avlNode
     * @return
     */
    private AVLNode<T> insert(T key, AVLNode<T> avlNode) {
        if (avlNode == null){
            avlNode = new AVLNode<T>(key);
        }
        else if (key.compareTo(avlNode.val) < 0) {
            avlNode.left = insert(key,  avlNode.left);
            if (height(avlNode.left) - height(avlNode.right) == 2)
                if (key.compareTo(avlNode.left.val) < 0)
                    avlNode = leftRotation(avlNode);
                else
                    avlNode = leftRightRotation(avlNode);
        } else if (key.compareTo(avlNode.val) > 0){
            avlNode.right = insert(key,avlNode.right);
            if (height(avlNode.right) - height(avlNode.left) == 2)
                if (key.compareTo(avlNode.right.val) > 0)
                    avlNode = rightRotation(avlNode);
                else
                    avlNode = rightLeftRotation(avlNode);
        } else
            ; // Duplicate; do nothing
        avlNode.height = max(height(avlNode.left), height(avlNode.right)) + 1;
        return avlNode;
    }

    /**
     *
     * @param avlNode
     * @return
     */
    private AVLNode<T> leftRotation(AVLNode avlNode) {
        AVLNode<T> k1 = avlNode.left;
        avlNode.left = k1.right;
        k1.right = avlNode;
        avlNode.height = max(height(avlNode.left), height(avlNode.right)) + 1;
        k1.height = max(height(k1.left), avlNode.height) + 1;
        return k1;
    }


    /**
     *
     * @param avlNode
     * @return
     */
    private AVLNode<T> rightRotation(AVLNode<T> avlNode) {
        AVLNode node = avlNode.right;
        avlNode.right = node.left;
        node.left = avlNode;
        avlNode.height = max(height(avlNode.left), height(avlNode.right)) + 1;
        node.height = max(height(node.right), avlNode.height) + 1;
        return node;
    }
    /**
     * left-right rotation
     * @param avlNode
     * @return
     */
    private AVLNode<T> leftRightRotation(AVLNode<T> avlNode) {
        avlNode.left = rightRotation(avlNode.left);
        return leftRotation(avlNode);
    }

    /**
     * right-left rotation
     * @param avlNode
     * @return
     */
    private AVLNode<T> rightLeftRotation(AVLNode<T> avlNode) {
        avlNode.right = leftRotation(avlNode.right);
        return rightRotation(avlNode);
    }

    /**
     *
     * @return
     */
    public int countNodes() {
        return countNodes(root);
    }

    /**
     *
     * @param avlNode
     * @return
     */
    private int countNodes(AVLNode<T> avlNode) {
        if (avlNode == null)
            return 0;
        else {
            int l = 1;
            l += countNodes(avlNode.left);
            l += countNodes(avlNode.right);
            return l;
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public AVLNode<T> search(T key) {
        return search(root, key);
    }

    /**
     *
     * @param avlNode
     * @param key
     * @return
     */
    private AVLNode<T> search(AVLNode<T> avlNode, T key) {
        AVLNode<T> found = null;
        if(avlNode == null){
            return null;
        }
        T rval = avlNode.val;
        if (key.compareTo(rval) < 0){
            return search(avlNode.left, key);
        }
        else if (key.compareTo(rval) > 0){
            return search(avlNode.right, key);
        }
        else{
            return avlNode;
        }

    }

    /**
     *
     */
    public void inorder() {
        inorder(root);
    }

    /**
     *
     * @param avlNode
     */
    private void inorder(AVLNode<T> avlNode) {
        if (avlNode != null) {
            inorder(avlNode.left);
            System.out.print(avlNode.val + " ");
            inorder(avlNode.right);
        }
    }

    /**
     *
     * @return
     */


    @Override
    public Iterator iterator() {
        return new InOrderTreeIterator(this.root);
    }
}
