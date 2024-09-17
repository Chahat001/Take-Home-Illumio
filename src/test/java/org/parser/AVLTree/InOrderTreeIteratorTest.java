package org.parser.AVLTree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.parser.AVLTree.AVLTree;

import java.util.Iterator;

public class InOrderTreeIteratorTest {
    @Test
    public void testIteratorOrdering(){
        AVLTree<String> tree = new AVLTree<>();
        for(int i = 100; i >= 1; i--){
            if(i == 100){
                tree.insert(i+"");
            }
            else if(i < 100 && i >= 10){
                tree.insert("0" + i);
            }
            else {
                tree.insert("00" + i);
            }
        }

        // test in order traversal
        Iterator itr = tree.iterator();
        for(int i  = 1; i <= 100; i++){
            AVLNode<String> node = (AVLNode<String>) itr.next();
            String returned = node.val;
            if(i == 100){
                Assertions.assertEquals(i+"", returned);
            }
            else if(i < 100 && i >= 10){
                Assertions.assertEquals("0"+i, returned);
            }
            else {
                Assertions.assertEquals("00"+i, returned);
            }
        }
    }
}
