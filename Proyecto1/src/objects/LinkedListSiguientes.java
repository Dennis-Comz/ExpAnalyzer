package objects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import objects.BinaryTree.Tree_Node;
import references.AuxiliarRef;
import references.Transitions;

public class LinkedListSiguientes extends ArrayList<TablaSiguientes>{
    int contador = 0;
    int contador2 = 0;
    int una_vez = 0;
    ArrayList<Transicion> listTransicion = new ArrayList<Transicion>();
    ArrayList<String> root_sigs = new ArrayList<String>();

    public LinkedListSiguientes(){super();}

    public void addTabla(Tree_Node root, String name, ArrayList<Siguientes> lista){
        this.add(new TablaSiguientes(root, name, lista));
        AuxiliarRef.list.addInfo(root, name, lista);
    }

    public ArrayList<String> removeDuplicates(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<String>();
        for (String elemeString : list) {
            if (!newList.contains(elemeString)) {
                newList.add(elemeString);
            }
        }
        newList.sort(Comparator.comparing(Integer::parseInt));
        return newList;
    }
    
    public void generar_graphviz(){
        for (int i = 0; i < this.size(); i++) {
            TablaSiguientes nodo = this.get(i);
            String resultado = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>"+nodo.name+"</B>>;\nbgcolor=\"#50FAA3\";\ngraph[nodesep=1];\nnode[shape=none, height=15, width=15];\n";
            resultado += "Tabla[fixedsize=true,label=<\n<TABLE bgcolor=\"white\" CELLSPACING=\"2\" CELLPADDING=\"2\" BORDER=\"1\">\n";
            resultado += "<TR>\n<TD>No.</TD>\n<TD>Lexema</TD>\n<TD>Siguientes</TD>\n</TR>\n";
            for (int j = 0; j < nodo.list.size(); j++) {
                Siguientes sig = nodo.list.get(j);
                resultado += "<TR>\n<TD>"+String.valueOf(j+1)+"</TD>\n<TD>"+sig.lexema+"</TD>\n";
                String nexts = "";
                sig.siguientes = removeDuplicates(sig.siguientes);
                // ArrayList<String> siguientes = sig.siguientes;
                for (int k = 0; k < sig.siguientes.size(); k++) {
                    if (k+1 != sig.siguientes.size()) {                        
                        nexts += sig.siguientes.get(k) +",";
                    }else{nexts += sig.siguientes.get(k);}
                }
                resultado += "<TD>"+nexts+"</TD>\n</TR>\n";
            }
            resultado += "</TABLE>>]\n}\n";
            archivo(nodo.name, resultado);
        }
    }

    public void archivo(String name, String resultado) {
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Graphviz\\"+name+"sig.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Siguientes_202010406\\"+name+"sig.png";
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

    public void generar_transiciones(){
        // Ciclo para esta lista
        for (int i = 0; i < this.size(); i++) {
            // Obtencion de datos
            TablaSiguientes tablaSiguientes = this.get(i);
            String name = tablaSiguientes.name;
            ArrayList<String> siguientes_inicial = new ArrayList<String>();
            siguientes_inicial.addAll(Arrays.asList(tablaSiguientes.root.num_izq.split(",")));

            /*
            Mando a evaluar los siguientes del nodo en el que estoy, es decir del nodo raiz del arbol
            para verificar que no son un estado de aceptacion
            */
            root_sigs = siguientes_inicial;
            evaluar_siguientes(tablaSiguientes.list, siguientes_inicial);
            for (int j = 0; j < listTransicion.size(); j++) {
                if (!comprobar_estados()) {
                    if (una_vez == 0) {
                        evaluar_siguientes(tablaSiguientes.list, listTransicion.get(j).sig);                        
                    }else if(una_vez != 0 && !listTransicion.get(j).sig.equals(root_sigs)){
                        evaluar_siguientes(tablaSiguientes.list, listTransicion.get(j).sig);                        
                    }
                }else{
                    break;
                }
            }
            listTransicion = depurar_iguales(listTransicion);
            Transitions.list.addTabla(name, listTransicion);
            listTransicion = new ArrayList<Transicion>();
            contador = 0;
            contador2 = 0;
        }
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
                    nodo = "S0";
                    encontrado = true;
                    break;
                }else if(transicion.sig.equals(siguientes_actual)){
                    nodo = transicion.destino;
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
                if (siguientes_actual.equals(root_sigs)) {
                    nodo = "S"+(contador2-1);                    
                }else{
                    nodo = "S"+(contador2);
                }
            }else{
                nodo = "S"+contador2;
                contador2++;
            }
        }
        return nodo;
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

    public void evaluar_siguientes(ArrayList<Siguientes> siguientes_general, ArrayList<String> siguientes_actual) {
        if (una_vez == 0) {
            una_vez++;
        }       
        // String transicion_actual = crear_transicion_actual();
        String transicion_actual = crear_transicion_destino(siguientes_actual, "Actual");
        for (String num_hoja_siguiente : siguientes_actual) {
            for (Siguientes siguiente : siguientes_general) {
                if (num_hoja_siguiente.equals(siguiente.num_hoja)) {
                    String transicion_destino = crear_transicion_destino(siguiente.siguientes, "Destino");
                    boolean aceptacion = tiene_aceptacion(siguientes_general, siguientes_actual);
                    listTransicion.add(new Transicion(transicion_actual, siguiente.siguientes, siguiente.lexema, transicion_destino, aceptacion));    
                    // Salida del ciclo actual porque ya agregue la transicion pertinente
                    break;
                }
            }
        }
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
}