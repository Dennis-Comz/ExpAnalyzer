package objects;

public class ObjCadena {
    String nombre;
    String cadena;

    public ObjCadena(String nombre, String cadena) {
        this.nombre = nombre;
        this.cadena = cadena;
    }
    public String getNombre() {return this.nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getCadena() {return this.cadena;}
    public void setCadena(String cadena) {this.cadena = cadena;}
}