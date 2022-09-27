package objects;

import java.util.ArrayList;

public class LinkedListParams extends ArrayList<Intervalo>{

    public LinkedListParams() {super();}
    
    public void addParam(Intervalo intervalo) {
        this.add(intervalo);
    }
}