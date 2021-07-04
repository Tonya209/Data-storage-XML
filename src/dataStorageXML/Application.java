package dataStorageXML;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javaapplication1.DOMUtil;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.w3c.dom.Document;

/**
 *
 * @author Antonina Hales
 */
public class Application extends JFrame implements ActionListener{
    
    
    private JButton btnAddText = new JButton("Add datos TXT");
    private JButton btnCrearXML = new JButton("Crear XML");
    private JLabel lblInf = new JLabel();
    private String docStr = "";
    
    private boolean puedoCreaXML = false; 
    private boolean hayDatosErroneos = false;
    
    public Application(){
        
        //Configuracion de la ventana o JFrame.
        setTitle("Texto to XML");
        setResizable(false);
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Container content = this.getContentPane();
        
        //Configuracion del contenedor central y pricipal.
        
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(btnAddText);
        panel.add(btnCrearXML);
        panel.add(lblInf);
        
        btnCrearXML.addActionListener(this);
        btnAddText.addActionListener(this);
        btnCrearXML.setName("crear");
        btnAddText.setName("add");
        lblInf.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblInf.setText("Elige una opcion.");
        
        
        //Posicionamiento del panel central.
        content.add(panel, BorderLayout.CENTER);
        
        
    }

   
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        enabledBtns(false);
        
