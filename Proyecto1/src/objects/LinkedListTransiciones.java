package objects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LinkedListTransiciones extends ArrayList<TablaTransiciones>{
    public LinkedListTransiciones(){super();}

    public void addTabla(String name, ArrayList<Transicion> transiciones) {
        this.add(new TablaTransiciones(name, transiciones));
    }

    public void generar_graphviz() {
        for (int i = 0; i < this.size(); i++) {
            TablaTransiciones nodo = this.get(i);
            String resultado = "digraph G{\nlabelloc=\"t\";\nlabel=<<B>"+nodo.name+"</B>>;\nbgcolor=\"#50FAA3\";\ngraph[nodesep=1];\nnode[shape=none, height=15, width=15];\n";
            resultado += "Tabla[fixedsize=true,label=<\n<TABLE bgcolor=\"white\" CELLSPACING=\"2\" CELLPADDING=\"2\" BORDER=\"1\">\n";
            resultado += "<TR>\n<TD>De Estado</TD>\n<TD>Con Elemento Transicion</TD>\n<TD>A Estado</TD>\n</TR>\n";
            for (int j = 0; j < nodo.transiciones.size(); j++) {
                Transicion trans = nodo.transiciones.get(j);
                resultado += "<TR>\n<TD>"+trans.transicion+"</TD>\n<TD>"+trans.caracter+"</TD>\n<TD>"+trans.destino+"</TD>\n</TR>";
            }
            resultado += "</TABLE>>]\n}\n";
            archivo(nodo.name, resultado);
        }
    }

    public void generar_automata() {
        for (int i = 0; i < this.size(); i++) {
            TablaTransiciones nodo = this.get(i);
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

    public void archivo(String name, String resultado) {
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Graphviz\\"+name+"trans.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Transiciones_202010406\\"+name+"trans.png";
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

    public void archivo_automata(String name, String resultado) {
        String ubicacion_dot = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Graphviz\\"+name+"auto.dot";
        String ubicacion_img = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Automatas_AFD_202010406\\"+name+"auto.png";
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
