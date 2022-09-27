package references;

import objects.LinkedListSiguientes;

public class SiguienteRef {
    public static LinkedListSiguientes list;
    public static SiguienteRef next;

    public void initialize() {
        try {
            list = new LinkedListSiguientes();
        } catch (Exception e) {}
    }

    public static SiguienteRef getInstance(){
        if (next == null) {
            list = new LinkedListSiguientes();
            next = new SiguienteRef();
        }
        return next;
    }

    public static LinkedListSiguientes getList() {
        return list;
    }
    public static void setList(LinkedListSiguientes list) {
        SiguienteRef.list = list;
    }
}
