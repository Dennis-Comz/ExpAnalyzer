package proyecto1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import objects.ObjCadena;
import objects.TablaTransiciones;
import objects.Transicion;

import java.awt.Desktop;
import references.*;

public class Administrador {
    public Administrador() {}
    ArrayList<json_obj> listobjs = new ArrayList<json_obj>();

    public void analizar_entrada(String path) {
        listobjs = new ArrayList<json_obj>();
        Conjunto conj = Conjunto.getInstance();
        Cadena cd = Cadena.getInstance();cd.analize();
        Tree tr = Tree.getInstance();tr.initialize();
        AuxiliarRef aux = AuxiliarRef.getInstance();aux.initialize();
        Transitions trt = Transitions.getInstance();trt.initialize();
        SiguienteRef sgr = SiguienteRef.getInstance();sgr.initialize();
        ErrorRef error = ErrorRef.getInstance();error.analize();
        conj.analize(path);
        Tree.generar_metodo();
        SiguienteRef.list.generar_graphviz();
        SiguienteRef.list.generar_transiciones(); 
        Transitions.list.generar_graphviz();
        Transitions.list.generar_automata();
        AuxiliarRef.list.generar_afn();
    }

    public String hay_error() {
        String error = "";
        if (!ErrorRef.list.isEmpty()) {
            String resultado = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n\t<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\n\t<title>Reporte de Errores</title>\n</head>\n<body style=\"text-align: center; background-color: gray;\">\t\n<h1 style=\"color: rgb(100, 212, 237);\">REPORTE DE ERRORES</h1>\t\n<table class=\"table\">\t\t\n<thead>\t\t\t\n<tr>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">No.</td>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">Tipo</td>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">Descripcion</td>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">Caracter</td>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">Columna</td>\t\t\t\t\n<td class=\"table-primary\" scope=\"col\">Linea</td>\t\t\t\n</tr>\t\t\n</thead>\t\t\n<tbody>";
            for (int i = 0; i < ErrorRef.list.size(); i++) {
                resultado += "\t\t\t\n<tr>";

                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+String.valueOf(i)+"</td>";
                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+ErrorRef.list.get(i).getType()+"</td>";
                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+ErrorRef.list.get(i).getMessage()+"</td>";
                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+ErrorRef.list.get(i).getCaracter()+"</td>";
                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+ErrorRef.list.get(i).getColumna()+"</td>";
                resultado += "\t\t\t\t\n<td class=\"table-danger\">"+ErrorRef.list.get(i).getLinea()+"</td>";

                error += ErrorRef.list.get(i).getMessage() + ErrorRef.list.get(i).getCaracter() + ";\t Linea: "+ErrorRef.list.get(i).getLinea()+" Columna: "+ErrorRef.list.get(i).getColumna()+"\n";
                resultado += "\t\t\t\n</tr>";
            }
            resultado += "\t\t\t\n</tbody>\t\t\n</table>";
            resultado += "\t\t\n<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>\"";
            resultado += "</body>\n</html>";
            generar_archivo(resultado);
        }
        return error;
    }

    public void generar_archivo(String resultado) {
        String ubicacion = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Errores_202010406\\ReporteDeErrores.html";
        File file = new File(ubicacion);
        try {file.createNewFile();} catch (Exception e) {}
        FileWriter writer;
        try {
            writer = new FileWriter(ubicacion);
            writer.write(resultado);
            writer.close();
        } catch (Exception e) {}
    }

    public boolean abrir_archivo(){
        String ubicacion = "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Errores_202010406\\ReporteDeErrores.html";
        File file = new File(ubicacion);
        if (file.exists()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
                return true;
            } catch (IOException e) {e.printStackTrace();}
        }
        return false;
    }

    public String evaluar_cadena(){
        String resultado = "";
        for (ObjCadena cadena : Cadena.list) {
            ArrayList<String> aceptado =new ArrayList<String>();
//            ArrayList<String> no_aceptado =new ArrayList<String>();
            ArrayList<Integer> iterador =new ArrayList<Integer>();
            String name = cadena.getNombre();
            String s = cadena.getCadena();
            StringBuilder sbd = new StringBuilder(s);
            sbd.deleteCharAt(s.length()-1);
            sbd.deleteCharAt(0);
            s = sbd.toString();
            for (TablaTransiciones transicion : Transitions.list) {
                ArrayList<Transicion> list = transicion.getTransiciones();
                if (transicion.getName().equals(name)) {
                    for (int i = 0; i < s.length(); i++) {
                        String eval = Character.toString(s.charAt(i));
                        for (int j = 0; j < list.size(); j++) {
                            if (Character.toString(list.get(j).getCaracter().charAt(0)).equals("\"")) {
                                StringBuilder sbr = new StringBuilder(list.get(j).getCaracter());
                                sbr.deleteCharAt(list.get(j).getCaracter().length()-1);
                                sbr.deleteCharAt(0);
                                list.get(j).setCaracter(sbr.toString());
                            }
                            if (!list.get(j).getCaracter().contains("{") && eval.equals(list.get(j).getCaracter()) && !iterador.contains(j)) {
                                if (list.get(j).getTransicion().equals(list.get(j).getDestino())) {
                                    aceptado.add(eval);                                    
                                    break;
                                }else{
                                    aceptado.add(eval);
                                    iterador.add(j);
                                    break;
                                }
                            }else if(list.get(j).getCaracter().contains("{") && !iterador.contains(j)){
                                String conj = list.get(j).getCaracter();
                                StringBuilder sb = new StringBuilder(conj);
                                sb.deleteCharAt(conj.length()-1);
                                sb.deleteCharAt(0);
                                conj = sb.toString();
                                for (int k = 0; k < Conjunto.list.size(); k++) {
                                    if (Conjunto.list.get(k).get_nombre().equals(conj)) {
                                        ArrayList<String> vals = Conjunto.list.get(k).get_lista();
                                        if (list.get(j).getTransicion().equals(list.get(j).getDestino()) && vals.contains(eval)) {
                                            if (vals.contains(eval)) {
                                                aceptado.add(eval);
                                                break;
                                            }
                                        }else if(vals.contains(eval)){
                                            if (vals.contains(eval)) {
                                                aceptado.add(eval);
                                                iterador.add(j);
                                                break;
                                            }
                                        }else{
                                            i--;
                                            iterador.add(j);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            boolean valido = true;
            for (int j = 0; j < aceptado.size(); j++) {
                if (!aceptado.get(j).equals(Character.toString(s.charAt(j)))) {
                    valido = false;
                }
            }
            if (valido) {
                resultado += "La expresion: "+s+" es valida con la expresion regular "+name+"\n";                
                listobjs.add(new json_obj(s, name, "Cadena valida"));
            }else{
                resultado += "La expresion: "+s+" no es valida con la expresion regular "+name+"\n";
                listobjs.add(new json_obj(s, name, "Cadena No valida"));
            }
        }
        return resultado;
    }

    public void crear_json() {
        ArrayList<json_obj> list = listobjs;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        try {
            FileWriter writer = new FileWriter("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Salidas_202010406\\salida.json");
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}

class json_obj{
    String valor;
    String exp;
    String result;

    public json_obj(String valor, String exp, String result) {
        this.valor = valor;
        this.exp = exp;
        this.result = result;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getExp() {
        return this.exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}