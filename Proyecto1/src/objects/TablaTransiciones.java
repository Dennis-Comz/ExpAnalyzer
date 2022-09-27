package objects;

import java.util.ArrayList;
public class TablaTransiciones {
    String name;
    ArrayList<Transicion> transiciones;

    public TablaTransiciones(String name, ArrayList<Transicion> transiciones) {
        this.name = name;
        this.transiciones = transiciones;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Transicion> getTransiciones() {
        return this.transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

}
