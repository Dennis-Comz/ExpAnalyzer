package references;

import java.io.FileReader;

import  objects.LinkedListParams;

public class Conjunto {
    public static LinkedListParams list;
    public static Conjunto conjunto;

    public void analize(String path){
        try {
            list = new LinkedListParams();
            analizadores.Sintactico pars;
            try {
                analizadores.Lexico lexico = new analizadores.Lexico(new FileReader(path));
                pars = new analizadores.Sintactico(lexico);
                pars.parse();
            } catch (Exception ex) {
                System.out.println("Error fatal en compilación de entrada.");
                System.out.println("Causa: "+ex.getCause());
            } 
        } catch (Exception e) {}
    }
    public static Conjunto getInstance(){
        if (conjunto == null) {
            list = new LinkedListParams();
            conjunto = new Conjunto();
        }
        return conjunto;
    }

    public static LinkedListParams getList(){
        return list;
    }

    public static void setList(LinkedListParams list){
        Conjunto.list = list;
    }
}