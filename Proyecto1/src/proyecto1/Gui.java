package proyecto1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import references.ErrorRef;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Gui implements ActionListener {
    // Componentes globales para permitir modificarlos en otras clases
    JLabel imagen;
    JFrame initial_frame;
    JMenuBar menu_bar;
    JMenu menu;
    JTextArea input_area, output_text;
    JScrollPane input_scroll, output_scroll;
    JButton generate_automata,parse_inputs, sendArbol, sendSiguiente, sendTrans, sendAuto, sendAFN, open_error;
    Administrador admin;
    JFileChooser fileChooser = new JFileChooser("Seleccione el archivo");
    JComboBox afd, arboles, transiciones, siguientes, afn;

    public Gui(){
        admin = new Administrador();
        principal();
        file_options();
        input();
        display_images();
        output();
    }

    public void principal() {
        // Imagen nueva de icono
        ImageIcon icon = new ImageIcon("C:/Users/denni/Documents/Varios_Progra/Proyecto1_OLC1_202010406/Proyecto1/src/images/icon.png");
        // frame utilizado para contener la informacion principal
        initial_frame = new JFrame();
        initial_frame.setTitle("ExpAnalyzer");
        initial_frame.setSize(1550,800);
        initial_frame.setLayout(null);
        initial_frame.getContentPane().setBackground(new Color(13,0,26));
        initial_frame.setLocationRelativeTo(null);
        initial_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initial_frame.setIconImage(icon.getImage());        
    }

    public void file_options() {
        // Menu desplegable y acciones por cada item del menu
        menu_bar = new JMenuBar();
        menu = new JMenu("Archivos");
        menu.add(new JMenuItem(new AbstractAction("Abrir") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int op = fileChooser.showOpenDialog(initial_frame);
                if (op == JFileChooser.APPROVE_OPTION) {
                    try {
                        Path filename = Path.of(fileChooser.getSelectedFile().toString());
                        String text = Files.readString(filename);
                        input_area.setText(text);
                    } catch (Exception e2) {System.out.println(e2);}
                    // input_area.setText();
                    System.out.println();
                }
            }
        }));
        menu.add(new JMenuItem(new AbstractAction("Guardar") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.getSelectedFile() != null) {
                    String textArea = input_area.getText();
                    try {
                        Path filename = Path.of(fileChooser.getSelectedFile().toString());
                        Files.writeString(filename, textArea);
                    } catch (IOException e1) {e1.printStackTrace();}
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "No se ha encontrado el archivo a editar.", "Error", JOptionPane.DEFAULT_OPTION);
                }               
            }
        }));
        menu.add(new JMenuItem(new AbstractAction("Guardar como...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textArea = input_area.getText();
                if (textArea.isEmpty() != true) {
                    int op = fileChooser.showSaveDialog(initial_frame);
                    if (op == JFileChooser.APPROVE_OPTION) {
                        File filo_to_save = fileChooser.getSelectedFile();
                        try {
                            Path filename = Path.of(filo_to_save.getAbsolutePath());
                            Files.writeString(filename, textArea);
                            } catch (IOException e1) {e1.printStackTrace();}
                    }
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El area de texto se encuentra vacia.", "Error", JOptionPane.DEFAULT_OPTION);
                }
            }
            
        }));
        menu.add(new JMenuItem(new AbstractAction("Generar JSON de salida") {

            @Override
            public void actionPerformed(ActionEvent e) {
                admin.crear_json();
                JOptionPane.showMessageDialog(initial_frame, "Archivo JSON Creado", "Exito", JOptionPane.DEFAULT_OPTION);
            }
            
        }));
        menu_bar.add(menu);
        initial_frame.setJMenuBar(menu_bar);        
    }
    // "C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Salidas_202010406\\salida.json"
    public void input() {
        // Cuadro de texto con el archivo seleccionado o para crear un nuevo archivo
        JLabel label_cuadro = new JLabel("Archivo de Entrada");
        label_cuadro.setBounds(10, 10, 200, 30);
        label_cuadro.setForeground(Color.WHITE);
        initial_frame.add(label_cuadro);

        input_area = new JTextArea();
        input_scroll = new JScrollPane(input_area);
        input_scroll.setBounds(10,50,600,400);
        input_scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        initial_frame.getContentPane().add(input_scroll);

        // Botones para generar automata y analizar entradas
        generate_automata = new JButton("Generar Automata");
        generate_automata.setBounds(45, 470, 150, 30);
        initial_frame.add(generate_automata);
        generate_automata.addActionListener(this);
        generate_automata.setActionCommand("automata");

        parse_inputs = new JButton("Analizar Entradas");
        parse_inputs.setBounds(225, 470, 150,30);
        initial_frame.add(parse_inputs);
        parse_inputs.addActionListener(this);
        parse_inputs.setActionCommand("analizar");

        open_error = new JButton("Abrir Reporte de Errores");
        open_error.setBounds(405, 470, 180,30);
        initial_frame.add(open_error);
        open_error.addActionListener(this);
        open_error.setActionCommand("errores");
    }

    public void output() {
        // Salida generada al analizar el archivo
        JLabel lb_salida = new JLabel("Salida");
        lb_salida.setBounds(10, 540, 100, 20);
        lb_salida.setForeground(Color.WHITE);
        initial_frame.add(lb_salida);

        output_text = new JTextArea();
        
        output_scroll = new JScrollPane(output_text);
        output_scroll.setBounds(10,560,1450,150);
        output_scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        initial_frame.getContentPane().add(output_scroll);
        initial_frame.setVisible(true);
    }

    public void display_images() {
        Icon icon = new ImageIcon("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\send.png");
        
        // ARBOLES
        JLabel arbol = new JLabel("Arboles");
        arbol.setBounds(630,10,150,30);
        arbol.setForeground(Color.WHITE);
        initial_frame.add(arbol);
        arboles = new JComboBox();
        arboles.setBounds(650, 40, 150, 20);
        initial_frame.getContentPane().add(arboles);
        sendArbol = new JButton(icon);
        sendArbol.setBounds(630, 40, 20,20);
        initial_frame.add(sendArbol);
        sendArbol.addActionListener(this);
        sendArbol.setActionCommand("arbol");

        // SIGUIENTES
        JLabel nexts = new JLabel("Siguientes");
        nexts.setBounds(810,10,150,30);
        nexts.setForeground(Color.WHITE);
        initial_frame.add(nexts);
        siguientes = new JComboBox();
        siguientes.setBounds(830, 40, 150, 20);
        initial_frame.getContentPane().add(siguientes);
        sendSiguiente = new JButton(icon);
        sendSiguiente.setBounds(810, 40, 20,20);
        initial_frame.add(sendSiguiente);
        sendSiguiente.addActionListener(this);
        sendSiguiente.setActionCommand("sig");

        // TRANSICIONES
        JLabel trans = new JLabel("Transiciones");
        trans.setBounds(990,10,150,30);
        trans.setForeground(Color.WHITE);
        initial_frame.add(trans);
        transiciones = new JComboBox();
        transiciones.setBounds(1010, 40, 150, 20);
        initial_frame.getContentPane().add(transiciones);
        sendTrans = new JButton(icon);
        sendTrans.setBounds(990, 40, 20,20);
        initial_frame.add(sendTrans);
        sendTrans.addActionListener(this);
        sendTrans.setActionCommand("trans");

        // AUTOMATA DETERMINISTA
        JLabel auto_fd = new JLabel("Automatas Deterministas");
        auto_fd.setBounds(1170,10,150,30);
        auto_fd.setForeground(Color.WHITE);
        initial_frame.add(auto_fd);
        afd = new JComboBox();
        afd.setBounds(1190, 40, 150, 20);
        initial_frame.getContentPane().add(afd);
        sendAuto = new JButton(icon);
        sendAuto.setBounds(1170, 40, 20,20);
        initial_frame.add(sendAuto);
        sendAuto.addActionListener(this);
        sendAuto.setActionCommand("auto");

        // AUTOMATA NO DETERMINISTA
        JLabel auto_nfd = new JLabel("Automatas No Deterministas");
        auto_nfd.setBounds(1350,10,150,30);
        auto_nfd.setForeground(Color.WHITE);
        initial_frame.add(auto_nfd);
        afn = new JComboBox();
        afn.setBounds(1370, 40, 150, 20);
        initial_frame.getContentPane().add(afn);
        sendAFN = new JButton(icon);
        sendAFN.setBounds(1350, 40, 20,20);
        initial_frame.add(sendAFN);
        sendAFN.addActionListener(this);
        sendAFN.setActionCommand("afn");

        // Panel donde se despliega la images
        JPanel incial = new JPanel();
        imagen = new JLabel();
        imagen.setOpaque(true);
        imagen.setBackground(Color.WHITE);
        imagen.setForeground(Color.WHITE);
        incial.add(imagen);
        JScrollPane scrollPane = new JScrollPane(incial);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(630,70,830,470);
        initial_frame.getContentPane().add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("analizar")) {
            String result = admin.evaluar_cadena();
            output_text.setText(result);
        }else if(action.equals("automata")){
            if (fileChooser.getSelectedFile() != null) {
                admin.analizar_entrada(fileChooser.getSelectedFile().toString());
                String val = admin.hay_error();
                if (val != "") {
                    JOptionPane.showMessageDialog(initial_frame, "El archivo analizado contiene errores.", "Error", JOptionPane.DEFAULT_OPTION);
                    output_text.setForeground(Color.RED);
                    output_text.setText(val);                    
                }else{
                    output_text.setForeground(Color.BLACK);
                    output_text.setText("");
                    JOptionPane.showMessageDialog(initial_frame, "Archivo analizado con exito", "Exito", JOptionPane.DEFAULT_OPTION);                    
                }
                String[] carps = new String[]{"Arboles_202010406","Automatas_AFD_202010406", "Siguientes_202010406", "Transiciones_202010406", "AFN_202010406"};
                for (int i = 0; i < carps.length; i++) {
                    if (carps[i]=="Arboles_202010406") {
                        fill_combos(carps[i], arboles);
                    }else if(carps[i]=="Automatas_AFD_202010406"){
                        fill_combos(carps[i], afd);
                    }else if(carps[i]=="Siguientes_202010406"){
                        fill_combos(carps[i], siguientes);
                    }else if(carps[i]=="Transiciones_202010406"){
                        fill_combos(carps[i], transiciones);                        
                    }else if(carps[i]=="AFN_202010406"){
                        fill_combos(carps[i], afn);                        
                    }
                }
            }
        }else if(action.equals("errores")){
            boolean existe= admin.abrir_archivo();
            if (!existe) {
                JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }else if(action.equals("arbol")){
            if (arboles.getSelectedItem() != null) {
                File img = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Arboles_202010406\\"+arboles.getSelectedItem());
                if (img.exists()) {
                    try {
                        BufferedImage picture = ImageIO.read(img);
                        imagen.setIcon(new ImageIcon(picture));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                        
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
                }
            }else{
                JOptionPane.showMessageDialog(initial_frame, "Seleccione un archivo valido", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }else if(action.equals("sig")){
            if (siguientes.getSelectedItem() != null) {
                File img = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Siguientes_202010406\\"+siguientes.getSelectedItem());
                if (img.exists()) {
                    try {
                        BufferedImage picture = ImageIO.read(img);
                        imagen.setIcon(new ImageIcon(picture));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                        
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
                }
            }else{
                JOptionPane.showMessageDialog(initial_frame, "Seleccione un archivo valido", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }else if(action.equals("trans")){
            if (transiciones.getSelectedItem() != null) {
                File img = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Transiciones_202010406\\"+transiciones.getSelectedItem());
                if (img.exists()) {
                    try {
                        BufferedImage picture = ImageIO.read(img);
                        imagen.setIcon(new ImageIcon(picture));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                        
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
                }
            }else{
                JOptionPane.showMessageDialog(initial_frame, "Seleccione un archivo valido", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }else if(action.equals("auto")){
            if (afd.getSelectedItem() != null) {
                File img = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\Automatas_AFD_202010406\\"+afd.getSelectedItem());
                if (img.exists()) {
                    try {
                        BufferedImage picture = ImageIO.read(img);
                        imagen.setIcon(new ImageIcon(picture));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                        
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
                }
            }else{
                JOptionPane.showMessageDialog(initial_frame, "Seleccione un archivo valido", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }else if(action.equals("afn")){
            if (afd.getSelectedItem() != null) {
                File img = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\AFN_202010406\\"+afn.getSelectedItem());
                if (img.exists()) {
                    try {
                        BufferedImage picture = ImageIO.read(img);
                        imagen.setIcon(new ImageIcon(picture));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }                        
                }else{
                    JOptionPane.showMessageDialog(initial_frame, "El archivo no existe", "Error", JOptionPane.DEFAULT_OPTION);                    
                }
            }else{
                JOptionPane.showMessageDialog(initial_frame, "Seleccione un archivo valido", "Error", JOptionPane.DEFAULT_OPTION);                    
            }
        }
    }

    public void fill_combos(String carpeta, JComboBox item) {
        File folder = new File("C:\\Users\\denni\\Documents\\Varios_Progra\\Proyecto1_OLC1_202010406\\Proyecto1\\src\\images\\"+carpeta);
        item.removeAllItems();
        
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                item.addItem(listOfFiles[i].getName());
            }
        }
    }
}