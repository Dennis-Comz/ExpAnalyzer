package objects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import objects.BinaryTree.Tree_Node;

public class AuxiliarLinkedList extends ArrayList<TablaSiguientes>{
    int contador = 0;
    int contador2 = 0;
    int una_vez = 0;
    ArrayList<Transicion> listTransicion = new ArrayList<Transicion>();
    ArrayList<String> root_sigs = new ArrayList<String>();
    ArrayList<TablaTransiciones> trans = new ArrayList<TablaTransiciones>();

    public AuxiliarLinkedList(){super();}

    public void addInfo(Tree_Node root, String name, ArrayList<Siguientes> lista){
        this.add(new TablaSiguientes(root, name, lista));
    }

    public void generar_afn() {
        for (TablaSiguientes posicion : this) {
            String name = posicion.name;
            ArrayList<String> siguientes_inicial = new ArrayList<String>();
            siguientes_inicial.addAll(Arrays.asList(posicion.root.num_izq.split(",")));
            root_sigs = siguientes_inicial;
            evaluar_siguientes(posicion.list, siguientes_inicial);
            for (int j = 0; j < listTransicion.size(); j++) {
                if (!comprobar_estados()) {
                    if(!listTransicion.get(j).sig.equals(root_sigs)){
                        boolean val = evaluar_siguientes(posicion.list, listTransicion.get(j).sig);                        
                        if (val) {
                            break;
                        }
                    }
                }else{
                    break;
                }
            }
            listTransicion = crear_final(listTransicion);
            listTransicion = depurar_iguales(listTransicion);
            trans.add(new TablaTransiciones(name, listTransicion));
            generar_automata(trans);
            listTransicion = new ArrayList<Transicion>();
            trans = new ArrayList<TablaTransiciones>();
            una_vez = 0;
            contador = 0;
            contador2 = 0;
        }
    }

    public boolean comprobar_estados() {
        // VERIFICAR QUE LOS SIGUIENTES DE UN ESTADO DESTINO SON 1, SI SON MAS DE 1 DEBE DE TENER 
        // 2 ESTADOS PRINCIPALES CON EL
        boolean encontrado = true;
        ArrayList<Boolean> estados = new ArrayList<Boolean>();
        ArrayList<String> analizados = new ArrayList<String>();
        if (!listTransicion.isEmpty()) {
            for (Transicion transicion : listTransicion) {
                ArrayList<String> siguientes = transicion.sig;
                String destino = transicion.destino;
                int count = 0;
                boolean comprob = false;
                if (destino != "S0" && !siguientes.isEmpty()) {
                    for (Transicion transicion2 : listTransicion) {
                        if (!analizados.contains(destino)) {
                            if (count == siguientes.size()) {
                                comprob = true;
                                estados.add(comprob);
                                analizados.add(destino);
                            }else if(transicion2.transicion.equals(destino)){
                                count++;
                                if (count == (siguientes.size())) {
                                    comprob = true;
                                    analizados.add(destino);
                                }
                            }                                
                        }else{
                            comprob = true;
                        }
                    }
                    estados.add(comprob);                     
                }else{
                   encontrado = true;
                }
            }                
        }
        if (!estados.isEmpty()) {
            boolean falso_encontrado = false;
            for (int i = 0; i < estados.size(); i++) {
                if (estados.get(i).equals(false)) {
                    falso_encontrado = true;
                }
            }
            if (falso_encontrado) {
                encontrado = false;
            }
        }
        return encontrado;
    }

    public String crear_transicion_actual() {
        String transicion = "";
        transicion = "S"+contador;
        contador++;
        return transicion;
    }

