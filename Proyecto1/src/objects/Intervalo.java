package objects;

import java.util.ArrayList;

public class Intervalo {
    String nombre;
    String intervalo;
    ArrayList<String> datos;
    public Intervalo(String nombre, String intervalo) {
        this.nombre = nombre;
        this.intervalo = intervalo;
        this.datos = new ArrayList<String>();
        if (intervalo.indexOf('~') != -1) {
            char inicio = intervalo.charAt(0);
            char fin = intervalo.charAt(2);
            if (inicio > fin) {
                for (int i = fin; i <= inicio; i++) {
                    char d = (char)i;
                    if (inicio < '0' || inicio  > '9' && inicio < 'A' || inicio > 'Z' && inicio < 'a' || inicio > 'z' && inicio < '~') {
                        if ((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                            continue;
                        }else{datos.add(String.valueOf(d));}
                    }else{
                        if (i>90&&i<97) {
                            continue;
                        }
                        datos.add(String.valueOf(d));
                    }
                }
            }else if(fin > inicio){
                for (int i = inicio; i <= fin; i++) {
                    char d = (char)i;
                    if (inicio < '0' || inicio  > '9' && inicio < 'A' || inicio > 'Z' && inicio < 'a' || inicio > 'z' && inicio < '~') {
                        if ((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                            continue;
                        }else{datos.add(String.valueOf(d));}
                    }else{
                        if (i>90&&i<97) {
                            continue;
                        }
                        datos.add(String.valueOf(d));
                    }
                }
            }else{
                datos.add(String.valueOf(inicio));
            }
        }else if (intervalo.indexOf('¿') != -1) {
            for (int i = 0; i < intervalo.length(); i++) {
                if (intervalo.charAt(i) != '¿') {
                    datos.add(String.valueOf(intervalo.charAt(i)));
                }
            }
        }else{
            datos.add(String.valueOf(intervalo));
        }
    }
    public String get_nombre(){return nombre;}
    public String get_intervalo(){return intervalo;}
    public ArrayList<String> get_lista(){return datos;}
    public void set_nombre(String nombre) {this.nombre = nombre;}
    public void set_inicio(String intervalo) {this.intervalo = intervalo;}
}