package org.parser.AVLTree;

import org.junit.jupiter.api.Test;
import org.parser.AVLTree.AVLTree;

public class AVLTreeTest {

    @Test
    public void TestHappyPath(){
        AVLTree<String> tree = new AVLTree<>();
        tree.insert("1");
        tree.insert("2");
        tree.insert("3");

        assert tree.countNodes() == 3;
        assert tree.search("1").val.equals("1");
        assert tree.search("2").val.equals("2");
        assert tree.search("3").val.equals("3");
        assert tree.search("4") == null;
    }
}