    public String crear_transicion_destino(ArrayList<String> siguientes_actual, String tipo) {
        String nodo = "";
        if (!listTransicion.isEmpty()) {
            boolean encontrado = false;
            for (Transicion transicion : listTransicion) {
                if (root_sigs.equals(siguientes_actual)) {
                    nodo = transicion.destino;
                    encontrado = true;
                    break;
                }else if(transicion.sig.equals(siguientes_actual)){
                    nodo = "S"+(contador2);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                contador2++;
                nodo = "S"+(contador2);
            }
        }else{
            if (tipo == "Destino") {
                nodo = "S"+(contador2);
            }else{
                nodo = "S"+contador2;
                contador2++;
            }
        }
        return nodo;
    }

    public boolean evaluar_siguientes(ArrayList<Siguientes> siguientes_general, ArrayList<String> siguientes_actual) {       
        // String transicion_actual = crear_transicion_actual();
        boolean fin = false;
        String transicion_actual = crear_transicion_destino(siguientes_actual, "Actual");
        for (String num_hoja_siguiente : siguientes_actual) {
            for (Siguientes siguiente : siguientes_general) {
                if (num_hoja_siguiente.equals(siguiente.num_hoja)) {
                    if (siguiente.lexema.equals("#")) {
                        fin = true;
                    }
                    if (una_vez==0) {
                        String transicion_destino = crear_transicion_destino(siguiente.siguientes, "Destino");
                        boolean aceptacion = tiene_aceptacion(siguientes_general, siguientes_actual);
                        listTransicion.add(new Transicion(transicion_actual, siguiente.siguientes, "ε", transicion_destino, aceptacion));                                
                    }else{
                        String transicion_destino = crear_transicion_destino(siguiente.siguientes, "Destino");
                        boolean aceptacion = tiene_aceptacion(siguientes_general, siguientes_actual);
                        listTransicion.add(new Transicion(transicion_actual, siguiente.siguientes, siguiente.lexema, transicion_destino, aceptacion));                                
                        
                    }
                    // Salida del ciclo actual porque ya agregue la transicion pertinente
                    break;
                }
            }
        }
        if (una_vez == 0) {
            una_vez++;
            evaluar_siguientes(siguientes_general, siguientes_actual);
        }
        return fin;
    }
    
    public boolean tiene_aceptacion(ArrayList<Siguientes> siguientes, ArrayList<String> nums_hojas){
        String num_aceptacion = "";
        for (int i = 0; i < siguientes.size(); i++) {
            if (siguientes.get(i).lexema.equals("#")) {
                num_aceptacion = siguientes.get(i).num_hoja;
            }
        }
        for (int i = 0; i < nums_hojas.size(); i++) {
            if (nums_hojas.get(i).equals(num_aceptacion)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Transicion> depurar_iguales(ArrayList<Transicion> list) {
        ArrayList<Transicion> transiciones = new ArrayList<Transicion>();
        for (Transicion transicion : list) {
            if (transiciones.size() != 0) {
                boolean si_esta = false;
                for (int i = 0; i < transiciones.size(); i++) {
                    if ((transicion.destino.equals(transiciones.get(i).destino)) && (transicion.transicion.equals(transiciones.get(i).transicion)) && (transicion.caracter.equals(transiciones.get(i).caracter))) {
                        si_esta = true;
                    }
                }
                if (si_esta == false) {
                    transiciones.add(transicion);
                }
                     
            }else{
                transiciones.add(transicion);
            }
        }
        return transiciones;
    }

    public ArrayList<Transicion> crear_final(ArrayList<Transicion> list) {
        ArrayList<Transicion> transiciones = new ArrayList<Transicion>();
        String transicion_final = "S"+(contador2);
        int count = 0;
        ArrayList<Integer> nums = new ArrayList<Integer>();
        for (Transicion transicion : list) {
            if (transicion.aceptacion == true) {
                transiciones.add(new Transicion(transicion.transicion, null, "ε", transicion_final, true));
                transicion.aceptacion = false;
                if(transicion.caracter.equals("#")){
                    nums.add(count);
                }
            }
            count++;
        }
        for (int i = 0; i < nums.size(); i++) {
            int n = nums.get(i);
            if (n == list.size()) {
                n--;
            }
            list.remove(n);
        }
        list.addAll(transiciones);
        return list;
    }

    public void generar_automata(ArrayList<TablaTransiciones> tabla) {
        for (int i = 0; i < tabla.size(); i++) {
            TablaTransiciones nodo = tabla.get(i);
            String resultado = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>"+nodo.name+"</B>>;\nbgcolor=\"#50FAA3\";\n";
            String aceptados = "";
            for (int j = 0; j < nodo.transiciones.size(); j++) {
                Transicion trans = nodo.transiciones.get(j);
                if (trans.aceptacion) {
                    aceptados += trans.transicion + " ";
                }
            }
            if (aceptados != "") {
                resultado += "node[shape=doublecircle];"+aceptados+";\n"+"node[shape=circle];\nrankdir=LR;\n";                
            }else{
                resultado += "node[shape=doublecircle];"+aceptados+"\n"+"node[shape=circle];\nrankdir=LR;\n";
            }
            String nodos = "";
            String conexiones = "";
            for (int k = 0; k < nodo.transiciones.size(); k++) {
                if (!nodo.transiciones.get(k).caracter.equals("#")) {
                    Transicion trans = nodo.transiciones.get(k);
                    nodos += trans.transicion+"[label=\""+trans.transicion+"\"];\n";
                    nodos += trans.destino+"[label=\""+trans.destino+"\"];\n";
                    if (trans.caracter.contains("\"")) {
                        if (trans.caracter.contains("\\n") || trans.caracter.contains("\\r") || trans.caracter.contains("\\t")) {
                            trans.caracter = trans.caracter.replace("\"", "");
                            trans.caracter = "\\" + trans.caracter;
                            conexiones += trans.transicion + " -> " + trans.destino+"[label=\""+trans.caracter+"\"];\n";
                        }else{
                            conexiones += trans.transicion + " -> " + trans.destino+"[label="+trans.caracter+"];\n";                    
                        }
                    }else{
                        conexiones += trans.transicion + " -> " + trans.destino+"[label=\""+trans.caracter+"\"];\n";
                    }
                }
            }
            resultado += nodos+conexiones+"\n}";
            archivo_automata(nodo.name, resultado);
        }
    }

    public void archivo_automata(String name, String resultado) {
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Graphviz\\"+name+"afn.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\AFN_202010406\\"+name+"afn.png";
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