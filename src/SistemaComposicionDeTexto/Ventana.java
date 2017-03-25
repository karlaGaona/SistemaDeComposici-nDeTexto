package SistemaComposicionDeTexto;

import javax.swing.JInternalFrame;

public final class Ventana extends JInternalFrame{
    
    private final PanelArchivo panel;
    
    public Ventana(){
        panel = new PanelArchivo();
        add(panel);
        personalizar();
        mostrar();
    }
    
    public void personalizar(){
        setSize(500, 500);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
    }
    
    public void mostrar(){
        setVisible(true);
    }
    
}