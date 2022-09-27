package references;

import objects.LinkedListTrees;

public class Tree {
    public static LinkedListTrees list;
    public static Tree tree;

    public void initialize(){
        try {
            list = new LinkedListTrees();
        } catch (Exception e) {}
    }

    public static Tree getInstance(){
        if (tree == null) {
            list = new LinkedListTrees();
            tree = new Tree();
        }
        return tree;
    }

    public static void generar_metodo() {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) != null) {
                    list.get(i).ejecutar_arbol();                    
                }
            }
        }
    }
    public static void setList(LinkedListTrees list) {
        Tree.list = list;
    }
}