       if (ae.getSource().getClass() == JButton.class){
           JButton btn = (JButton)ae.getSource();
           if (btn.getName()=="add"){
               puedoCreaXML = false;
               String a =  JOptionPane.showInputDialog("Introduzca datos para procesar");
               lblInf.setText("Procesando datos...");
               //Esto es para que no quede bloqueado y se vean los mensajes.
               if(a!=null){    
                    EventQueue.invokeLater(new Runnable() {

                          @Override
                          public void run() {
                              docStr="";
                              hayDatosErroneos = false;
                              decodeString(a);
                          }
                      });
               }else{
                   enabledBtns(true);
                   lblInf.setText("Accion add cancelada.");
               }
              
           }else if(btn.getName()=="crear"){
               enabledBtns(true);
               
               if (!puedoCreaXML){
                   showAlert("No se puede crear el xml.\n Todavia no hay datos.", "Problema"); 
               }
               else{
                   int op = JOptionPane.YES_OPTION;
                   if (hayDatosErroneos){
                       op = JOptionPane.showConfirmDialog(this, "Los datos de entrada contienen errores\n"
                               + "no se guardaran esos datos errones. ¿Quieres continuar?", 
                              "Atención", JOptionPane.YES_NO_OPTION);
                   }
                   
                   if(op == JOptionPane.YES_OPTION){
                    
                        JFileChooser fc = new JFileChooser();
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichero XML", "xml");
                        fc.setFileFilter(filter);

                        int returnVal = fc.showSaveDialog(this);


                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            Document doc = DOMUtil.String2DOM("<datos_cliente>"+docStr+"</datos_cliente>");
                            //Guarda el doc xml a un fichero.
                            if (DOMUtil.DOM2XML(doc, file.getPath())){
                                showAlert("El fichero se ha guardado correctamente", "Atención");
                               
                                //inicializar de nuevo el programa.
                                puedoCreaXML = false;
                                docStr = "";
                                hayDatosErroneos = false;
                   
                            }else{
                                showAlert("Error al intentar guardar el fichero.\n"
                                        + "lo siento pero algo ha ido mal upsss.", "Error");
                            }
                        } else {
                            showAlert("El fichero no se ha guardado.", "Atención");
                        } 

                       
               }else{
                  lblInf.setText("Accion Crear cancelada.");
               }
           }
         }
       } 
       
    }
    
    // el metodo que divide la cadena intoducida para luego separar los datos
    public void decodeString(String data){
        String[] listaDatos = data.split(",");
        if (listaDatos.length > 2){
        
            if(Utilidades.esDNI(listaDatos[0])){
                docStr += "<id>"+listaDatos[0] +"</id>";

            }else if(Utilidades.esNIF(listaDatos[0])){
                docStr += "<id>"+listaDatos[0] +"</id>";
            }else{
                   docStr+="<!-- No se encontro un id valido para el documento -->";
                   showAlert("No se encontro un id valido para el documento.", "Error");
                   hayDatosErroneos = true; 
            }
            
            docStr += "<nombre>"+Utilidades.limpiarStr(listaDatos[1])+"</nombre>";
            docStr += "<apellidos>"+Utilidades.limpiarStr(listaDatos[2])+"</apellidos>";
            List<String> tels = new ArrayList<String>();
            List<String> mails = new ArrayList<String>();
            
            String comCorreo = "";
            String comTels = "";
            
            int conCorreo = 0;
            int conTels = 0;
            
            for(int i = 3; i<listaDatos.length; i++){
               String aux =  Utilidades.limpiarStr(listaDatos[i]);
               if (Utilidades.esMail(aux.toLowerCase())){
                   if(!mails.contains(aux.toLowerCase())) mails.add(aux.toLowerCase());
                   else{
                       conCorreo++;
                       comCorreo+="<!-- El correo "+aux+" esta repetido en los datos de entrada -->";
                   }
               }else if(Utilidades.esTelf(aux)){
                   if (!tels.contains(aux)) tels.add(aux);
                   else{
                       conTels++;
                       comTels+="<!-- El telefono "+aux+" esta repetido en los datos de entrada -->";
                   }
               }else{
                   String men = "Error en los datos.";
                   if(Utilidades.esTelErr(aux)){
                        men = "El telefono "+aux+" no es un telefono valido";
                   }else{
                        men = "El correo "+aux+" no es un correo valido";
                   }
                   
                    docStr+="<!-- "+men+" -->";
                    showAlert(men, "Error");
                    hayDatosErroneos = true;
               }
            }
            
            
            Collections.sort(tels, new OrdnadorTels());
            
            String telsStr = "";
            for(String f: tels){
                telsStr+="<telefono>"+f+"</telefono>";
            }
            
            docStr+=comTels;
            docStr+="<telefonos total=\""+tels.size()+"\">"+telsStr+"</telefonos>";
            
            String mailsStr = "";
            for(String m: mails){
                 mailsStr+="<mail>"+m+"</mail>";
            }
            
            
            docStr+=comCorreo;
            docStr+="<mails>"+mailsStr+"</mails>";
            
            String men = " procesados correctamente";
            if (hayDatosErroneos)
                men = "No se han añadido algunos datos erroneos.";
            
            String prulTel = (conTels == 1 ? conTels + " telefono repetido. " : conTels + " telefonos repetidos. ");
            String prulCorreo = (conCorreo == 1 ? conCorreo +" correo repetido ": conCorreo+" correos repetidos ");
            
            
            
            lblInf.setText(men);
            showAlert( men +"\n"
                    + " Tambien ha habido "+prulCorreo +" y \n"
                            +prulTel, "Terminado");
                   
        }else{
           showAlert("Los datos no tienen ningun formato conocido.", "Error");
           lblInf.setText("Error al procesar.");
        }
        
        puedoCreaXML = true;
        enabledBtns(true);
        
    }
    
    
    public void enabledBtns(boolean d){
        btnAddText.setEnabled(d);
        btnCrearXML.setEnabled(d);
    }
    
    private void showAlert(String men, String title) {
       
        JOptionPane.showConfirmDialog(this, men, title, JOptionPane.CLOSED_OPTION);
       
    }
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Application a =  new Application();
                Toolkit tool = Toolkit.getDefaultToolkit();
                Dimension d = tool.getScreenSize();
                Dimension dwin = a.getSize();
                a.setLocation(d.width/2-dwin.width, d.height/2-dwin.height);
            }
        });
    }

}
