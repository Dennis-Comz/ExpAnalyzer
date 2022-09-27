package references;

import objects.LinkedListError;

public class ErrorRef {
    public static LinkedListError list;
    public static ErrorRef error;

    public void analize() {
        try {
            list = new LinkedListError();
        } catch (Exception e) {}
    }

    public static ErrorRef getInstance() {
        if (error == null) {
            list = new LinkedListError();
            error = new ErrorRef();
        }
        return error;
    }

    public static LinkedListError getList(){
		return list;
	}
	
	public static void  setList(LinkedListError list){
		ErrorRef.list = list;
	}

}
