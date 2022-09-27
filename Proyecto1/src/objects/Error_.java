package objects;

public class Error_ {
    String message;
    String type;
    String caracter;
    String linea;
    String columna;
    

    public Error_(String message, String type, String caracter, String linea, String columna) {
        this.message = message;
        this.type = type;
        this.caracter = caracter;
        this.linea = linea;
        this.columna = columna;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaracter() {
        return this.caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getLinea() {
        return this.linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getColumna() {
        return this.columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String toString() {
        return type + "; Mensaje: "+message+"; Caracter: "+caracter+"; Linea: "+linea+"; Columna: "+columna+"\n";
    }
}