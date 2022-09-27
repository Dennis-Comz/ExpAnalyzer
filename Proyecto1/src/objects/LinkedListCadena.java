package objects;

import java.util.ArrayList;

public class LinkedListCadena extends ArrayList<ObjCadena>{
    public LinkedListCadena() {super();}

    public void addCadena(String name, String cadena) {
        this.add(new ObjCadena(name, cadena));
    }
}
