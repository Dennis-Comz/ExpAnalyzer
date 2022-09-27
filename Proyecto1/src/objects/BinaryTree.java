package objects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import references.SiguienteRef;
public class BinaryTree {
    public static class Tree_Node{
        String data;
        Tree_Node left, right;
        String num_izq, num_der;
        boolean anulable;
        public Tree_Node(String data, Tree_Node left, Tree_Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.num_izq = "";
            this.num_der = "";
            this.anulable = false;
        }
    
    }

    String resultado = "";
    String name = "";
    int contador = 0;
    int numHoja = 1;
    Tree_Node root;
    String[] simbols = new String[]{"|","?",".","*","+"};
    String[] valid_simbols = new String[]{".","*","+"};
    ArrayList<Siguientes> hojas;

    public BinaryTree(String nombre, Tree_Node newroot) {
        contador = 0; numHoja = 1;
        name = nombre;
        resultado = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>"+nombre+"</B>>;\nbgcolor=\"#50FAA3\";\ngraph[nodesep=1];\nnode[shape=none];\n";
        root = addacceptance(newroot);
        hojas = new ArrayList<Siguientes>();        
    }
    
    public void ejecutar_arbol() {
        postOrderTraverseTree(root);
        inorder(root);
        resultado += "}";
        generar_graphviz();
        SiguienteRef.list.addTabla(root, name, hojas);
    }

    public Tree_Node addacceptance(Tree_Node root){
        Tree_Node punto = new Tree_Node(".", root, new Tree_Node("#", null, null));
        return punto;
    }

    public void inorder(Tree_Node root){
		if(root==null) return;
        if (root.left != null) {
            resultado += nodeshape(String.valueOf(root.hashCode())+String.valueOf(contador), String.valueOf(root.anulable), root.num_izq, root.data, root.num_der);
            resultado += nodeshape(String.valueOf(root.left.hashCode())+String.valueOf(contador), String.valueOf(root.left.anulable), root.left.num_izq, root.left.data, root.left.num_der);
            resultado += "N"+root.hashCode()+contador + " -> "+"N"+root.left.hashCode()+contador+";\n";
        }
		inorder(root.left);
        if (root.right != null) {
            resultado += nodeshape(String.valueOf(root.hashCode())+String.valueOf(contador), String.valueOf(root.anulable), root.num_izq, root.data, root.num_der);
            resultado += nodeshape(String.valueOf(root.right.hashCode())+String.valueOf(contador), String.valueOf(root.right.anulable), root.right.num_izq, root.right.data, root.right.num_der);
            resultado += "N"+root.hashCode()+contador + " -> "+"N"+root.right.hashCode()+contador+";\n";    
        }
        inorder(root.right);
    }

    public void preorderTraverseTree(Tree_Node root) {
		if (root != null) {
			System.out.print(root.data);
			preorderTraverseTree(root.left);
			preorderTraverseTree(root.right);
		}

	}

    public void postOrderTraverseTree(Tree_Node root) {
		if (root != null) {
			postOrderTraverseTree(root.left);
			postOrderTraverseTree(root.right);
			siguientes(root);
		}
	}

