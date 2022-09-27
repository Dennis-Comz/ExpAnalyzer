package references;

import objects.AuxiliarLinkedList;

public class AuxiliarRef {
    public static AuxiliarLinkedList list;
    public static AuxiliarRef aux;

    public void initialize() {
        try {
            list = new AuxiliarLinkedList();
        } catch (Exception e) {}
    }

    public static AuxiliarRef getInstance(){
        if (aux == null) {
            list = new AuxiliarLinkedList();
            aux = new AuxiliarRef();
        }
        return aux;
    }
}
