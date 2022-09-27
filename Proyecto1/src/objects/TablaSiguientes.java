package objects;

import java.util.ArrayList;

import objects.BinaryTree.Tree_Node;

public class TablaSiguientes {
    String name;
    ArrayList<Siguientes> list;
    Tree_Node root;
    public TablaSiguientes(Tree_Node root, String name, ArrayList<Siguientes> list) {
        this.root = root;
        this.name = name;
        this.list = list;
    }
}