    public void siguientes(Tree_Node node) {
        if (!Arrays.stream(simbols).anyMatch(node.data::equals)) {
            node.num_der = String.valueOf(numHoja);
            node.num_izq = String.valueOf(numHoja);
            node.anulable = false;
            hojas.add(new Siguientes(String.valueOf(numHoja), node.data));
            numHoja++;
        }else{
            if (!Arrays.stream(valid_simbols).anyMatch(node.data::equals)) {
                if (node.data == "|") {
                    node.num_izq = node.left.num_izq + "," + node.right.num_izq;
                    node.num_der = node.left.num_der + "," + node.right.num_der;
                    if (node.left.anulable == false && node.right.anulable == false) {
                        node.anulable = false;
                    }else{
                        node.anulable = true;
                    }
                }else if(node.data == "?"){
                    node.num_izq = node.left.num_izq;
                    node.num_der = node.left.num_der;
                    node.anulable = true;
                }
            }else{
                if (node.data == ".") {
                    if (node.left.anulable == true) {
                        node.num_izq = node.left.num_izq + "," + node.right.num_izq;       
                    }else{node.num_izq = node.left.num_izq;}
                    if(node.right.anulable == true){
                        node.num_der = node.left.num_der + "," + node.right.num_der;       
                    }else{node.num_der = node.right.num_der;}
                    if (node.right.anulable == true && node.left.anulable == true) {
                        node.anulable = true;
                    }else{node.anulable = false;}
                }else if(node.data == "*"){
                    node.num_izq = node.left.num_izq;
                    node.num_der = node.left.num_der;
                    node.anulable = true;
                }else if(node.data == "+"){
                    node.num_izq = node.left.num_izq;
                    node.num_der = node.left.num_der;
                    node.anulable = false;
                }
                fill_siguientes(node);
            }
        }
    }

    public void fill_siguientes(Tree_Node node) {
        if (node.data == ".") {
            String[] ultimaposc1 = node.left.num_der.split(",");
            List<String> primeraposc2 = Arrays.asList(node.right.num_izq.split(","));
            for (int i = 0; i < ultimaposc1.length; i++) {
                Siguientes ultimo = find_next(ultimaposc1[i]);
                ultimo.setSiguiente(primeraposc2);
            }
        }else if(node.data == "*"){
            String[] ultimapos = node.num_der.split(",");
            List<String> primerapos = Arrays.asList(node.num_izq.split(","));
            for (int i = 0; i < ultimapos.length; i++) {
                Siguientes ultimo = find_next(ultimapos[i]);
                ultimo.setSiguiente(primerapos);
            }
        }else if(node.data == "+"){
            String[] ultimapos = node.num_der.split(",");
            List<String> primerapos = Arrays.asList(node.num_izq.split(","));
            for (int i = 0; i < ultimapos.length; i++) {
                Siguientes ultimo = find_next(ultimapos[i]);
                ultimo.setSiguiente(primerapos);
            }
        }
    }

    public Siguientes find_next(String num_hoja) {
        for (int i = 0; i < hojas.size(); i++) {
            if (hojas.get(i).getNum_hoja().equals(num_hoja)) {
                return hojas.get(i);
            }
        }
        return null;
    }

    public String nodeshape(String code, String anulable, String izquierda, String data, String derecha) {
        String informacion = "";
        informacion += "N"+code+"[fixedsize=true,label=<\n<TABLE bgcolor=\"white\" CELLSPACING=\"2\" CELLPADDING=\"2\" BORDER=\"0\">\n";
        informacion += "<TR>\n<TD BORDER=\"0\"></TD>\n<TD BORDER=\"0\">"+anulable+"</TD>\n<TD BORDER=\"0\"></TD>\n</TR>\n";
        informacion += "<TR>\n<TD BORDER=\"0\">"+izquierda+"</TD>\n<TD BORDER=\"1\">"+data+"</TD>\n<TD BORDER=\"0\">"+derecha+"</TD>\n</TR>\n";
        informacion += "</TABLE>>];";
        return informacion;
    }
    
    public void generar_graphviz() {
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Graphviz\\"+name+".dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Arboles_202010406\\"+name+".png";
        File dotFile = new File(ubicacion_dot);
        try {dotFile.createNewFile();} catch (Exception e) {}
        FileWriter writer;
        try {
            writer = new FileWriter(ubicacion_dot);
            writer.write(resultado);
            writer.close();
        } catch (Exception e) {}
        String comando = "dot -Tpng " + ubicacion_dot + " -o " + ubicacion_img;
        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c"+comando);
        builder.redirectErrorStream(true);
        try {builder.start();} catch (IOException e) {}
    }
}