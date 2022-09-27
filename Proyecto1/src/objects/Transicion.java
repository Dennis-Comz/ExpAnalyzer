package objects;

import java.util.ArrayList;

public class Transicion{
    String transicion;
    ArrayList<String> sig;
    String caracter;
    String destino;
    boolean aceptacion;


    public Transicion(String transicion, ArrayList<String> sig, String caracter, String destino, boolean aceptacion) {
        this.transicion = transicion;
        this.sig = sig;
        this.caracter = caracter;
        this.destino = destino;
        this.aceptacion = aceptacion;
    }
    
    public String toString(){
        return "Transicion: " + transicion + " -> Siguientes: "+sig+" -> Caracter: " + caracter+" -> Destino: "+destino+" -> aceptacion: "+aceptacion+"\n";
    }


    public String getTransicion() {
        return this.transicion;
    }

    public void setTransicion(String transicion) {
        this.transicion = transicion;
    }

    public ArrayList<String> getSig() {
        return this.sig;
    }

    public void setSig(ArrayList<String> sig) {
        this.sig = sig;
    }

    public String getCaracter() {
        return this.caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getDestino() {
        return this.destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public boolean isAceptacion() {
        return this.aceptacion;
    }

    public boolean getAceptacion() {
        return this.aceptacion;
    }

    public void setAceptacion(boolean aceptacion) {
        this.aceptacion = aceptacion;
    }

}
