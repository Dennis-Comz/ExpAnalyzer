package objects;

import java.util.ArrayList;
import java.util.List;

public class Siguientes {
    String num_hoja;
    String lexema;
    ArrayList<String> siguientes;
    public Siguientes(String num_hoja, String lexema) {
        this.num_hoja = num_hoja;
        this.lexema = lexema;
        this.siguientes = new ArrayList<String>();
    }
    public String getNum_hoja() {return num_hoja;}
    public void setNum_hoja(String num_hoja) {this.num_hoja = num_hoja;}
    public String getLexema() {return lexema;}
    public void setLexema(String lexema) {this.lexema = lexema;}
    public void setSiguiente(List<String> sig) {siguientes.addAll(sig);}
    public String toString(){
        return "Numero: " + num_hoja + " -> Lexema: " + lexema+" -> Siguientes: "+siguientes+"\n";
    }
}
