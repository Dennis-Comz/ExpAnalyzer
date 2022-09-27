package references;

import objects.LinkedListCadena;

public class Cadena {
    public static LinkedListCadena list;
    public static Cadena cadena;
    
    public void analize() {
        try {
            list = new LinkedListCadena();
        } catch (Exception e) {}
    }
    public static Cadena getInstance(){
        if (cadena == null) {
            list = new LinkedListCadena();
            cadena = new Cadena();
        }
        return cadena;
    }
    public static LinkedListCadena getList() {
        return list;
    }
    public static void setList(LinkedListCadena list) {
        Cadena.list = list;
    }

}
