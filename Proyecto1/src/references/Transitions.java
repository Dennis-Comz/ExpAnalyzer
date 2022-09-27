package references;

import objects.LinkedListTransiciones;

public class Transitions {
    public static LinkedListTransiciones list;
    public static Transitions trans;

    public void initialize() {
        try{
            list = new LinkedListTransiciones();
        }catch(Exception e){}
    }

    public static Transitions getInstance() {
        if (trans == null) {
            list = new LinkedListTransiciones();
            trans = new Transitions();
        }
        return trans;
    }
}
