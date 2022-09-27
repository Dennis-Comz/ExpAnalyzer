package objects;

import java.util.ArrayList;

import objects.BinaryTree.Tree_Node;

public class LinkedListTrees extends ArrayList<BinaryTree>{
    public LinkedListTrees(){super();}

    public void addTree(String name, Tree_Node root){
        this.add(new BinaryTree(name, root));
    }
    
}
