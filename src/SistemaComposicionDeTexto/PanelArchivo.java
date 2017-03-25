package SistemaComposicionDeTexto;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class PanelArchivo extends JPanel{
    private final JButton seleccionar, generar, mostrar;
    private final JPanel psur;
    private final JTextPane txp;
    private final JScrollPane jsp;
    public int contador = 0;
    private JFileChooser abrirArchivo;
    String contenido = "";
    String nombre, nombrePDF;
    
    public PanelArchivo(){
        seleccionar = new JButton("Seleccionar archivo");
        generar = new JButton("Generar PDF");
        mostrar = new JButton("Mostrar PDF");
        //Se crea el editor de texto y se agrega a un scroll
        txp = new JTextPane();
        jsp = new JScrollPane();
        psur = new JPanel();
        armado();
    }
    
    public void armado(){
        setLayout(new BorderLayout());
        add(psur, BorderLayout.SOUTH);
        psur.setLayout(new GridLayout(1, 3, 10, 10));
        psur.add(seleccionar);
        psur.add(generar);
        psur.add(mostrar);
        jsp.setViewportView(txp);
        add(jsp, BorderLayout.CENTER);
 
        seleccionar.addActionListener(new selecciona());
        generar.addActionListener(new genera());
        mostrar.addActionListener(new muestra());
    }
    
    private class selecciona implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if( seleccionar.getText().equals( "Seleccionar archivo" ) ){
                if(abrirArchivo == null) abrirArchivo = new JFileChooser();
                
                JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT","txt");
		abrirArchivo.setFileFilter(filter);
                //Con esto solamente podamos abrir archivos
                abrirArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
 
                int seleccion = abrirArchivo.showOpenDialog(abrirArchivo);
 
                if( seleccion == JFileChooser.APPROVE_OPTION ){
                    File f = abrirArchivo.getSelectedFile();
                    try{
                        nombre = f.getName();
                        String path = f.getAbsolutePath();
                        String contenido = getArchivo(path);
 
                        //En el editor de texto colocamos su contenido
                        txp.setText(contenido);
 
                        }catch( Exception exp){}
                }
            }
        }
    }
  
    private class genera implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae)  {
            FileOutputStream archivo = null;
            try {
                nombrePDF = nombre+".pdf";
                archivo = new FileOutputStream(nombrePDF);
                Document documento = new Document();
                PdfWriter.getInstance(documento, archivo);
                documento.open();
                documento.add(new Paragraph(contenido));
                documento.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PanelArchivo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(PanelArchivo.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    archivo.close();
                } catch (IOException ex) {
                    Logger.getLogger(PanelArchivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
    }
    
    private class muestra implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                File path = new File (nombrePDF);
                Desktop.getDesktop().open(path);
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //-------------------------Se obtiene el contenido del Archivo----------------//
    public String getArchivo( String ruta ){
        FileReader fr = null;
        BufferedReader br = null;
        //Cadena de texto donde se guardara el contenido del archivo
//        String contenido = "";
        try{
            //ruta puede ser de tipo String o tipo File
            fr = new FileReader(ruta);
            br = new BufferedReader(fr);
 
            String linea;
            //Obtenemos el contenido del archivo linea por linea
            while((linea = br.readLine()) != null){ 
                contenido += linea + "\n";
            }
        }catch( Exception e ){  }
        //finally se utiliza para que si todo ocurre correctamente o si ocurre 
        //algun error se cierre el archivo que anteriormente abrimos
        finally{
            try{
                br.close();
            }catch( Exception ex ){}
        }
        return contenido;
    }
    
}
